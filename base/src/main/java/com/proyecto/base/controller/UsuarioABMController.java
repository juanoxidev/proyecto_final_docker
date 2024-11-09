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

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.model.Rol;
import com.proyecto.base.repository.RolRepository;
import com.proyecto.base.service.UsuarioService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/abmusuarios")
public class UsuarioABMController extends DefaultController{
	
	@Autowired
	private UsuarioService usuarioService;
	
	@Autowired
	private RolRepository rolRepository;
	
	
	/**
	 * Envia los datos necesarios al front end.
	 * @param dtRequest
	 * @return retorna la vista principal al clickear sobre usuarios.
	 */
	@GetMapping("/bandeja")
	public String bandejaUsuarios(Model model, HttpServletRequest request) {
		
		EstadosEnum [] estados = EstadosEnum.ALL;
		EstadosEnum [] estadosSeleccionables = EstadosEnum.SELECCIONABLES;
		List<Rol> roles = rolRepository.findAll();
		
		UsuarioDTO usuarioForm = new UsuarioDTO();
		model.addAttribute("roles", roles);
		model.addAttribute("estadosFiltro", estados);
		model.addAttribute("busquedaForm", usuarioForm);
		model.addAttribute("formCreacionEditar", usuarioForm);
		model.addAttribute("estadosSeleccionables", estadosSeleccionables);
		return "views/abm/usuarios.html";
	}
	
	
	
	/**
	 * Filtrado de busqueda en la vista
	 * @param dtRequest
	 * @return lista de usuarios a mostrar en view
	 */
	@PostMapping(value = "/getUsuarios")
	public @ResponseBody DataTablesResponse<UsuarioDTO> getUsuarios(
			@RequestBody DataTablesRequestForm<UsuarioDTO> dtRequest) {
 

		DataTablesResponse<UsuarioDTO> dtResponse = new DataTablesResponse<>();


		dtResponse = usuarioService.getRespuestaDatatable(dtRequest);

		
		return dtResponse;
	}
	
	
	
	/**
	 * Recibe los datos del usuario modal y lo guarda en la bd. 
	 * @param dtRequest
	 * @return una respuesta en un alert.
	 */
	
	@PostMapping(value= "/altaUsuario")
	public @ResponseBody ResponseDTO<UsuarioDTO> altaUsuario( @ModelAttribute UsuarioDTO formCreacion){
		ResponseDTO<UsuarioDTO> resp = new ResponseDTO<UsuarioDTO>();
		
				try {
					resp = usuarioService.altaUsuario(formCreacion,getUsuario());
					 log.info(resp.getLog() + " - USUARIO " + getUsuario().getUsername());	
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
	
	@PostMapping(value= "/editarUsuario")
	public @ResponseBody ResponseDTO<UsuarioDTO> editarUsuario( @ModelAttribute UsuarioDTO formCreacion){
		ResponseDTO<UsuarioDTO> resp = new ResponseDTO<UsuarioDTO>();
		
				try {
					resp = usuarioService.editarUsuario(formCreacion,getUsuario());
					 log.info(resp.getLog() + " - USUARIO " + getUsuario().getUsername());	
				} catch (BaseException e) {
					e.printStackTrace();
					 resp.setLog(e.getMessage());
					 resp.setOk(false);
				}
				return resp;
	}
	
	
}
