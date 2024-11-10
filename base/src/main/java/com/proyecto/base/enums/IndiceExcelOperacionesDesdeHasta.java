package com.proyecto.base.enums;

public enum IndiceExcelOperacionesDesdeHasta {

	
	
	
	

	FECHA(0,"Fecha"),
	ALYC(1,"ALYC"),
	CLIENTE(2,"Cliente"),
	T_OPERACION(3,"T.Operacion"),
	PLAZO(4,"Plazo"),
	ESPECIE(5,"Especie"),
	CANTIDADNOMINAL(6,"C.Nominal"),
	MONEDA(7,"Moneda"),
	PRECIOMESA(8,"P.Mesa"),
	PRECIOCLIENTE(9,"P.Cliente"),
	MONTOOPERADO(10,"M.Operado"),
	MONTOCOMISION(11,"M.Comision");
	
	
private int pos;	
private String nombre;
	
IndiceExcelOperacionesDesdeHasta(int pos,String nombre) {
		this.pos=pos;
		this.nombre=nombre;
	}

	public int getPos() {
		return this.pos;
	}
	
	public String getNombre() {
		return this.nombre;
	}
	
}
