package com.proyecto.base.model;

import javax.persistence.*;

import org.apache.commons.text.WordUtils;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.base.converter.EstadoConverter;
import com.proyecto.base.enums.EstadosEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Date;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
@Table(name = "usuario")
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nombre;
    private String apellido;
    private String email;
    private String username;
    private String password;
    
	@Convert(converter = EstadoConverter.class)
	@Column(name = "ESTADO")
    private EstadosEnum estado;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "usuario_rol",
        joinColumns = @JoinColumn(name = "usuario_id"),
        inverseJoinColumns = @JoinColumn(name = "rol_id")
    )
    private Set<Rol> roles;
    
    @Column(name= "PASSWORD_ACAMBIAR")
    private Boolean passwordACambiar;
    
    @Column(name="INTENTOS_FALLIDOS")
    private Integer intentosFallidos; 

    @Column(name="ULTIMO_INTENTO")
    private Date fechaUltimoIntento;
    
	@Embedded
	@JsonIgnore
	private Auditoria auditoria;



    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(Rol::getAuthority) 
                .map(SimpleGrantedAuthority::new)  
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
    	return true; 
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
    	 return this.estado == EstadosEnum.ACTIVO;
    }

	public boolean esMiNombre(String nombreForm) {
		return this.nombre.equalsIgnoreCase(nombreForm);
	}

	public boolean esMiApellido(String apellidoForm) {
		return this.apellido.equalsIgnoreCase(apellidoForm);
	}

	public boolean esMiEstado(EstadosEnum estadoForm) {
		return this.estado == estadoForm;
	}

	public boolean esMiRol(Rol rolForm) {
		return this.roles.stream().anyMatch(rol -> rol.getId().equals(rolForm.getId()));
	}
	
	
	@Transient
	public String getSiglas() {
		StringBuffer aux = new StringBuffer("");
		if(getNombre()!=null){
			aux.append(getNombre().charAt(0));
		}
		if(getNombre()!=null){
			aux.append(getApellido().charAt(0));
		}
		return WordUtils.capitalizeFully(aux.toString().toUpperCase());
	}
	
	
	@Transient
	public String getRolDescrip() {
		return this.roles.iterator().next().getDescripcion();
	}

	public boolean esMiEmail(String emailForm) {
		return this.email.trim().equalsIgnoreCase(emailForm.trim());
	}

	public boolean esMiUsername(String usernameForm) {
		return this.username.trim().equalsIgnoreCase(usernameForm.trim());
	}
}