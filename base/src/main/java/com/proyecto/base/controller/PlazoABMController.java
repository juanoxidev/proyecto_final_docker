package com.proyecto.base.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.PlazoDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.service.PlazoService;

import lombok.extern.slf4j.Slf4j;
@Slf4j
@Controller
@RequestMapping("/abmplazo")
public class PlazoABMController extends DefaultController {
	@Autowired 
	private PlazoService plazoService;
	
	
	@GetMapping("/bandeja")
	public String bandejaConciliacion(Model model) {
		
		model.addAttribute("estadosSeleccionables", EstadosEnum.SELECCIONABLES);
		model.addAttribute("estados", EstadosEnum.TODOS);
		model.addAttribute("busquedaForm", new PlazoDTO());
		model.addAttribute("formCreacionEditar", new PlazoDTO());
		return "views/abm/plazos";
		
	}

	@PostMapping(value = "/getPlazos")
	public @ResponseBody DataTablesResponse<PlazoDTO> getPlazos(
			@RequestBody DataTablesRequestForm<PlazoDTO> dtRequest) {
 

		DataTablesResponse<PlazoDTO> dtResponse = new DataTablesResponse<>();


		dtResponse = plazoService.getRespuestaDatatable(dtRequest);

		
		return dtResponse;
	}
	
	
	@PostMapping(value= "/editarPlazo")
	public @ResponseBody ResponseDTO<PlazoDTO> editarMoneda( @ModelAttribute PlazoDTO formCreacion){
		ResponseDTO<PlazoDTO> resp = new ResponseDTO<PlazoDTO>();
		
				try {
					resp = plazoService.editarPlazo(formCreacion, getUsuario());
					// Buscar como obtener informacion de la plantilla desde el DTO para loggear
					// log.info(resp.getLog() + " - Plantilla " + getPlantilla().getAlyc());	
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
	
	
	
	@PostMapping(value= "/crearPlazo")
	public @ResponseBody ResponseDTO<PlazoDTO> crearPlazo( @ModelAttribute PlazoDTO formCreacion){
		ResponseDTO<PlazoDTO> resp = new ResponseDTO<PlazoDTO>();
		
				try {
					resp = plazoService.crearPlazo(formCreacion, getUsuario());
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
}
