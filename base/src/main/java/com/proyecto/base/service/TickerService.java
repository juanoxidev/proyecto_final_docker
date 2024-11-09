package com.proyecto.base.service;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.TickerDTO;
import com.proyecto.base.model.Usuario;

public interface TickerService {

	DataTablesResponse<TickerDTO> getRespuestaDatatable(DataTablesRequestForm<TickerDTO> dtRequest);

	ResponseDTO<TickerDTO> altaTicker(TickerDTO formCreacion, Usuario usuario);

	ResponseDTO<TickerDTO> editarTicker(TickerDTO formCreacion, Usuario usuario);

}
