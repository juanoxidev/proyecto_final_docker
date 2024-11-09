package com.proyecto.base.datatable;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

public interface IDatatable<T> {

	
	   default <D> DataTablesResponse<D> getRespuestaDatatable(
				List<D> contenido, Long records) {
		   
					 DataTablesResponse<D> respuesta = new DataTablesResponse<D>();
						
						respuesta.setContent(contenido);
						respuesta.setRecordsFiltered(records.intValue());
						respuesta.setRecordsTotal(records.intValue());
						
						return respuesta;
	    }
	   
	   
	   
	    default <D> List<D> getDTOs( Function<T, D> mapper, List<T> entities) {
	        List<D> dtos = new ArrayList<>();
	        for (T entity : entities) {
	            dtos.add(mapper.apply(entity));
	        }
	        return dtos;
	    }
	   
}
