package com.proyecto.base.dto;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.model.Moneda;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class MonedaDTO {
	private String monedaName;
	private String monedaDescripcion;
	private Long monedaId;
	private EstadosEnum estado;
	
	
	public static MonedaDTO crearDTOBusqueda(Moneda moneda) {
		MonedaDTO dto = MonedaDTO.builder()
						.monedaId(moneda.getId())
						.monedaName(moneda.getNombre())
						.monedaDescripcion(moneda.getDescripcionCompleta())
						.estado(moneda.getEstado())
						.build();
		return dto;
		
	}
}
