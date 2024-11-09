package com.proyecto.base.enums;

import java.util.Arrays;

public enum TipoOperacion {

	VENTA("V"),
	COMPRA("C"),
	TODOS("");
	
	public static final TipoOperacion[] ALL = { TODOS, COMPRA,VENTA };
	public static final TipoOperacion[] SELECCIONABLES = {VENTA,COMPRA};

	private String codigo;
	
	private TipoOperacion(String c){
		codigo = c;
	}
	
	public String getCodigo() {
		return codigo;
	}

	public void setCodigo(String codigo) {
		this.codigo = codigo;
	}


	public static TipoOperacion find(String codigo) {
		TipoOperacion tipoEncontrado = Arrays.stream(TipoOperacion.ALL)
									    .filter(to -> to.getCodigo().equalsIgnoreCase(codigo))
									    .findAny()
									    .orElseThrow(() -> new IllegalArgumentException("La expresión \"" + codigo + "\" no coincide con los términos válidos."));
        return tipoEncontrado;
	}
	
}
