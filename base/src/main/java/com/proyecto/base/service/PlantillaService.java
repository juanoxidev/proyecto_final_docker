package com.proyecto.base.service;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.PlantillaDTO;
import com.proyecto.base.model.Plantilla;
import com.proyecto.base.model.Usuario;

public interface PlantillaService {
	public DataTablesResponse<AlycDTO> getRespuestaDatatable(DataTablesRequestForm<AlycDTO> dtRequest);

	public ResponseDTO<PlantillaDTO> altaPlantilla(PlantillaDTO formCreacion, Usuario usuario);
	
	public ResponseDTO<PlantillaDTO> editarPlantilla(PlantillaDTO formCreacion, Usuario usuario);
}
