package com.proyecto.base.converter;


import javax.persistence.AttributeConverter;

import com.proyecto.base.enums.EstadosEnum;


public class EstadoConverter implements AttributeConverter<EstadosEnum, String>{

	@Override
	public String convertToDatabaseColumn(EstadosEnum valor) {
		String resp = null;
		if(valor != null){
			resp = valor.getCodigo();
		}else{
			throw new IllegalArgumentException("Estado desconocido" + valor);
		}
        return resp;
	}

	@Override
	public EstadosEnum convertToEntityAttribute(String codigoDb) {
		EstadosEnum resp = null;
		if(codigoDb != null){
			resp = EstadosEnum.forName(codigoDb);
		}else{
			throw new IllegalArgumentException("Estado desconocido" + codigoDb);
		}
		return resp;
	}

}
