package com.proyecto.base.model;

import java.util.Date;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.proyecto.base.enums.EstadosEnum;

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
@Table(name = "plantilla")
public class Plantilla {
	
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @OneToOne(mappedBy = "plantilla")
    private Alyc alyc;
    
    @Column(nullable = false, length = 40)
    private String especie;

    @Column(nullable = false, length = 40)
    private String fechaOperacion;
    
    @Column(nullable = false, length = 40)
    private String cantidadValorNominal;
    
    @Column(nullable = false, length = 40)
    private String precioMesa;    
    
    @Column(nullable = false, length = 40)
    private String precioCliente;   
    
	@Embedded
	@JsonIgnore
	private Auditoria auditoria;
    
    
	public boolean esMiEspecie(String especieForm) {
		return this.especie.equalsIgnoreCase(especieForm);
	}
	
	public boolean esMiFechaOperacion(String fechaOperacionForm) {
		return this.fechaOperacion.equalsIgnoreCase(fechaOperacionForm);
	}
	
	public boolean esMiCantidadValorNominal(String cantidadValorNominalForm) {
		return this.cantidadValorNominal.equalsIgnoreCase(cantidadValorNominalForm);
	}
	
	public boolean esMiPrecioMesa(String precioMesaForm) {
		return this.precioMesa.equalsIgnoreCase(precioMesaForm);
	}
	
	public boolean esMiPrecioClienteForm(String precioClienteForm) {
		return this.precioCliente.equalsIgnoreCase(precioClienteForm);
	}

	public boolean esMiAlyc(Alyc alyc) {
		return this.alyc == alyc;
	}

	@Override
	public String toString() {
		return "Plantilla [especie=" + especie + ", fechaOperacion=" + fechaOperacion + ", cantidadValorNominal="
				+ cantidadValorNominal + ", precioMesa=" + precioMesa + ", precioCliente=" + precioCliente + "]";
	}

	
	
	
}
