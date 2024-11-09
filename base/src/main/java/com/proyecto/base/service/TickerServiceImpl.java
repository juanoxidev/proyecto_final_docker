package com.proyecto.base.service;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.datatable.IDatatable;
import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.dto.PlantillaDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.TickerDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.filter.TickerFilter;
import com.proyecto.base.model.Alyc;
import com.proyecto.base.model.Auditoria;
import com.proyecto.base.model.Plantilla;
import com.proyecto.base.model.Ticker;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.TickerRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TickerServiceImpl implements TickerService, IDatatable<Ticker> {

	@Autowired
	private TickerRepository tickerRepository;
	
	@Override
	public DataTablesResponse<TickerDTO> getRespuestaDatatable(DataTablesRequestForm<TickerDTO> dtRequest) {
		int start = dtRequest.getStart();
		int limit = dtRequest.getLength();
		TickerDTO criteriosDeBusqueda = dtRequest.getFormBusqueda();
		TickerFilter filter = new TickerFilter(criteriosDeBusqueda);
		List<Ticker> tickersBD = this.tickerRepository.list(filter.sentenciaHQL(), start, limit);
		List<TickerDTO> tickersDTO = this.getDTOs(TickerDTO::crearDTOBusqueda, tickersBD);
		Long cantidadRegistros = this.tickerRepository.count(filter.sentenciaHQLCount());
		return this.getRespuestaDatatable(tickersDTO, cantidadRegistros);
	}
	
	
	@Transactional
	public ResponseDTO<TickerDTO> altaTicker (TickerDTO dto, Usuario usuarioAuditoria){
		ResponseDTO<TickerDTO> resp = new ResponseDTO<TickerDTO>();
		
		
		try {
			validarTicker(dto);
			
			Ticker ticker = Ticker.builder()
							 .auditoria(Auditoria.crearAuditoria(usuarioAuditoria))
							 .descripcion(dto.getTickerDescripcion().toUpperCase())
							 .estado(dto.getEstado())
							 .nombre(dto.getTickerName().toUpperCase())
							 .build();
			
			Ticker tickerBD = tickerRepository.save(ticker);

	        log.info("Se ha creado el ticker {}",tickerBD.getNombre());
	        resp.setOk(true);
	        resp.setLog("Se creo el ticker correctamente.");
			
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new BaseException(e.getMessage());
		}
			
		return resp;
	}


	private void validarTicker(TickerDTO dto) {
		
		if (!StringUtils.hasText(dto.getTickerName())) {
			throw new BaseException("Debe indicar el nombre del ticker");
		}
		
		if (!StringUtils.hasText(dto.getTickerDescripcion())) {
			throw new BaseException("Debe indicar la descripcion del ticker");
		}
		
		if (dto.getEstado() == null) {
			throw new BaseException("Debe indicar el estado del ticker");
		}
		
	}

	
	@Override
	@Transactional
	public ResponseDTO<TickerDTO> editarTicker(TickerDTO formCreacion, Usuario usuario) {
		ResponseDTO<TickerDTO> resp = new ResponseDTO<TickerDTO>();

		String nombreForm =  formCreacion.getTickerName();
		String descripcionForm = formCreacion.getTickerDescripcion();
		EstadosEnum estadoForm = formCreacion.getEstado();
		
		
		boolean cambio = false;
		
		Ticker tickerBD = tickerRepository.getReferenceById(formCreacion.getTickerId());
		
		if(StringUtils.hasText(nombreForm) && !tickerBD.esMiNombre(nombreForm)) {
			tickerBD.setNombre(nombreForm.toUpperCase());
			cambio = true;
		}
		
		if(StringUtils.hasText(descripcionForm) && !tickerBD.esMiDescripcion(descripcionForm)) {
			tickerBD.setDescripcion(descripcionForm.toUpperCase());
			cambio = true;
		}
		
		if(estadoForm != null && !tickerBD.esMiEstado(estadoForm)) {
			tickerBD.setEstado(estadoForm);
			cambio = true;
		}
		
		


		if (cambio) {
			
			Auditoria auditoriaPlantilla = tickerBD.getAuditoria();
			Auditoria.modificar(auditoriaPlantilla, usuario);
			tickerBD.setAuditoria(auditoriaPlantilla);
			tickerRepository.save(tickerBD);


			resp.setLog(String.format("Se ha modificado el ticker %s ", tickerBD.getNombre()));
		} else {

			resp.setLog(String.format("No se han enviado modificaciones para el ticker %s", tickerBD.getNombre()));
		}
		
		resp.setOk(cambio);
		
		return resp;
	}
}
