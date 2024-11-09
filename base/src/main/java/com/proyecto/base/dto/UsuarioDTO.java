package com.proyecto.base.dto;

import java.util.function.Function;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.model.Usuario;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
	private Long id;
    private String nombre;
    private String apellido;
	private String username;
	private String email;
	private String password;
	private EstadosEnum estado;
	private int intentosFallidos;
	private String fechaUltimoIntento;
	private String rol;
	private Long idRol;
	
	
	public static UsuarioDTO crearDTOBusqueda(Usuario usuario) {
		UsuarioDTO dto = UsuarioDTO.builder()
					  .id(usuario.getId())
					  .nombre(usuario.getNombre().toUpperCase())
					  .apellido(usuario.getApellido().toUpperCase())
					  .username(usuario.getUsername().toUpperCase())
					  .email(usuario.getEmail().toUpperCase())
					  .rol(usuario.getRoles().iterator().next().getDescripcion().toUpperCase())
					  .estado(usuario.getEstado())
					  .build();
		
		return dto;
	}
}
