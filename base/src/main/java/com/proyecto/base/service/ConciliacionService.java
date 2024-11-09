package com.proyecto.base.service;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.ConciliacionDTO;

public interface ConciliacionService {

	DataTablesResponse<AlycDTO> getRespuestaDatatable(DataTablesRequestForm<ConciliacionDTO> dtRequest);

}
