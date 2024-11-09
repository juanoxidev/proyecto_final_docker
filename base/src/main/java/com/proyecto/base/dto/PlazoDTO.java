package com.proyecto.base.dto;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.model.Moneda;
import com.proyecto.base.model.Plazo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlazoDTO {
	
	private String plazoName;
	private String plazoDescripcion;
	private Long plazoId;
	private EstadosEnum estado;
	
	
	public static PlazoDTO crearDTOBusqueda(Plazo plazo) {
		PlazoDTO dto = PlazoDTO.builder()
						.plazoId(plazo.getId())
						.plazoName(plazo.getNombre())
						.estado(plazo.getEstado())
						.plazoDescripcion(plazo.getDescripcionCompleta())
						.build();
		return dto;
		
	}

}
