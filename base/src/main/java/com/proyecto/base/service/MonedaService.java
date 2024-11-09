package com.proyecto.base.service;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.MonedaDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.model.Usuario;

public interface MonedaService {

	DataTablesResponse<MonedaDTO> getRespuestaDatatable(DataTablesRequestForm<MonedaDTO> dtRequest);


	ResponseDTO<MonedaDTO> editarMoneda(MonedaDTO formCreacion, Usuario usuario);


	ResponseDTO<MonedaDTO> crearMoneda(MonedaDTO formCreacion, Usuario usuario);

}
