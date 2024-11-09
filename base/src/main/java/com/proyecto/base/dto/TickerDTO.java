package com.proyecto.base.dto;

import javax.persistence.Column;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.model.Ticker;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TickerDTO {

	private String tickerName;
	private Long tickerId;
	private EstadosEnum estado;
    private String tickerDescripcion;
	
    public static TickerDTO crearDTOBusqueda(Ticker ticker) {
	 TickerDTO dto = TickerDTO.builder()
			 	     .tickerId(ticker.getId())
			 	     .tickerName(ticker.getNombre())
			 	     .tickerDescripcion(ticker.getDescripcion())
			 	     .estado(ticker.getEstado())
			 	     .build();
	 return dto;
		
	}
}
