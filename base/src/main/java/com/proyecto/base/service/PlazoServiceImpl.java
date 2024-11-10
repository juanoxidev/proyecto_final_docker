package com.proyecto.base.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.datatable.IDatatable;

import com.proyecto.base.dto.PlazoDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.TickerDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.filter.PlazoFilter;
import com.proyecto.base.model.Auditoria;
import com.proyecto.base.model.Plazo;
import com.proyecto.base.model.Ticker;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.PlazoRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PlazoServiceImpl implements PlazoService, IDatatable<Plazo> {
	
	@Autowired
	private PlazoRepository plazoRepository;

	@Override
	public DataTablesResponse<PlazoDTO> getRespuestaDatatable(DataTablesRequestForm<PlazoDTO> dtRequest) {
		int start = dtRequest.getStart();
		int limit = dtRequest.getLength();
		PlazoDTO criteriosDeBusqueda = dtRequest.getFormBusqueda();
		PlazoFilter filter = new PlazoFilter(criteriosDeBusqueda);
		List<Plazo> plazosBD = this.plazoRepository.list(filter.sentenciaHQL(), start, limit);
		List<PlazoDTO> plazosDTO = this.getDTOs(PlazoDTO::crearDTOBusqueda, plazosBD);
		Long cantidadRegistros = this.plazoRepository.count(filter.sentenciaHQLCount());
		return this.getRespuestaDatatable(plazosDTO, cantidadRegistros);
	}

	@Override
	@Transactional
	public ResponseDTO<PlazoDTO> editarPlazo(PlazoDTO formCreacion, Usuario usuario) {
		ResponseDTO<PlazoDTO> resp = new ResponseDTO<PlazoDTO>();

		String nombreForm =  formCreacion.getPlazoName();
		String descripcionForm = formCreacion.getPlazoDescripcion();
		EstadosEnum estadoForm = formCreacion.getEstado();
		
		
		boolean cambio = false;
		
		Plazo plazoBD = plazoRepository.getReferenceById(formCreacion.getPlazoId());
		
		if(StringUtils.hasText(nombreForm) && !plazoBD.esMiNombre(nombreForm)) {
			if (validarSiYaExiste(nombreForm)) {
				throw new BaseException("Ya existe un Plazo con ese nombre");
			}
			plazoBD.setNombre(nombreForm.toUpperCase());
			cambio = true;
		}
		
		if(StringUtils.hasText(descripcionForm) && !plazoBD.esMiDescripcion(descripcionForm)) {
			plazoBD.setDescripcionCompleta(descripcionForm.toUpperCase());
			cambio = true;
		}
		
		if(estadoForm != null && !plazoBD.esMiEstado(estadoForm)) {
			plazoBD.setEstado(estadoForm);
			cambio = true;
		}
		
		
		if (cambio) {
			
			Auditoria auditoria = plazoBD.getAuditoria();
			Auditoria.modificar(auditoria, usuario);
			plazoBD.setAuditoria(auditoria);
			plazoRepository.save(plazoBD);


			resp.setLog(String.format("Se ha modificado el plazo %s ", formCreacion.getPlazoName()));
		} else {

			resp.setLog(String.format("No se han enviado modificaciones para el plazo %s", formCreacion.getPlazoName()));
		}
		
		resp.setOk(cambio);
		
		return resp;
	}

	private boolean validarSiYaExiste(String nombreForm) {
		return plazoRepository.existsByNombre(nombreForm.trim());
	}

	@Override
	public ResponseDTO<PlazoDTO> crearPlazo(PlazoDTO dto, Usuario usuario) {
		ResponseDTO<PlazoDTO> resp = new ResponseDTO<PlazoDTO>();
		try {
			validarPlazo(dto);
			
			Plazo plazo = Plazo.builder()
							 .auditoria(Auditoria.crearAuditoria(usuario))
							 .descripcionCompleta(dto.getPlazoDescripcion().toUpperCase())
							 .estado(dto.getEstado())
							 .nombre(dto.getPlazoName().toUpperCase())
							 .build();
			
			Plazo plazoBD = plazoRepository.save(plazo);

	        log.info("Se ha creado el plazo {}",plazoBD.getNombre());
	        resp.setOk(true);
	        resp.setLog("Se creo el plazo correctamente.");
			
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new BaseException(e.getMessage());
		}
			
		return resp;
	}

	private void validarPlazo(PlazoDTO dto) {
		
		
		if (!StringUtils.hasText(dto.getPlazoName())) {
			throw new BaseException("Debe indicar el nombre del plazo");
		}
		
		if (validarSiYaExiste(dto.getPlazoName().trim())) {
			throw new BaseException("Ya existe un Plazo con ese nombre");
		}
		
		if (!StringUtils.hasText(dto.getPlazoDescripcion())) {
			throw new BaseException("Debe indicar la descripcion del plazo");
		}
		
		if (dto.getEstado() == null) {
			throw new BaseException("Debe indicar el estado del plazo");
		}
		
	}

}
