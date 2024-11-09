package com.proyecto.base.controller;

import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proyecto.base.converter.OperacionConverter;
import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.dto.PlantillaDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.TickerDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.model.Alyc;
import com.proyecto.base.model.Auditoria;
import com.proyecto.base.model.Moneda;
import com.proyecto.base.model.Operacion;
import com.proyecto.base.model.Ticker;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.service.TickerService;

import javassist.expr.NewArray;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/abmticker")
public class TickerABMController extends DefaultController {

	@Autowired
	private TickerService tickerService;
	
	
	
	@GetMapping("/bandeja")
	public String bandejaTicker(Model model) {
		
		model.addAttribute("estadosSeleccionables", EstadosEnum.SELECCIONABLES);
		model.addAttribute("estados", EstadosEnum.TODOS);
		model.addAttribute("busquedaForm", new TickerDTO());
		model.addAttribute("formCreacionEditar", new TickerDTO());
		return "views/abm/tickers";
		
	}
	
	
	@PostMapping(value = "/getTickers")
	public @ResponseBody DataTablesResponse<TickerDTO> getPlantillas(
			@RequestBody DataTablesRequestForm<TickerDTO> dtRequest) {
 

		DataTablesResponse<TickerDTO> dtResponse = new DataTablesResponse<>();


		dtResponse = tickerService.getRespuestaDatatable(dtRequest);

		
		return dtResponse;
	}
	
	
	@PostMapping(value= "/altaTicker")
	public @ResponseBody ResponseDTO<TickerDTO> altaTicker( @ModelAttribute TickerDTO formCreacion){
		ResponseDTO<TickerDTO> resp = new ResponseDTO<TickerDTO>();
		
				try {
					resp = tickerService.altaTicker(formCreacion, getUsuario());
					// Buscar como obtener informacion de la plantilla desde el DTO para loggear
					// log.info(resp.getLog() + " - Plantilla " + getPlantilla().getAlyc());	
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
	
	
	
	@PostMapping(value= "/editarTicker")
	public @ResponseBody ResponseDTO<TickerDTO> editarTicker( @ModelAttribute TickerDTO formCreacion){
		ResponseDTO<TickerDTO> resp = new ResponseDTO<TickerDTO>();
		
				try {
					resp = tickerService.editarTicker(formCreacion, getUsuario());
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
	
	

}
