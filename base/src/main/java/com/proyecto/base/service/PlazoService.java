package com.proyecto.base.service;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.PlazoDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.model.Usuario;

public interface PlazoService {

	DataTablesResponse<PlazoDTO> getRespuestaDatatable(DataTablesRequestForm<PlazoDTO> dtRequest);

	ResponseDTO<PlazoDTO> editarPlazo(PlazoDTO formCreacion, Usuario usuario);

	ResponseDTO<PlazoDTO> crearPlazo(PlazoDTO formCreacion, Usuario usuario);

}
