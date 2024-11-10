package com.proyecto.base.dto;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.proyecto.base.converter.DateFechaDeserializer;
import com.proyecto.base.converter.DateFechaSerializer;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.enums.TipoOperacion;
import com.proyecto.base.model.Operacion;
import com.proyecto.base.util.UtilesFechas;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OperacionDTO {
	private Long id;
    private Integer cantidadNominal;
    private Long moneda;
    private String cliente;
    private String monedaName;
	private Double precioMesa;
	private Double precioCliente;
	private Long alyc;
	private String alycName;
	private EstadosEnum estado;
	private TipoOperacion tipoOperacion;
	private Long ticker;
	private String tickerName;
	private String plazoName;
	private Double operado;
	private Double comision;
	private Double tipoCambio;
	@JsonDeserialize(using = DateFechaDeserializer.class)
	@JsonSerialize(using = DateFechaSerializer.class)
	private Date fechaDesde;
	@JsonDeserialize(using = DateFechaDeserializer.class)
	@JsonSerialize(using = DateFechaSerializer.class)
	private Date fechaHasta;
	@JsonDeserialize(using = DateFechaDeserializer.class)
	@JsonSerialize(using = DateFechaSerializer.class)
	private Date fecha;
	private Long plazo;


	
	public static OperacionDTO crearDTOBusqueda(Operacion operacion) {
		OperacionDTO dto = OperacionDTO.builder()
					  .id(operacion.getId())
					  .cantidadNominal(operacion.getCantidadNominal())
					  .moneda(operacion.getMoneda().getId())
					  .monedaName(operacion.getMoneda().getNombre().toUpperCase())
					  .precioMesa(operacion.getPrecioMesa())
					  .precioCliente(operacion.getPrecioCliente())
					  .tipoOperacion(operacion.getTipoOperacion())
					  .alyc(operacion.getAlyc().getId())
					  .alycName(operacion.getAlyc().getNombre().toUpperCase())
					  .ticker(operacion.getTicker().getId())	
					  .tickerName(operacion.getTicker().getNombre().toUpperCase())
					  .cliente(operacion.getCliente())
					  .fecha(operacion.getFecha())
					  .operado(operacion.getOperado())
					  .comision(operacion.getComision())
					  .plazoName(operacion.getPlazo().getDescripcionCompleta())
					  .plazo(operacion.getPlazo().getId())
					  .estado(operacion.getEstado())
					  .build();
		return dto;
	}
	
	
}
