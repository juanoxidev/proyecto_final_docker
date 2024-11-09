package com.proyecto.base.enums;

public enum IndiceExcelOperacionesDesdeHasta {

	FECHA(0),
	ALYC(1),
	CLIENTE(2),
	T_OPERACION(3),
	PLAZO(4),
	ESPECIE(5),
	CANTIDADNOMINAL(6),
	MONEDA(7),
	PRECIOMESA(8),
	PRECIOCLIENTE(9),
	MONTOOPERADO(10),
	MONTOCOMISION(11);
	
	
private int pos;	
	
IndiceExcelOperacionesDesdeHasta(int pos) {
		this.pos=pos;
	}

	public int getPos() {
		return this.pos;
	}
	
}
