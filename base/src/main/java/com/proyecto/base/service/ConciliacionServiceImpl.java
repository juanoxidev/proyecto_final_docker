package com.proyecto.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.datatable.IDatatable;
import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.ConciliacionDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.filter.ConciliacionFilter;
import com.proyecto.base.filter.UsuarioFilter;
import com.proyecto.base.model.Alyc;
import com.proyecto.base.repository.AlycRepository;

@Service
public class ConciliacionServiceImpl implements ConciliacionService, IDatatable<Alyc> {
	
	@Autowired
	private AlycRepository alycRepository;

	@Override
	@Transactional
	public DataTablesResponse<AlycDTO> getRespuestaDatatable(DataTablesRequestForm<ConciliacionDTO> dtRequest) {
		int start = dtRequest.getStart();
		int length = dtRequest.getLength();
		ConciliacionDTO criteriosDeBusqueda = dtRequest.getFormBusqueda();
		ConciliacionFilter filter = new ConciliacionFilter(criteriosDeBusqueda);
		List<Alyc> alycsBD = alycRepository.list(filter.sentenciaHQL(),start, length);
		List<AlycDTO> dtos = this.getDTOs(AlycDTO::crearDTOConciliacion, alycsBD);
		Long records = alycRepository.count(filter.sentenciaHQLCount());
		return this.getRespuestaDatatable(dtos, records);
	}

}
