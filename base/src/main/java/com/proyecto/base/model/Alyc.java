package com.proyecto.base.model;

import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Convert;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

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
public class Alyc {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 40)
    private String nombre;
    

    @Column(nullable = true, length = 40)
    private String usuario;

    @Column(nullable = true, length = 100)
    private String contrasenia;

    @Column(nullable = true)
    private Double porcentajeComision;
    
    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "idPlantilla", referencedColumnName = "id")
    private Plantilla plantilla; 

    @OneToMany(mappedBy = "alyc", fetch = FetchType.LAZY)
    private List<Operacion> operaciones;
    
    @OneToMany(mappedBy = "alyc", fetch = FetchType.LAZY)
    private List<TipoDeCambio> tiposDeCambio;
    
	@Convert(converter = EstadoConverter.class)
	@Column(name = "ESTADO")
    private EstadosEnum estado;
    
	@Embedded
	@JsonIgnore
	private Auditoria auditoria;

	public boolean esMiNombre(String nombreForm) {
		return this.nombre.trim().equalsIgnoreCase(nombreForm.trim());
	}

	public boolean esMiComision(Double comisionForm) {
		return this.porcentajeComision.compareTo(comisionForm) == 0;
	}

	public boolean esMiEstado(EstadosEnum estadoForm) {
		return this.estado.equals(estadoForm);
	}
	
	
}

