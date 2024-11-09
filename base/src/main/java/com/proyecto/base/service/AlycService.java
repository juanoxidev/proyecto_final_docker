package com.proyecto.base.service;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.model.Usuario;

public interface AlycService {

	DataTablesResponse<AlycDTO> getRespuestaDatatable(DataTablesRequestForm<AlycDTO> dtRequest);

	ResponseDTO<AlycDTO> altaAlyc(AlycDTO formCreacion, Usuario usuario);

	ResponseDTO<AlycDTO> editarAlyc(AlycDTO formCreacion, Usuario usuario);


}
