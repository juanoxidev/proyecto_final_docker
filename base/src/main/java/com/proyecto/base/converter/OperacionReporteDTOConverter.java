package com.proyecto.base.converter;

import java.time.LocalDate;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import com.proyecto.base.constantes.WConstant;
import com.proyecto.base.dto.OperacionReporteDTO;

public class OperacionReporteDTOConverter {

	public static OperacionReporteDTO rowToDTO(Row row, HashMap<String, Integer> encabezado) {

		OperacionReporteDTO operacion = new OperacionReporteDTO();

		System.out.println("EJECUTANDO METODO 'rowToDTO'");

		System.out.println("iterando el hash map");

		/*
		 * for (String clave : encabezado.keySet()) { Integer valor =
		 * encabezado.get(clave); System.out.println("Clave: " + clave + ", Valor: " +
		 * valor); }
		 */

		if (encabezado.isEmpty()) {
			System.out.println("El HashMap está vacío.");
		} else {
			System.out.println("El HashMap no está vacío.");
			
			System.out.println("----------------------");
			for (String clave : encabezado.keySet()) { 
				Integer valor = encabezado.get(clave);
				 System.out.println("Clave: " + clave + ", Valor: " + valor); }
			
		}

		
		  
		 

		// se llaman a metodos internos para hacer las validaciones pertinentes
		 System.out.println("'getStringCellValue'");
		operacion.setEspecie(getStringCellValue(row, encabezado.get(WConstant.ESPECIE)));
		 System.out.println("'getLocalDateCellValue'");
		operacion.setFecha(getLocalDateCellValue(row, encabezado.get(WConstant.FECHA_OPERACION)));
		 System.out.println("'getNumericCellValue'");
		operacion.setCantidadVN(getNumericCellValue(row, encabezado.get(WConstant.CANTIDAD_V_N), 0));
		 System.out.println("'getNumericCellValue'");
		operacion.setPrecioMesa(getNumericCellValue(row, encabezado.get(WConstant.PRECIO_MESA), 0.0));
		 System.out.println("'getNumericCellValue'");
		operacion.setPrecioCliente(getNumericCellValue(row, encabezado.get(WConstant.PRECIO_CLIENTE), 0.0));

		return operacion;
	}

	private static String getStringCellValue(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		String valorPordefecto = "";

		return (cell != null && cell.getCellType() == CellType.STRING) ? cell.getStringCellValue() : valorPordefecto;
	}

	private static LocalDate getLocalDateCellValue(Row row, int cellIndex) {
		Cell cell = row.getCell(cellIndex);
		LocalDate fechaConvertida = null;

		if (cell != null) {
			switch (cell.getCellType()) {
			case NUMERIC:
				fechaConvertida = cell.getLocalDateTimeCellValue().toLocalDate();
				break;
			case STRING:
				String fechaString = cell.getStringCellValue();
				fechaConvertida = parseStringToLocalDate(fechaString);
				break;
			case FORMULA:
				if (DateUtil.isCellDateFormatted(cell)) {
					fechaConvertida = cell.getLocalDateTimeCellValue().toLocalDate();
				}
				break;
			default:
				break;
			}
		}

		return fechaConvertida;

		// Valor por defecto si la celda es nula o no es NUMERIC
	}

	public static LocalDate parseStringToLocalDate(String dateString) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		try {
			return LocalDate.parse(dateString, formatter);
		} catch (DateTimeParseException e) {

			return null;
		}
	}

	private static int getNumericCellValue(Row row, int cellIndex, int defaultValue) {
		Cell cell = row.getCell(cellIndex);

		if (cell != null) {
			switch (cell.getCellType()) {
			case NUMERIC:
				return (int) cell.getNumericCellValue();
			case STRING:
				try {
					return Integer.parseInt(cell.getStringCellValue().trim());
				} catch (NumberFormatException e) {
					// Manejo del error si la conversión falla
					System.out.println("Error al convertir a int: " + e.getMessage());
				}
				break;
			default:
				break;
			}
		}

		return defaultValue; // defecto
	}

	// MODULARIZAR TODO
	private static double getNumericCellValue(Row row, int cellIndex, double defaultValue) {
		Cell cell = row.getCell(cellIndex);
		String valorCelda = "000";
		Double numeroConvertido = 00.00;

		if(cell!=null) {
			try {
				
				// en este caso si hago las normalizacion previas no deberia SER NUNCA 
				// CellType.STRING. ya que las normalizaciones deberian hacer que quede
				//la celda como Numeric
				
				if (cell.getCellType() == CellType.STRING) {
					valorCelda = cell.getStringCellValue();
				} else if (cell.getCellType() == CellType.NUMERIC) {
					 valorCelda = String.valueOf(cell.getNumericCellValue());
				}
				
				System.out.println("este es el valor de la celda :"+ cell.getCellType());
				System.out.println("este es el valor de la celda :"+ cell.getCellType());
				System.out.println("este es el valor de la celda :"+ cell.getCellType());

				System.out.println();
				System.out.println();
				
				// OJO CON EL CONVERSOR ACA PERO PARA EL ALYC
				// OJO CON EL CONVERSOR ACA PERO PARA EL ALYC
				// OJO CON EL CONVERSOR ACA PERO PARA EL ALYC
				
				System.out.println("ESTE ES EL VALOR DEL STRING ANTES DE CAMBIAR : "+valorCelda);
				System.out.println("ESTE ES EL VALOR DEL STRING ANTES DE CAMBIAR : "+valorCelda);
				System.out.println("ESTE ES EL VALOR DEL STRING ANTES DE CAMBIAR : "+valorCelda);

				
				//valorCelda=sacarCerosDespuesDelPunto(valorCelda);
				
				System.out.println("ESTE ES EL VALOR DEL STRING: "+valorCelda);
				System.out.println("ESTE ES EL VALOR DEL STRING: "+valorCelda);
				System.out.println("ESTE ES EL VALOR DEL STRING: "+valorCelda);
				
				
			    numeroConvertido =Double.parseDouble(valorCelda);
			    
			    
			    System.out.println("ESTE ES EL NUMERO CONVERTIDO: "+numeroConvertido);
			    System.out.println("ESTE ES EL NUMERO CONVERTIDO: "+numeroConvertido);
			    System.out.println("ESTE ES EL NUMERO CONVERTIDO: "+numeroConvertido);
			} catch (Exception e) {
				System.out.println("Error (getNumericCellValue)(OperacionReporteDTOConverter) al convertir a double: " + e.getMessage());
			}
		}
		
		return numeroConvertido;
	}

	
	/*private static String sacarCerosDespuesDelPunto(String valorCelda) {
		int puntoIndex = valorCelda.indexOf('.');
		String resultado=valorCelda;
		
		if(puntoIndex!=-1) {
			String parteEntera = valorCelda.substring(0, puntoIndex);
			String parteDecimal = valorCelda.substring(puntoIndex+ 1);
			
			// Usar un StringBuilder para la parte decimal
	        StringBuilder sbDecimal = new StringBuilder(parteDecimal);
			
			// Recorrer la parte decimal desde el final para eliminar ceros
	        int i = 0;
	        //boolean verificacion=false;
	        
	        if(sbDecimal.charAt(i) == '0') {
        		//verificacion =true;
        		return parteEntera;
        	} else {
        		return parteEntera+"."+sbDecimal.charAt(i);
        	}
	        
	        /*i = sbDecimal.length() - 1; 
	        
	        while (i > 0 && !verificacion) {
	        	
	        	if(sbDecimal.charAt(i)== '0') {
	        		sbDecimal.deleteCharAt(i);
	        	}
	            
	            i--;
	        }
	
		}
		return resultado;
		
	}*/
}
