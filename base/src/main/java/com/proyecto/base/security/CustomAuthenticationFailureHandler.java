package com.proyecto.base.security;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class CustomAuthenticationFailureHandler implements AuthenticationFailureHandler {

	@Autowired
	private ServletContext servletContext;
	@Autowired
	private UsuarioRepository usuarioRepository;
	
	private final int MAX_INTENTOS=3;
	
	  @Override
	    public void onAuthenticationFailure(HttpServletRequest request,
	                                        HttpServletResponse response,
	                                        AuthenticationException exception) throws IOException, ServletException {
		  
		  request.getSession().setAttribute("errorLogin", exception.getMessage());
	   
	      response.sendRedirect(servletContext.getContextPath()+"/login");
	    }
	  
	  

}
