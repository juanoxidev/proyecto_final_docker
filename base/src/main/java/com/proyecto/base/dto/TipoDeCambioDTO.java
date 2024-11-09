package com.proyecto.base.dto;

import javax.mail.Multipart;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.model.TipoDeCambio;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TipoDeCambioDTO {
	private String moneda;
	private Double cotizacion;
	
	public static TipoDeCambioDTO crearDTO(TipoDeCambio tc) {
		
		TipoDeCambioDTO dto = TipoDeCambioDTO.builder()
							  .moneda(tc.getMoneda().getNombre())
							  .cotizacion(tc.getCotizacion())
							  .build();
		
		return dto;
		
	}
}
