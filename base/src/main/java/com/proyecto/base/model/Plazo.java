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
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import net.bytebuddy.asm.Advice.Return;

@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Plazo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
   

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 100)
    private String descripcionCompleta;
    
	@Convert(converter = EstadoConverter.class)
	@Column(name = "ESTADO")
    private EstadosEnum estado;
	
	@Embedded
	@JsonIgnore
	private Auditoria auditoria;

	public boolean esMiNombre(String nombreForm) {
		return this.nombre.equalsIgnoreCase(nombreForm);
	}

	public boolean esMiDescripcion(String descripcionForm) {
		return this.descripcionCompleta.equalsIgnoreCase(descripcionForm);
	}

	public boolean esMiEstado(EstadosEnum estadoForm) {
		return this.estado.equals(estadoForm);
	}
}
