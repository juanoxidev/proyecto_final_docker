package com.proyecto.base.service;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.model.Usuario;

public interface OperacionService {
	public DataTablesResponse<OperacionDTO> getRespuestaDatatable(DataTablesRequestForm<OperacionDTO> dtRequest);

	public ResponseDTO<OperacionDTO> altaOperacion(OperacionDTO formCreacion, Usuario usuario);
	
	public ResponseDTO<OperacionDTO> editarOperacion(OperacionDTO formCreacion, Usuario usuario);

	public byte[] getOperacionesSegunAlycEntreFechas(OperacionDTO operacionDTO);
}
