package com.proyecto.base.service;

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import com.proyecto.base.config.PasswordGenerator;
import com.proyecto.base.constantes.WConstant;
import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.datatable.IDatatable;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.IComplementoAlyc;
import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.dto.PlantillaDTO;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.filter.IFilter;
import com.proyecto.base.filter.PlantillaFilter;
import com.proyecto.base.filter.UsuarioFilter;
import com.proyecto.base.model.Rol;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.model.Alyc;
import com.proyecto.base.model.Auditoria;
import com.proyecto.base.model.Plantilla;
import com.proyecto.base.repository.AlycRepository;
import com.proyecto.base.repository.PlantillaRepository;
import com.proyecto.base.repository.RolRepository;
import com.proyecto.base.repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service

public class PlantillaServiceImpl implements PlantillaService, IDatatable<Alyc>{
	
	@Autowired
	private PlantillaRepository plantillaRepository;
		
	@Autowired
	private AlycRepository alycRepository;
	
	
	@Transactional
	public ResponseDTO<PlantillaDTO> altaPlantilla (PlantillaDTO dto, Usuario usuario){
		ResponseDTO<PlantillaDTO> resp = new ResponseDTO<PlantillaDTO>();
		
		
		try {	
			validarPlantilla(dto);
		} catch (Exception e ) {
				log.info(e.getMessage());
				throw new BaseException(e.getMessage());
		}
		
		Alyc alyc = alycRepository.getReferenceById(dto.getAlyc());
		
		Auditoria auditoria = Auditoria.crearAuditoria(usuario);
						
		Plantilla plantilla = Plantilla.builder()
									  .alyc(alyc)
									  .especie(dto.getEspecie().toLowerCase())
									  .fechaOperacion(dto.getFechaOperacion().toLowerCase())
									  .cantidadValorNominal(dto.getCantidadValorNominal().toLowerCase())
									  .precioMesa(dto.getPrecioMesa().toLowerCase())
									  .precioCliente(dto.getPrecioCliente().toLowerCase())
									  .auditoria(auditoria)
									  .build();

        Plantilla plantillaBD = plantillaRepository.save(plantilla);
       
        alyc.setPlantilla(plantillaBD);
        alycRepository.save(alyc);

        log.info("Se ha creado la plantilla #{}-{}",plantillaBD.getId(), plantillaBD.getAlyc().getNombre());
       
        resp.setOk(true);
        resp.setLog("Se creo la plantilla correctamente.");
		return resp;
	}
	
	private void validarPlantilla(PlantillaDTO dto) {
		int count = 0;
		StringBuffer mensaje = new StringBuffer();
		mensaje.append("Debe indicar como se individualiza la columna: ");
	      
			if (!StringUtils.hasText(dto.getEspecie())) {
	    	   mensaje.append(WConstant.ESPECIE+" ,");
	    	   count++;
	        }
	       
	       if (!StringUtils.hasText(dto.getFechaOperacion())) {
	    	   mensaje.append(WConstant.FECHA_OPERACION+" ,");
	    	   count++;
	        }
	       
	       if (!StringUtils.hasText(dto.getCantidadValorNominal())) {
	    	   mensaje.append(WConstant.CANTIDAD_V_N+" ,");
	    	   count++;
	        }
	       
	       if (!StringUtils.hasText(dto.getPrecioCliente())) {
	    	   mensaje.append(WConstant.PRECIO_CLIENTE+" ,");
	    	   count++;
	        }
	       
	       if (!StringUtils.hasText(dto.getPrecioMesa())) {
	    	   mensaje.append(WConstant.PRECIO_MESA+" ,");
	    	   count++;
	        }
	       
	       
	       
	       if (count > 0) {
	    	   mensaje.setCharAt(mensaje.length() - 1, '.');
	           throw new IllegalArgumentException(mensaje.toString());
	       }
	}


	@Override

	public DataTablesResponse<AlycDTO> getRespuestaDatatable(DataTablesRequestForm<AlycDTO> dtRequest) {
		
		int start = dtRequest.getStart();
		int limit = dtRequest.getLength();

		AlycDTO criteriosDeBusqueda = dtRequest.getFormBusqueda();
		IFilter filter = new PlantillaFilter(criteriosDeBusqueda);
		List<Alyc> alycsBD = this.alycRepository.list(filter.sentenciaHQL(), start, limit);
		List<AlycDTO> alycsDTO = this.getDTOs(AlycDTO::crearDTOPlantilla, alycsBD);
		Long cantidadRegistros = this.plantillaRepository.count(filter.sentenciaHQLCount());

		return this.getRespuestaDatatable(alycsDTO, cantidadRegistros);
	}
	
	@Override
	@Transactional
	public ResponseDTO<PlantillaDTO> editarPlantilla(PlantillaDTO formCreacion, Usuario usuario) {
		ResponseDTO<PlantillaDTO> resp = new ResponseDTO<PlantillaDTO>();

		String especieForm = formCreacion.getEspecie();
		Long alycId = formCreacion.getAlyc();
		Alyc alyc = alycRepository.getReferenceById(alycId);

	    String fechaOperacionForm = formCreacion.getFechaOperacion(); 
	    String cantidadValorNominalForm = formCreacion.getCantidadValorNominal();
	    String precioMesaForm = formCreacion.getPrecioMesa();    

	    String precioClienteForm = formCreacion.getPrecioCliente();
		
		boolean cambio = false;
		
		Plantilla plantillaBD = plantillaRepository.getReferenceById(formCreacion.getId());
		
		if(StringUtils.hasText(especieForm) && !plantillaBD.esMiEspecie(especieForm)) {
			plantillaBD.setEspecie(especieForm.toLowerCase());
			cambio = true;
		}
		
		if(StringUtils.hasText(fechaOperacionForm) && !plantillaBD.esMiFechaOperacion(fechaOperacionForm)) {

			plantillaBD.setFechaOperacion(fechaOperacionForm);
			cambio = true;
		}
		
		if(StringUtils.hasText(cantidadValorNominalForm) && !plantillaBD.esMiCantidadValorNominal(cantidadValorNominalForm)) {

			plantillaBD.setCantidadValorNominal(cantidadValorNominalForm);
			cambio = true;
		}
		
		
		if(StringUtils.hasText(precioMesaForm) && !plantillaBD.esMiPrecioMesa(precioMesaForm)) {

			plantillaBD.setPrecioMesa(precioMesaForm);
			cambio = true;
		}
		
		if(StringUtils.hasText(precioClienteForm) && !plantillaBD.esMiPrecioClienteForm(precioClienteForm)) {

			plantillaBD.setPrecioCliente(precioClienteForm);
			cambio = true;
		}
		


		if (cambio) {
			
			Auditoria auditoriaPlantilla = plantillaBD.getAuditoria();
			Auditoria.modificar(auditoriaPlantilla, usuario);
			plantillaBD.setAuditoria(auditoriaPlantilla);
			plantillaRepository.save(plantillaBD);

			
			
			
			alyc.setPlantilla(plantillaBD);
			//TODO: DESCOMENTAR CUANDO SE HAGA EL ALTA DE ALYC QUE DEBE TENER AUDITORIA.
//			Auditoria auditoriaAlyc = alyc.getAuditoria();
//			Auditoria.modificar(auditoriaAlyc, usuario);
//			alyc.setAuditoria(auditoriaAlyc);
			alycRepository.save(alyc);
			
			
			resp.setLog(String.format("Se ha modificado la plantilla de %s ", alyc.getNombre()));
		} else {

			resp.setLog(String.format("No se han enviado modificaciones para la plantilla de %s", alyc.getNombre()));
		}
		
		resp.setOk(cambio);
		
		return resp;
	}


}
