package com.proyecto.base.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.enums.TipoOperacion;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.AlycRepository;
import com.proyecto.base.repository.MonedaRepository;
import com.proyecto.base.repository.PlazoRepository;
import com.proyecto.base.repository.TickerRepository;
import com.proyecto.base.service.OperacionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/operaciones")
public class OperacionesController extends DefaultController {
	
	@Autowired
	private MonedaRepository monedaRepository;
	
	@Autowired
	private TickerRepository tickerRepository;
	
	
	@Autowired
	private AlycRepository alycRepository;
	
	@Autowired
	private PlazoRepository plazoRepository;
	
	@Autowired
	private OperacionService operacionService;
	
	@GetMapping("/bandeja")
	public String bandejaOperaciones(Model model, HttpServletRequest request) {
		
		OperacionDTO operacionForm = new OperacionDTO();
		model.addAttribute("tipoOperacionSeleccionables", TipoOperacion.SELECCIONABLES);
		model.addAttribute("tipoOperacion", TipoOperacion.ALL);
		model.addAttribute("estadosSeleccionables", EstadosEnum.SELECCIONABLES);
		model.addAttribute("estados", EstadosEnum.ALL);
		model.addAttribute("tickers", tickerRepository.findAll());
		model.addAttribute("monedas", monedaRepository.findAll());
		model.addAttribute("alycs", alycRepository.findAll());
		model.addAttribute("plazos", plazoRepository.findAll());
		model.addAttribute("busquedaForm", operacionForm);
		model.addAttribute("formCreacionEditar", operacionForm);
		model.addAttribute("exportarExcel", operacionForm);
		return "views/abm/operacion.html";
	}
	
	
	
	/**
	 * Filtrado de busqueda en la vista
	 * @param dtRequest
	 * @return lista de usuarios a mostrar en view
	 */
	
	// SEGURO QUE ESTO NO ES UN GET? PQ SE DEJO COMO POST?
	@PostMapping(value = "/getOperaciones")
	public @ResponseBody DataTablesResponse<OperacionDTO> getOperaciones(
			@RequestBody DataTablesRequestForm<OperacionDTO> dtRequest) {
 

		DataTablesResponse<OperacionDTO> dtResponse = new DataTablesResponse<>();


		dtResponse = operacionService.getRespuestaDatatable(dtRequest);

		
		return dtResponse;
	}
	
	
	
	/**
	 * Recibe los datos del usuario modal y lo guarda en la bd. 
	 * @param dtRequest
	 * @return una respuesta en un alert.
	 */
	
	@PostMapping(value= "/altaOperacion")
	public @ResponseBody ResponseDTO<OperacionDTO> altaOperacion( @ModelAttribute OperacionDTO formCreacion){
		ResponseDTO<OperacionDTO> resp = new ResponseDTO<OperacionDTO>();
		
				try {
					resp = operacionService.altaOperacion(formCreacion, getUsuario());
					 log.info(resp.getLog() + " - Operacion " + getUsuario());	
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
	
	 // Controlador para manejar la edición de la operación
    @PostMapping("/editarOperacion")
    public  @ResponseBody ResponseDTO<OperacionDTO> editarOperacion(@ModelAttribute OperacionDTO operacionDTO, 
                                  Usuario usuario) {
 
        ResponseDTO<OperacionDTO> resp = new ResponseDTO<OperacionDTO>();

    	try {
            resp = operacionService.editarOperacion(operacionDTO, usuario);
    	}catch (Exception e) {
    		e.printStackTrace();
			 resp.setLog(e.getMessage());
			 resp.setOk(false);
			  	        
    	}
    	
    	return resp;
   
    }
    
    
    
    @PostMapping(value="/getOperacionesAlyc",  consumes = "application/json")
    public ResponseEntity<byte[]> compararReportes(@RequestBody OperacionDTO operacionDTO) {
    		
        try {

            byte[] excelResultado = operacionService.getOperacionesSegunAlycEntreFechas(operacionDTO);

     
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
          
            headers.setContentDisposition(ContentDisposition.builder("attachment").filename("operaciones_alyc").build());


            return new ResponseEntity<>(excelResultado, headers, HttpStatus.OK);

        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

	
	

}
