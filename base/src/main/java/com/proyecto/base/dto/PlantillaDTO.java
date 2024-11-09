package com.proyecto.base.dto;

import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.model.Alyc;
import com.proyecto.base.model.Plantilla;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PlantillaDTO implements IComplementoAlyc{

    private Long id;
    private String especie;
    private Long alyc;
    private String fechaOperacion;
    private String cantidadValorNominal;
    private String precioMesa;    
    private String precioCliente;  
    
	
	public static PlantillaDTO crearDTOBusqueda(Plantilla plantilla) {
		PlantillaDTO dto = PlantillaDTO.builder()
					  .id(plantilla.getId())
					  .alyc(plantilla.getAlyc().getId())
					  .fechaOperacion(plantilla.getFechaOperacion())
					  .especie(plantilla.getEspecie())
					  .cantidadValorNominal(plantilla.getCantidadValorNominal())
					  .precioMesa(plantilla.getPrecioMesa())
					  .precioCliente(plantilla.getPrecioCliente())
					  .build();
		return dto;
	}



}
