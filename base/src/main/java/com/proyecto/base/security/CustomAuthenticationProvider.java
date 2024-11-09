package com.proyecto.base.security;



import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.UsuarioRepository;
import com.proyecto.base.service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider {

	@Autowired
	private UserDetailsService userDetailsService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired 
    private UsuarioRepository usuarioRepository;
    
    private final int MAX_INTENTOS = 3;
    
   
    


    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = authentication.getName();
        String password = authentication.getCredentials().toString();
        StringBuffer mensaje = new StringBuffer();
        Usuario usuario;
        
        try {
             usuario = (Usuario) userDetailsService.loadUserByUsername(username);
        } catch (UsernameNotFoundException e) {
        	log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Se intento loguear un usuario que no existe {}", username);
        	throw new BadCredentialsException("Usuario o contraseña incorrecta");
        }
       log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> Ingreso al Login");
       log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> El usuario {} esta intentando ingresar al sistema", usuario.getUsername());
       

       if (!usuario.isEnabled()) {
    	   log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> El usuario {} esta dada de baja", usuario.getUsername());
           throw new DisabledException("Cuenta dada de baja");
           
          
       }

//        if (!usuario.isAccountNonLocked()) {
//        	log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> La cuenta del usuario {} se encuentra bloqueada al fallar los 3 intentos de inicio de sesion", usuario.getUsername());
//            throw new BadCredentialsException("Cuenta bloqueada.");
//        }

        if (!passwordEncoder.matches(password, usuario.getPassword())) {
         	log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> El usuario {} ha ingresado una contrasenia incorrecta", usuario.getUsername());
         	usuario.setIntentosFallidos( usuario.getIntentosFallidos() + 1);
         	if(usuario.getIntentosFallidos() >= MAX_INTENTOS) {
         		usuario.setEstado(EstadosEnum.BAJA);
         	}
         	 usuarioRepository.save(usuario);

            throw new BadCredentialsException("Usuario o contraseña incorrecta");
        }
        
        if (usuario.getIntentosFallidos() > 0) {
        	usuario.setIntentosFallidos(0);
        	 usuarioRepository.save(usuario);
        }
        
        log.info(">>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>> El usuario {} se ha logueado correctamente", usuario.getUsername());
        return new UsernamePasswordAuthenticationToken(usuario, password, usuario.getAuthorities());
       
    }



	@Override
    public boolean supports(Class<?> authentication) {
        return UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication);
    }
}
