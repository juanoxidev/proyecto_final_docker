package com.proyecto.base.service;


import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.model.Usuario;

public interface UsuarioService {

	public ResponseDTO<UsuarioDTO> login(UsuarioDTO usuario);

	public DataTablesResponse<UsuarioDTO> getRespuestaDatatable(DataTablesRequestForm<UsuarioDTO> dtRequest);

	public ResponseDTO<UsuarioDTO> altaUsuario(UsuarioDTO formCreacion, Usuario usuario);

	public ResponseDTO<UsuarioDTO> editarUsuario(UsuarioDTO formCreacion, Usuario usuario);

	public boolean cambiarContrasenia(String name, String newPassword);
	
	public boolean restablecerContrasenia(String email);
	
}
