package com.proyecto.base.filter;

import com.proyecto.base.filter.sentencia.Sentencia;

public interface IFilter {
	
	public Sentencia sentenciaHQL();
	
	public Sentencia sentenciaHQLCount();

	

}
