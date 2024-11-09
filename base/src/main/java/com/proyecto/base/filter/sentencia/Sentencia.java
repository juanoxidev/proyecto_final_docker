package com.proyecto.base.filter.sentencia;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;

public class Sentencia {
	private String sql;
	private Map<String,Object> parametros = new HashMap<String,Object>(0);
	
	public Sentencia(String sql, Map<String, Object> parametros) {
		this.sql = sql;
		this.parametros = parametros;
	}
	
	public Sentencia(){
		
	}

	public String getSql() {
		return sql;
	}

	public void setSql(String sql) {
		this.sql = sql;
	}

	public Map<String, Object> getParametros() {
		return parametros;
	}

	public void setParametros(Map<String, Object> parametros) {
		this.parametros = parametros;
	}
	
	public void append(String sql){
		StringBuffer aux = new StringBuffer(getSql());
		if(!StringUtils.isEmpty(sql)){
			aux.append(sql);
			setSql(aux.toString());
		}
	}
}
