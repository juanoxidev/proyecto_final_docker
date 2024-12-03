package com.proyecto.base.enums;

public enum NacionBursatilEnum {
	
	NacionBursatil;


	private String tipoDeOperacion= "OP"; //compra,venta,etc. en nacion Bursatil le llaman OP 
	private String palabraBuscada="CAUCION";
	
	public  String getTipoDeOperacion() {
		return this.tipoDeOperacion;
	}
	
	public String getPalabraBuscada() {
		return this.palabraBuscada;
	}
	
}
