package com.proyecto.base.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import org.hibernate.annotations.DynamicUpdate;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
@Embeddable
@DynamicUpdate(value = true)
public class Creacion {
	
	@ManyToOne(optional = true,fetch = FetchType.LAZY)
	@JoinColumn(name = "USUARIO_CREACION", insertable=true, updatable=true)
	private Usuario usuarioCreacion;
	
	@Column(name="FECHA_CREACION")
	private Date fechaCreacion;
	
}