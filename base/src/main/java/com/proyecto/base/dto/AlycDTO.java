package com.proyecto.base.dto;

import java.util.List;
import java.util.stream.Collectors;

import javax.mail.Multipart;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.model.Alyc;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AlycDTO {

	private Long idAlyc;
	private String nombre;
	private boolean tienePlantilla;
	private EstadosEnum estado;
	private Multipart excel;
	private PlantillaDTO plantilla;
	private Double comision;
	private List<TipoDeCambioDTO> cotizaciones;
	
	public static AlycDTO crearDTOConciliacion(Alyc alyc) {
		AlycDTO dto = AlycDTO.builder()
					  .idAlyc(alyc.getId())
					  .nombre(alyc.getNombre())
					  .estado(alyc.getEstado())
					  .build();
		return dto;
		
	}
	
	public static AlycDTO crearDTOPlantilla(Alyc alyc) {
		AlycDTO dto = AlycDTO.builder()
					  .idAlyc(alyc.getId())
					  .nombre(alyc.getNombre())
					  .estado(alyc.getEstado())
					  .tienePlantilla(alyc.getPlantilla()!= null? true: false)
					  .plantilla(alyc.getPlantilla() != null? PlantillaDTO.crearDTOBusqueda(alyc.getPlantilla()): null)
					  .build();
		return dto;
		
	}
	
	
	public static AlycDTO crearDTOAlyc(Alyc alyc) {
		AlycDTO dto = AlycDTO.builder()
					  .idAlyc(alyc.getId())
					  .nombre(alyc.getNombre())
					  .estado(alyc.getEstado())
					  .comision(alyc.getPorcentajeComision())
					  .cotizaciones(alyc.getTiposDeCambio().stream().map(TipoDeCambioDTO::crearDTO).collect(Collectors.toList()))
					  .build();
		return dto;
		
	}
}
