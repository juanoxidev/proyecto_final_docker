package com.proyecto.base.service;


import java.util.Set;

import javax.transaction.TransactionScoped;
import javax.transaction.Transactional;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestBody;

import com.proyecto.base.config.PasswordGenerator;
import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.datatable.IDatatable;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.filter.UsuarioFilter;
import com.proyecto.base.model.Rol;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.RolRepository;
import com.proyecto.base.repository.UsuarioRepository;
import com.proyecto.base.util.UtilesFechas;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UsuarioServiceImpl implements UsuarioService, IDatatable<Usuario>{
	
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	@Autowired
	private RolRepository rolRepository;
	
	@Autowired
	private PasswordGenerator passwordGenerator;
	
	@Autowired
	@Lazy
	private PasswordEncoder passwordEncoder;
	
	@Autowired 
	private EmailService emailService;
	
	private static final int MAX_INTENTOS_FALLIDOS = 3;
	 
	@Transactional
	public ResponseDTO<UsuarioDTO> altaUsuario (UsuarioDTO dto, Usuario usuarioAuditoria){
		ResponseDTO<UsuarioDTO> resp = new ResponseDTO<UsuarioDTO>();
		
		
		try {	
				validarUsuario(dto);
		} catch (Exception e ) {
				log.info(e.getMessage());
				throw new BaseException(e.getMessage());
		}
		
		Rol rol = rolRepository.findById(dto.getIdRol())
		            .orElseThrow(() -> new BaseException("Rol no encontrado"));

        String generatedPassword = passwordGenerator.generatePassword();


        String hashedPassword = passwordEncoder.encode(generatedPassword);
        
        Set<Rol> roles = new HashSet<>();
        roles.add(rol);
        

        Usuario usuario = Usuario.builder()
        				.username(dto.getUsername().toLowerCase())
        				.email(dto.getEmail().toLowerCase())
        				.apellido(dto.getApellido().toLowerCase())
        				.nombre(dto.getNombre().toLowerCase())
        				.estado(dto.getEstado())
        				.password(hashedPassword)
        				.passwordACambiar(true)
        				.roles(roles)
        				.intentosFallidos(0)
        				.build();

        Usuario usuarioBD = usuarioRepository.save(usuario);

        log.info("Se ha creado el usuario #{}-{} - {}",usuarioBD.getId(), usuario.getUsername(), usuarioAuditoria.getUsername());
        
        sendNotificationUser(dto.getEmail(), generatedPassword, dto.getUsername().toLowerCase()); 
        
        resp.setOk(true);
        resp.setLog("Se creo el usuario correctamente.");
		return resp;
	}

	   private void sendPasswordChangeNotification(String username, String email) {
	        String subject = "Cambio de Contraseña Exitoso";
	        String body = "Hola " + username + ",\n\nTu contraseña ha sido cambiada exitosamente." +
	                      "\nSi no realizaste este cambio, por favor contacta a soporte de inmediato.\n\nSaludos,\nEl equipo de soporte";
	        emailService.sendEmail(email, subject, body);
	    }
	

	private void sendNotificationUser(String email, String password, String username) {
	    String subject = "Contraseña provisoria para tu cuenta";
	    String body = "Hola " + username + ",\n\nTu cuenta ha sido creada exitosamente. Tu contraseña provisoria es: " + password +
	                  "\nPor favor, cámbiala al iniciar sesión.\n\nSaludos,\nEl equipo de soporte";
	    emailService.sendEmail(email, subject, body);
	}

	private void validarUsuario(UsuarioDTO dto) {
		
	       if (usuarioRepository.existsByUsername(dto.getUsername())) {
	            throw new IllegalArgumentException("El nombre de usuario ya está en uso.");
	        }
	       
	       if (usuarioRepository.existsByEmail(dto.getEmail())) {
	    	   throw new IllegalArgumentException("El email de usuario ya está en uso.");
	       }
		
	}
	
    public Set<Rol> getRolesByUsername(String username) {
        Usuario usuario = usuarioRepository.findByUsername(username)
            .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return usuario.getRoles();
    }
    
    
    public ResponseDTO<UsuarioDTO> login(UsuarioDTO usuario) {
        Usuario user = usuarioRepository.findByUsername(usuario.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        ResponseDTO<UsuarioDTO> resp = new ResponseDTO<UsuarioDTO>();

        if (user.getEstado() == EstadosEnum.BAJA) {
        	resp.setOk(false);
        	resp.setLog("El usuario se encuentra bloqueado");
        	 return resp;
        }
        

        if (passwordEncoder.matches(usuario.getPassword(), user.getPassword())) {
            resetFailedAttempts(usuario);
            resp.setEntity(usuario);
            resp.setLog(null);
            resp.setOk(true);
            return resp;
      
        } else {
            handleFailedAttempt(usuario, user);
            resp.setEntity(usuario);
        	resp.setOk(false);
        	resp.setLog("Usuario o contrasenia incorrecta");
        	return resp;
        }
    }


    private void handleFailedAttempt(UsuarioDTO user, Usuario usuario) {
        int failedAttempts = user.getIntentosFallidos() + 1;
        user.setIntentosFallidos(failedAttempts);
        user.setFechaUltimoIntento(UtilesFechas.getFechaFormateada(new Date()));

        if (failedAttempts >= MAX_INTENTOS_FALLIDOS) {
        	usuario.setEstado(EstadosEnum.BAJA);
        }

        usuarioRepository.save(usuario);
    }

    private void resetFailedAttempts(UsuarioDTO user) {
        user.setIntentosFallidos(0);
        user.setFechaUltimoIntento(null);
    }

	@Override
	public DataTablesResponse<UsuarioDTO> getRespuestaDatatable(DataTablesRequestForm<UsuarioDTO> dtRequest) {
		int start = dtRequest.getStart();
		int limit = dtRequest.getLength();
		UsuarioDTO criteriosDeBusqueda = dtRequest.getFormBusqueda();
		UsuarioFilter filter = new UsuarioFilter(criteriosDeBusqueda);
		List<Usuario> usuarioBD = this.usuarioRepository.list(filter.sentenciaHQL(), start, limit);
		List<UsuarioDTO> usuariosDTO = this.getDTOs(UsuarioDTO::crearDTOBusqueda, usuarioBD);
		Long cantidadRegistros = this.usuarioRepository.count(filter.sentenciaHQLCount());
		return this.getRespuestaDatatable(usuariosDTO, cantidadRegistros);
	}



	@Override
	@Transactional
	public ResponseDTO<UsuarioDTO> editarUsuario(UsuarioDTO formCreacion, Usuario usuario) {
		ResponseDTO<UsuarioDTO> resp = new ResponseDTO<UsuarioDTO>();
		Rol rolForm = rolRepository.getReferenceById(formCreacion.getIdRol());
		String nombreForm = formCreacion.getNombre();
		String apellidoForm = formCreacion.getApellido();
		String emailForm = formCreacion.getEmail();
		String usernameForm = formCreacion.getUsername();
		EstadosEnum estadoForm = formCreacion.getEstado();
		Set<Rol> rolFormSet =  new HashSet<Rol>();
		rolFormSet.add(rolForm);
		boolean cambio = false;
		
		Usuario usuarioBD = usuarioRepository.getReferenceById(formCreacion.getId());
		
		if(StringUtils.hasText(nombreForm) && !usuarioBD.esMiNombre(nombreForm)) {
			usuarioBD.setNombre(nombreForm.toLowerCase());
			cambio = true;
		}
		
		if(StringUtils.hasText(apellidoForm) && !usuarioBD.esMiApellido(apellidoForm)) {
			usuarioBD.setApellido(apellidoForm);
			cambio = true;
		}
		
		if(!usuarioBD.esMiEstado(estadoForm)) {
			usuarioBD.setEstado(estadoForm);
			cambio = true;
		}
		
		if(!usuarioBD.esMiEmail(emailForm)) {
			validarEmail(emailForm);
			usuarioBD.setEmail(emailForm);
			cambio = true;
		}
		
		if(!usuarioBD.esMiUsername(usernameForm)) {
			validarUsername(usernameForm);
			usuarioBD.setUsername(usernameForm);
			cambio = true;
		}
		
		if(!usuarioBD.esMiRol(rolForm)) {
			usuarioBD.setRoles(rolFormSet);
			cambio = true;
		}
		
		if (cambio) {
			usuarioRepository.save(usuarioBD);
			resp.setLog(String.format("Se ha modificado el usuario %s ", usuarioBD.getUsername()));
		} else {
			resp.setLog(String.format("No se han enviado modificaciones para el usuario %s", usuarioBD.getUsername()));
		}
		
		resp.setOk(cambio);
		
		return resp;
	}

	private void validarUsername(String usernameForm) {
	       if (usuarioRepository.existsByUsername(usernameForm)) {
	    	   throw new BaseException("El username ya está en uso.");
	       }
		
	}




	private void validarEmail(String emailForm) {
	       if (usuarioRepository.existsByEmail(emailForm)) {
	    	   throw new BaseException("El email de usuario ya está en uso.");
	       }
		
	}




	public Usuario getUsuario() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}




	@Override
	@Transactional
	public boolean cambiarContrasenia(String username, String newPassword) {
		boolean resultado = false;
		try {
        Usuario usuario = usuarioRepository.findByUsername(username)
                                        .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        usuario.setPassword(passwordEncoder.encode(newPassword));
        usuario.setPasswordACambiar(false);
        usuarioRepository.save(usuario);
        this.sendPasswordChangeNotification(username, usuario.getEmail());
        resultado = true;
        
		} catch (Exception e) {
			e.printStackTrace();
			log.warn(e.getMessage());
		}
		return resultado;
        
	}

	@Override
	@Transactional
	public boolean restablecerContrasenia(String email) {
			boolean resultado = false;
			try {
		        Usuario usuario = usuarioRepository.findByEmailIgnoreCase(email);
		         if (usuario == null ) {
		        	 throw new BaseException("El email no corresponde a un usuario del sistema");
		         }
		        
		
		          
		            String provisionalPassword = passwordGenerator.generatePassword();
		            String hashedPassword = passwordEncoder.encode(provisionalPassword);
		            usuario.setPassword(hashedPassword);
		            usuario.setPasswordACambiar(true);
		            usuarioRepository.save(usuario);
		            
		       
		            this.sendRestablecerContrasenia(usuario.getEmail(), provisionalPassword, usuario.getUsername());
		            resultado = true;
			} catch (Exception e) {
				e.printStackTrace();
				log.warn(e.getMessage());
			}
					return resultado;
		          

	}

	
    public void sendRestablecerContrasenia(String email, String password, String username) {
        String subject = "Contraseña provisoria para tu cuenta";
        String body = "Hola " + username + ",\n\nTu contraseña provisoria es: " + password +
                      "\nPor favor, cámbiala al iniciar sesión.\n\nSaludos,\nEl equipo de soporte";
        
        emailService.sendEmail(email, subject, body);
    }






}
