package com.proyecto.base.enums;

public enum IndiceExcel {

	FECHA(0),
	ESPECIE(1),
	CANTIDADVN(2),
	PRECIOMESA(3),
	PRECIOCLIENTE(4);
	
	private int posColumna;
	
	IndiceExcel (int indice) {
		posColumna=indice;
	}
	
	public int getPos () {
		return this.posColumna;
	}
	
}
