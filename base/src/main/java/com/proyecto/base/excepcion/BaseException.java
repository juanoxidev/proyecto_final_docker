package com.proyecto.base.excepcion;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BaseException  extends RuntimeException{

	/**
	 * 
	 */
	private static final long serialVersionUID = -1437254077180657185L;
	
	
	private String message;

}