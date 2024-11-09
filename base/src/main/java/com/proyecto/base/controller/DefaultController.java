package com.proyecto.base.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;

import com.proyecto.base.model.Usuario;

@Controller
public abstract class  DefaultController {
	
	public Usuario getUsuario() {
		return (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}
	
	@Value("${server.servlet.context-path}")
	private String contextPath;
	
	public String getContextPath() {
		return this.contextPath;
	}

}
