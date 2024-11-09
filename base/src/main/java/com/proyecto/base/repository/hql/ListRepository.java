package com.proyecto.base.repository.hql;

import java.util.List;
import java.util.Map;

import com.proyecto.base.filter.sentencia.Sentencia;



public interface ListRepository<T> {
	List<T> list(String sql, Map<String, Object> map,  Integer primerResultado, Integer cantidadAPedir);
	
	public Long count(String sql,Map<String,Object> map);
	
	List<T> list(Sentencia sentencia,  Integer primerResultado, Integer cantidadAPedir);
	
	<U> List<U> listDto(Sentencia sentenciaDto,  Integer primerResultado, Integer cantidadAPedir);
	
	public Long count(Sentencia sentencia);
}
