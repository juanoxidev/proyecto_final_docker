package com.proyecto.base.enums;
public enum IndiceColumnaExcelTablas {

	contextoAMARILLO(8),
	alycAMARRILLO(15),
	contextoAZUL(22),
	alycAZUL(29),
	contextoVERDE(36),
	alycVERDE(43);
	
	
	
	private int pos;
	
	 IndiceColumnaExcelTablas(int num){
		 this.pos =num;
	 }
	 
	public int getPos() {
		return this.pos;
	}
}
