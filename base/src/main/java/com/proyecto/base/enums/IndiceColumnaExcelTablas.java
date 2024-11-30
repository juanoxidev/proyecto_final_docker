package com.proyecto.base.enums;
public enum IndiceColumnaExcelTablas {

	contextoAMARILLO(8),
	alycAMARRILLO(15),
	contextoAZUL(36),
	alycAZUL(43),// 22 29 36 43
	contextoVERDE(22),
	alycVERDE(29);
	
	
	
	private int pos;
	
	 IndiceColumnaExcelTablas(int num){
		 this.pos =num;
	 }
	 
	public int getPos() {
		return this.pos;
	}
}
