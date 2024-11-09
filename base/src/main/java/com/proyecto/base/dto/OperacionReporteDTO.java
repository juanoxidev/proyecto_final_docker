package com.proyecto.base.dto;



import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@NoArgsConstructor
@Setter
public class OperacionReporteDTO {

	private String especie;         
    private LocalDate fecha;       
    private int cantidadVN;        
    private double precioMesa;     
    private double precioCliente; 
	
    
    @Override
    public String toString() {
        return "OperacionReporteDTO :" +
                "ticker='" + especie + '\'' +
                ", fecha=" + fecha +
                ", cantidadVN=" + cantidadVN +
                ", precioMesa=" + precioMesa +
                ", precioCliente=" + precioCliente +
                '}';
    }


	
}
