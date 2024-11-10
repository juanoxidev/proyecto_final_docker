package com.proyecto.base.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.datatable.IDatatable;
import com.proyecto.base.dto.MonedaDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.filter.MonedaFilter;
import com.proyecto.base.model.Auditoria;
import com.proyecto.base.model.Moneda;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.MonedaRepository;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MonedaServiceImpl implements MonedaService, IDatatable<Moneda> {
	
	@Autowired
	private MonedaRepository monedaRepository;

	@Override
	public DataTablesResponse<MonedaDTO> getRespuestaDatatable(DataTablesRequestForm<MonedaDTO> dtRequest) {
		int start = dtRequest.getStart();
		int limit = dtRequest.getLength();
		MonedaDTO criteriosDeBusqueda = dtRequest.getFormBusqueda();
		MonedaFilter filter = new MonedaFilter(criteriosDeBusqueda);
		List<Moneda> monedasBD = this.monedaRepository.list(filter.sentenciaHQL(), start, limit);
		List<MonedaDTO> monedasDTO = this.getDTOs(MonedaDTO::crearDTOBusqueda, monedasBD);
		Long cantidadRegistros = this.monedaRepository.count(filter.sentenciaHQLCount());
		return this.getRespuestaDatatable(monedasDTO, cantidadRegistros);
	}

	@Override
	public ResponseDTO<MonedaDTO> editarMoneda(MonedaDTO formCreacion, Usuario usuario) {
		ResponseDTO<MonedaDTO> resp = new ResponseDTO<MonedaDTO>();

		String nombreForm =  formCreacion.getMonedaName();
		String descripcionForm = formCreacion.getMonedaDescripcion();
		EstadosEnum estadoForm = formCreacion.getEstado();
		
		
		boolean cambio = false;
		
		Moneda monedaBD = monedaRepository.getReferenceById(formCreacion.getMonedaId());
		
		if(StringUtils.hasText(nombreForm) && !monedaBD.esMiNombre(nombreForm)) {
			if(validarSiYaExiste(nombreForm)) {
				throw new BaseException("Ya existe una Moneda con ese nombre");
			};
			monedaBD.setNombre(nombreForm.toUpperCase());
			cambio = true;
		}
		
		
		
		if(StringUtils.hasText(descripcionForm) && !monedaBD.esMiDescripcion(descripcionForm)) {
			monedaBD.setDescripcionCompleta(descripcionForm.toUpperCase());
			cambio = true;
		}
		
		if(estadoForm != null && !monedaBD.esMiEstado(estadoForm)) {
			monedaBD.setEstado(estadoForm);
			cambio = true;
		}
		
		


		if (cambio) {
			
			Auditoria auditoria = monedaBD.getAuditoria();
			Auditoria.modificar(auditoria, usuario);
			monedaBD.setAuditoria(auditoria);
			monedaRepository.save(monedaBD);


			resp.setLog(String.format("Se ha modificado la moneda %s ", monedaBD.getNombre()));
		} else {

			resp.setLog(String.format("No se han enviado modificaciones para la moneda %s", monedaBD.getNombre()));
		}
		
		resp.setOk(cambio);
		
		return resp;
	}

	private boolean validarSiYaExiste(String nombreForm) {
		return monedaRepository.existsByNombre(nombreForm.trim());
	}


	@Override
	public ResponseDTO<MonedaDTO> crearMoneda(MonedaDTO dto, Usuario usuario) {
		ResponseDTO<MonedaDTO> resp = new ResponseDTO<MonedaDTO>();
		try {
			validarMoneda(dto);
			
			Moneda moneda = Moneda.builder()
							 .auditoria(Auditoria.crearAuditoria(usuario))
							 .descripcionCompleta(dto.getMonedaDescripcion().toUpperCase())
							 .estado(dto.getEstado())
							 .nombre(dto.getMonedaName().toUpperCase())
							 .build();
			
			Moneda monedaBD = monedaRepository.save(moneda);

	        log.info("Se ha creado la moneda {}", monedaBD.getNombre());
	        resp.setOk(true);
	        resp.setLog("Se creo la moneda correctamente.");
			
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new BaseException(e.getMessage());
		}
			
		return resp;
	}

	private void validarMoneda(MonedaDTO dto) {
		if (!StringUtils.hasText(dto.getMonedaName())) {
			throw new BaseException("Debe indicar el nombre de la moneda");
		}
		
		if (monedaRepository.existsByNombre(dto.getMonedaName().trim())) {
			throw new BaseException("Ya existe una Moneda con ese nombre");
		}
		
		if (!StringUtils.hasText(dto.getMonedaDescripcion())) {
			throw new BaseException("Debe indicar la descripcion de la moneda");
		}
		
		if (dto.getEstado() == null) {
			throw new BaseException("Debe indicar el estado de la moneda");
		}
		
		
	}

}
