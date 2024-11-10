package com.proyecto.base.model;

import java.time.LocalDateTime;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.base.converter.EstadoConverter;
import com.proyecto.base.converter.TipoOperacionConverter;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.enums.TipoOperacion;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Operacion {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable=false)
    private Integer cantidadNominal;
    
    @OneToOne
    @JoinColumn(name="idMoneda", nullable=false)
    private Moneda moneda;

    @Column(nullable = false)
    private String cliente;
    
    @Column(nullable = false)
    private Double operado;
    
    @Column(nullable = false)
    private Double comision;
    
    
    @Column(nullable = false)
    private Double precioMesa;

    @Column(nullable = false)
    private Double precioCliente;

    @ManyToOne
    @JoinColumn(name = "idAlyc", nullable = false)
    private Alyc alyc;
    
    @ManyToOne
    @JoinColumn(name = "idPlazo", nullable = false)
    private Plazo plazo;

    @ManyToOne
    @JoinColumn(name = "idTicker")  
    private Ticker ticker;
    
	@Convert(converter = EstadoConverter.class)
	@Column(name = "ESTADO")
    private EstadosEnum estado;
	
	@Convert(converter = TipoOperacionConverter.class)
	@Column(name = "TIPO_OPERACION")
    private TipoOperacion tipoOperacion;
    
	@Embedded
	@JsonIgnore
	private Auditoria auditoria;
	
	
    @Column(nullable = false)
    private Date fecha;
    

	
	public boolean esMiFechaOperacion(Date fechaOperacionForm) {
		return this.fecha.equals(fechaOperacionForm);
	}
	
	public boolean esMiCantidadNominal(Integer cantidadNominalForm) {
		return this.cantidadNominal == cantidadNominalForm;
	}
	
	public boolean esMiPrecioMesa(Double precioMesaForm) {
		return this.precioMesa == precioMesaForm;
	}
	
	public boolean esMiPrecioCliente(Double precioClienteForm) {
		return this.precioCliente == precioClienteForm;
	}

	public boolean esMiAlyc(Alyc alyc) {
		return this.alyc == alyc;
	}
	
	public boolean esMiTicker(Ticker ticker) {
		return this.ticker == ticker;
	}
	
	public boolean esMiMoneda(Moneda moneda) {
		return this.moneda == moneda;
	}

	public boolean esMiAlyc(Long alycId) {
		return this.alyc.getId().compareTo(alycId) == 0;
	}

	public boolean esMiTicker(Long tickerId) {
		return this.ticker.getId().compareTo(tickerId) == 0;
	}

	public boolean esMiMoneda(Long monedaId) {
		return this.moneda.getId().compareTo(monedaId) == 0;
	}

	public boolean esMiPlazo(Long plazoId) {
		// TODO Auto-generated method stub
		return this.plazo.getId().compareTo(plazoId) == 0;
	}

	public boolean esMiTipoOperacion(TipoOperacion tipoOperacionForm) {
		// TODO Auto-generated method stub
		return this.tipoOperacion.equals(tipoOperacionForm);
	}
	
}
