package com.proyecto.base.util;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import com.proyecto.base.model.Usuario;

public class UtilesSecurity {
	
	public  static Usuario getUsuarioLogueado() {
    	Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        return  (Usuario) auth.getPrincipal(); 
		
	}

}
