package com.proyecto.base.converter;

import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.model.Alyc;
import com.proyecto.base.model.Auditoria;
import com.proyecto.base.model.Moneda;
import com.proyecto.base.model.Operacion;
import com.proyecto.base.model.Ticker;

public class OperacionConverter {

	public static Operacion dtoToClass(OperacionDTO dto,Moneda moneda,Alyc alyc,Ticker ticker,Auditoria auditoria) {
	      return Operacion.builder()
					.id(dto.getId())
					.cantidadNominal(dto.getCantidadNominal())
					.moneda(moneda)
					.precioMesa(dto.getPrecioMesa())
					.precioCliente(dto.getPrecioCliente())
					.alyc(alyc)
					.ticker(ticker)
					.auditoria(auditoria)
					.build();
		}
	
	/*public static OperacionDTO classToDTO(Operacion operacion) {
       return OperacionDTO
	}*/

}
