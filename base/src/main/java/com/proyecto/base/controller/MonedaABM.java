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
import com.proyecto.base.dto.MonedaDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.TickerDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.service.MonedaService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/abmmoneda")
public class MonedaABM extends DefaultController {
	
	@Autowired 
	private MonedaService monedaService;
	
	
	@GetMapping("/bandeja")
	public String bandejaConciliacion(Model model) {
		
		model.addAttribute("estadosSeleccionables", EstadosEnum.SELECCIONABLES);
		model.addAttribute("estados", EstadosEnum.TODOS);
		model.addAttribute("busquedaForm", new MonedaDTO());
		model.addAttribute("formCreacionEditar", new MonedaDTO());
		return "views/abm/monedas";
		
	}

	@PostMapping(value = "/getMonedas")
	public @ResponseBody DataTablesResponse<MonedaDTO> getPlantillas(
			@RequestBody DataTablesRequestForm<MonedaDTO> dtRequest) {
 

		DataTablesResponse<MonedaDTO> dtResponse = new DataTablesResponse<>();


		dtResponse = monedaService.getRespuestaDatatable(dtRequest);

		
		return dtResponse;
	}
	
	
	@PostMapping(value= "/editarMoneda")
	public @ResponseBody ResponseDTO<MonedaDTO> editarMoneda( @ModelAttribute MonedaDTO formCreacion){
		ResponseDTO<MonedaDTO> resp = new ResponseDTO<MonedaDTO>();
		
				try {
					resp = monedaService.editarMoneda(formCreacion, getUsuario());
					// Buscar como obtener informacion de la plantilla desde el DTO para loggear
					// log.info(resp.getLog() + " - Plantilla " + getPlantilla().getAlyc());	
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
	
	
	
	@PostMapping(value= "/altaMoneda")
	public @ResponseBody ResponseDTO<MonedaDTO> crearMoneda( @ModelAttribute MonedaDTO formCreacion){
		ResponseDTO<MonedaDTO> resp = new ResponseDTO<MonedaDTO>();
		
				try {
					resp = monedaService.crearMoneda(formCreacion, getUsuario());
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
}
