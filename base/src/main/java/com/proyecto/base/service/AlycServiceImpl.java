package com.proyecto.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.datatable.IDatatable;
import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.MonedaDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.TickerDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.filter.IFilter;
import com.proyecto.base.filter.PlantillaFilter;
import com.proyecto.base.model.Alyc;
import com.proyecto.base.model.Auditoria;
import com.proyecto.base.model.Moneda;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.AlycRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class AlycServiceImpl implements AlycService, IDatatable<Alyc> {
	
	@Autowired 
	private AlycRepository alycRepository;

	@Override
	public DataTablesResponse<AlycDTO> getRespuestaDatatable(DataTablesRequestForm<AlycDTO> dtRequest) {
		int start = dtRequest.getStart();
		int limit = dtRequest.getLength();

		AlycDTO criteriosDeBusqueda = dtRequest.getFormBusqueda();
		IFilter filter = new PlantillaFilter(criteriosDeBusqueda);
		List<Alyc> alycsBD = this.alycRepository.list(filter.sentenciaHQL(), start, limit);
		List<AlycDTO> alycsDTO = this.getDTOs(AlycDTO::crearDTOAlyc, alycsBD);
		Long cantidadRegistros = this.alycRepository.count(filter.sentenciaHQLCount());

		return this.getRespuestaDatatable(alycsDTO, cantidadRegistros);
	}


	@Override
	public ResponseDTO<AlycDTO> altaAlyc(AlycDTO formCreacion, Usuario usuario) {
		ResponseDTO<AlycDTO> resp = new ResponseDTO<AlycDTO>();
		try {
			validarAlyc(formCreacion);
			
			Alyc alyc = Alyc.builder()
							 .auditoria(Auditoria.crearAuditoria(usuario))
							 .porcentajeComision(formCreacion.getComision())
							 .estado(formCreacion.getEstado())
							 .nombre(formCreacion.getNombre())
							 .build();
			
			Alyc alycBD = alycRepository.save(alyc);

	        log.info("Se ha creado la Alyc {}", alycBD.getNombre());
	        resp.setOk(true);
	        resp.setLog("Se creo la Alyc correctamente.");
			
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new BaseException(e.getMessage());
		}
			
		return resp;
	}

	private void validarAlyc(AlycDTO dto) {
		if (!StringUtils.hasText(dto.getNombre())) {
			throw new BaseException("Debe indicar el nombre de la Alyc");
		}
		
		if (dto.getComision() == null) {
			throw new BaseException("Debe indicar comision de la Alyc");
		}
		
		if (dto.getComision()<0 || dto.getComision()>100) {
			throw new BaseException("La comision debe estar comprendida entre 0 y 100");
		}
			
		
		if (dto.getEstado() == null) {
			throw new BaseException("Debe indicar el estado de la moneda");
		}
		
	}


	@Override
	public ResponseDTO<AlycDTO> editarAlyc(AlycDTO formCreacion, Usuario usuario) {
		ResponseDTO<AlycDTO> resp = new ResponseDTO<AlycDTO>();

		String nombreForm =  formCreacion.getNombre();
		Double comisionForm = formCreacion.getComision();
		EstadosEnum estadoForm = formCreacion.getEstado();
		
		
		boolean cambio = false;
		
		Alyc alycBD = alycRepository.getReferenceById(formCreacion.getIdAlyc());
		
		if(StringUtils.hasText(nombreForm) && !alycBD.esMiNombre(nombreForm)) {
			alycBD.setNombre(nombreForm.toUpperCase());
			cambio = true;
		}
		
		if(comisionForm != null && !alycBD.esMiComision(comisionForm)) {
			alycBD.setPorcentajeComision(comisionForm);
			cambio = true;
		}
		
		if(estadoForm != null && !alycBD.esMiEstado(estadoForm)) {
			alycBD.setEstado(estadoForm);
			cambio = true;
		}
		
		


		if (cambio) {
			
			Auditoria auditoria = alycBD.getAuditoria();
			Auditoria.modificar(auditoria, usuario);
			alycBD.setAuditoria(auditoria);
			alycRepository.save(alycBD);


			resp.setLog(String.format("Se ha modificado la Alyc %s ", nombreForm));
		} else {

			resp.setLog(String.format("No se han enviado modificaciones para la Alyc %s", nombreForm));
		}
		
		resp.setOk(cambio);
		
		return resp;
	}
	
	
	

}
