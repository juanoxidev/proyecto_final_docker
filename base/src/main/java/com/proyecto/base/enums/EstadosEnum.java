package com.proyecto.base.enums;

public enum EstadosEnum {
	ACTIVO("A"),
	BAJA("B"),
	TODOS("");
	
	public static final EstadosEnum[] ALL = { TODOS, ACTIVO,BAJA };
	public static final EstadosEnum[] SELECCIONABLES = {ACTIVO,BAJA };

	private String codigo;
	
	private EstadosEnum(String c){
		codigo = c;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public static EstadosEnum forName(final String codigo) {
		EstadosEnum resp = BAJA;
        if (codigo == null) {
            throw new IllegalArgumentException("El estado no puede ser null.");
        }
        if (codigo.toUpperCase().equals("A")) {
        	resp = ACTIVO;
        }else if(codigo.toUpperCase().equals("B")){
        	resp = BAJA;
        }else{ 
        	throw new IllegalArgumentException("La expresión \"" + codigo + "\" no coincide con los términos válidos.");
        }
        return resp;
	}
}
