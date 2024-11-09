package com.proyecto.base.enums;

public enum AlycsEnum {

	TOMARINVERSIONES("Tomar Inversiones",true),
	NACIONBURSATIL("Nacion Bursatil",false),
	CONOSUR("Conosur",false),
	COCOSCAPITAL("Cocos Capital",false),
	ALLARIA ("Allaria",false),
	ADCAP ("Adcap",false),
	TODOS("Todos",false);
	
	public static final AlycsEnum[] ALL = { TODOS, ADCAP,ALLARIA,COCOSCAPITAL, CONOSUR, NACIONBURSATIL, TOMARINVERSIONES };
	private String nombre;
	private boolean hayQueMultiplicarSusValoresX100;
	
	AlycsEnum(String nombre,boolean hayQueMultiplicarSusValoresX100) {
		this.nombre=nombre;
		this.hayQueMultiplicarSusValoresX100=hayQueMultiplicarSusValoresX100;
		// TODO Auto-generated constructor stub
	}

	public String getNombre() {
		return nombre;
	}

	public boolean hayQueMultiplicarValores() {
		return this.hayQueMultiplicarSusValoresX100;
	}
	
	 // Método estático para obtener la instancia a partir del nombre
    public static AlycsEnum buscarAlyc(String nombre) throws Exception {
        
    	int i=0;
    	AlycsEnum buscado=null;
    	AlycsEnum[] alycs= AlycsEnum.values();
    	
    	while(i<alycs.length && buscado==null) {
    		
    		if(alycs[i].getNombre().trim().equalsIgnoreCase(nombre.trim())) {
    			buscado=alycs[i];
    		} else {
    			i++;
    		}
    		
    	}
    	
    	if(buscado==null) throw new Exception("NO SE PUDO BUSCAR ALYC EN (buscarAlyc) del Enum : (AlycsEnum)");
    	
        return buscado; 
    }
	
	
	
	

	
	
}
