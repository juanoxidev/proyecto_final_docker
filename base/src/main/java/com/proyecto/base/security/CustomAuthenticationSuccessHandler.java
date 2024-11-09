package com.proyecto.base.security;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.UsuarioRepository;

import lombok.extern.slf4j.Slf4j;


public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	private ServletContext servletContext;

	
	

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
    	 Usuario usuario = (Usuario) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    	
    	 if (usuario.getPasswordACambiar()) {
    		 response.sendRedirect(servletContext.getContextPath()+"/cambiar-password");
    	 } else { 
    	
    		 response.sendRedirect(servletContext.getContextPath()+"/home");
    	 }
    }
    



}
