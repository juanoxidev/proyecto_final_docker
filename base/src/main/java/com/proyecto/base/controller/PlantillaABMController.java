package com.proyecto.base.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.model.Plantilla;
import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.PlantillaDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.repository.PlantillaRepository;
import com.proyecto.base.service.PlantillaService;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/abmplantillas")
public class PlantillaABMController extends DefaultController{
	
	@Autowired
	private PlantillaService plantillaService;
	
	
	/**
	 * Envia los datos necesarios al front end.
	 * @param dtRequest
	 * @return retorna la vista principal al clickear sobre usuarios.
	 */
	@GetMapping("/bandeja")
	public String bandejaPantillas(Model model, HttpServletRequest request) {
		


		
		AlycDTO busquedaForm = new AlycDTO();
		PlantillaDTO creacionEdicionPlantilla = new PlantillaDTO();

		model.addAttribute("busquedaForm", busquedaForm);
		model.addAttribute("formCreacionEditar", creacionEdicionPlantilla);

		return "views/abm/plantillas.html";
	}
	
	
	
	/**
	 * Filtrado de busqueda en la vista
	 * @param dtRequest
	 * @return lista de usuarios a mostrar en view
	 */
	@PostMapping(value = "/getAlycs")
	public @ResponseBody DataTablesResponse<AlycDTO> getPlantillas(
			@RequestBody DataTablesRequestForm<AlycDTO> dtRequest) {
 

		DataTablesResponse<AlycDTO> dtResponse = new DataTablesResponse<>();


		dtResponse = plantillaService.getRespuestaDatatable(dtRequest);

		
		return dtResponse;
	}
	
	
	
	/**
	 * Recibe los datos del usuario modal y lo guarda en la bd. 
	 * @param dtRequest
	 * 
	 * 
	 * 
	 * 
	 * @return una respuesta en un alert.
	 */
	
	@PostMapping(value= "/altaPlantilla")
	public @ResponseBody ResponseDTO<PlantillaDTO> altaPlantilla( @ModelAttribute PlantillaDTO formCreacion){
		ResponseDTO<PlantillaDTO> resp = new ResponseDTO<PlantillaDTO>();
		
				try {
					resp = plantillaService.altaPlantilla(formCreacion, getUsuario());
					// Buscar como obtener informacion de la plantilla desde el DTO para loggear
					// log.info(resp.getLog() + " - Plantilla " + getPlantilla().getAlyc());	
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}

	
	@PostMapping(value= "/editarPlantilla")
	public @ResponseBody ResponseDTO<PlantillaDTO> editarPlantilla( @ModelAttribute PlantillaDTO formCreacion){
		ResponseDTO<PlantillaDTO> resp = new ResponseDTO<PlantillaDTO>();
		
				try {
					resp = plantillaService.editarPlantilla(formCreacion, getUsuario());
					// Buscar como obtener informacion de la plantilla desde el DTO para loggear
					// log.info(resp.getLog() + " - Plantilla " + getPlantilla().getAlyc());	
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
}
