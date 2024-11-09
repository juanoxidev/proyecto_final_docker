package com.proyecto.base.converter;

import javax.persistence.AttributeConverter;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.enums.TipoOperacion;

public class TipoOperacionConverter implements AttributeConverter<TipoOperacion, String> {

	@Override
	public String convertToDatabaseColumn(TipoOperacion attribute) {
		String resp = null;
		if(attribute != null){
			resp = attribute.getCodigo();
		}else{
			throw new IllegalArgumentException("Estado desconocido" + attribute);
		}
        return resp;
	}

	@Override
	public TipoOperacion convertToEntityAttribute(String dbData) {
		TipoOperacion resp = null;
		if(dbData != null){
			resp = TipoOperacion.find(dbData);
		}else{
			throw new IllegalArgumentException("TipoOperacion desconocido" + dbData);
		}
		return resp;
	}

}
