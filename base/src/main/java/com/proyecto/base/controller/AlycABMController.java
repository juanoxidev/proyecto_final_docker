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
import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.TickerDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.enums.TipoOperacion;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.repository.MonedaRepository;
import com.proyecto.base.repository.PlazoRepository;
import com.proyecto.base.service.AlycService;
import com.proyecto.base.service.TickerService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/abmalyc")
public class AlycABMController extends DefaultController{

	
	@Autowired
	private AlycService alycService;
	@Autowired
	private MonedaRepository monedaRepository;

	
	
	
	@GetMapping("/bandeja")
	public String bandejaAlyc(Model model) {
		
		model.addAttribute("estadosSeleccionables", EstadosEnum.SELECCIONABLES);
		model.addAttribute("estados", EstadosEnum.ALL);
		model.addAttribute("monedas", monedaRepository.findAll());
		model.addAttribute("busquedaForm", new AlycDTO());
		model.addAttribute("formCreacionEditar", new AlycDTO());
		return "views/abm/alyc";
		
	}
	
	
	@PostMapping(value = "/getAlycs")
	public @ResponseBody DataTablesResponse<AlycDTO> getPlantillas(
			@RequestBody DataTablesRequestForm<AlycDTO> dtRequest) {
 

		DataTablesResponse<AlycDTO> dtResponse = new DataTablesResponse<>();


		dtResponse = alycService.getRespuestaDatatable(dtRequest);

		
		return dtResponse;
	}
	
	
	@PostMapping(value= "/altaAlyc")
	public @ResponseBody ResponseDTO<AlycDTO> altaAlyc( @ModelAttribute AlycDTO formCreacion){
		ResponseDTO<AlycDTO> resp = new ResponseDTO<AlycDTO>();
		
				try {
					resp = alycService.altaAlyc(formCreacion, getUsuario());
					// Buscar como obtener informacion de la plantilla desde el DTO para loggear
					// log.info(resp.getLog() + " - Plantilla " + getPlantilla().getAlyc());	
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
	
	
	
	@PostMapping(value= "/editarAlyc")
	public @ResponseBody ResponseDTO<AlycDTO> editarAlyc( @ModelAttribute AlycDTO formCreacion){
		ResponseDTO<AlycDTO> resp = new ResponseDTO<AlycDTO>();
		
				try {
					resp = alycService.editarAlyc(formCreacion, getUsuario());
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
	
	
}
