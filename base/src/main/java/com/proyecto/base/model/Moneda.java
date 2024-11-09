package com.proyecto.base.model;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.base.converter.EstadoConverter;
import com.proyecto.base.enums.EstadosEnum;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Moneda {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 4)
    private String nombre;
    
    @Column(nullable = false)
    private String descripcionCompleta;
    
    
	@Convert(converter = EstadoConverter.class)
	@Column(name = "ESTADO")
    private EstadosEnum estado;
    
	@Embedded
	@JsonIgnore
	private Auditoria auditoria;

	public boolean esMiDescripcion(String descripcionForm) {
		return this.descripcionCompleta.equalsIgnoreCase(descripcionForm);
	}

	public boolean esMiNombre(String nombreForm) {
		return this.nombre.equalsIgnoreCase(nombreForm);
	}

	public boolean esMiEstado(EstadosEnum estadoForm) {
		// TODO Auto-generated method stub
		return this.estado.equals(estadoForm);
	}
}

