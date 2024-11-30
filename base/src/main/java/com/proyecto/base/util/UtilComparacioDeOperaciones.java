package com.proyecto.base.util;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.apache.poi.ss.usermodel.Row;

import com.proyecto.base.dto.OperacionReporteDTO;
import com.proyecto.base.enums.IndiceExcel;

public class UtilComparacioDeOperaciones {

	public static boolean compararOperaciones (OperacionReporteDTO operacionActual, OperacionReporteDTO operacionAjena) {
		
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println("usando el metodo 'compararOperaciones'");
		System.out.println("usando el metodo 'compararOperaciones'");
		System.out.println("usando el metodo 'compararOperaciones'");
		System.out.println("usando el metodo 'compararOperaciones'");
		System.out.println("usando el metodo 'compararOperaciones'");
		System.out.println();
		System.out.println();
		System.out.println();
		System.out.println();
		
		
		return     compararFechas(operacionActual.getFecha(), operacionAjena.getFecha())
		        && compararEspecie(operacionActual.getEspecie(), operacionAjena.getEspecie())
		        && compararCantidadVN(operacionActual.getCantidadVN(), operacionAjena.getCantidadVN())
		        && compararPrecioMesa(operacionActual.getPrecioMesa(), operacionAjena.getPrecioMesa())
		        && compararPrecioCliente(operacionActual.getPrecioCliente(), operacionAjena.getPrecioCliente());
	}
	
	public static boolean compararOperacionesSinLaFecha(OperacionReporteDTO operacionActual, OperacionReporteDTO operacionAjena) {
		return compararEspecie(operacionActual.getEspecie(), operacionAjena.getEspecie())
		        && compararCantidadVN(operacionActual.getCantidadVN(), operacionAjena.getCantidadVN())
		        && compararPrecioMesa(operacionActual.getPrecioMesa(), operacionAjena.getPrecioMesa())
		        && compararPrecioCliente(operacionActual.getPrecioCliente(), operacionAjena.getPrecioCliente());
	}
	
	public static boolean compararOperacionesSinLaFechaYConElPrecioDeCompraYVentaINVERTIDOS (OperacionReporteDTO operacionActual, OperacionReporteDTO operacionAjena) {
		return compararEspecie(operacionActual.getEspecie(), operacionAjena.getEspecie())
		        && compararCantidadVN(operacionActual.getCantidadVN(), operacionAjena.getCantidadVN())
		        && compararPrecioMesa(operacionActual.getPrecioMesa(), operacionAjena.getPrecioCliente())
		        && compararPrecioCliente(operacionActual.getPrecioCliente(), operacionAjena.getPrecioMesa());
	}
	
	public static boolean compararOperacionesConElPrecioDeCompraYVentaINVERTIDOS (OperacionReporteDTO operacionActual, OperacionReporteDTO operacionAjena) {
		return  compararFechas(operacionActual.getFecha(), operacionAjena.getFecha())
				&& compararEspecie(operacionActual.getEspecie(), operacionAjena.getEspecie())
		        && compararCantidadVN(operacionActual.getCantidadVN(), operacionAjena.getCantidadVN())
		        && compararPrecioMesa(operacionActual.getPrecioMesa(), operacionAjena.getPrecioCliente())
		        && compararPrecioCliente(operacionActual.getPrecioCliente(), operacionAjena.getPrecioMesa());
	}
	
	public static boolean compararOperacionesConPequeñasDiferenciasEnPreciosYFechas(OperacionReporteDTO operacionActual, OperacionReporteDTO operacionAjena) {
		
		boolean verificacion=false;
		
		if(compararOperacionesSinLaFecha(operacionActual,operacionAjena) || compararOperacionesSinLaFechaYConElPrecioDeCompraYVentaINVERTIDOS(operacionActual,operacionAjena)) {
			verificacion=true;
		}
		
		return verificacion;
	}
	
    public static boolean compararOperacionesPeroConOtroFormatoEnLaFecha (OperacionReporteDTO operacionContexto, OperacionReporteDTO operacionAlyc) {
		
		
		return     compararFechasConFormatosInvertidos(operacionContexto.getFecha(), operacionAlyc.getFecha())
		        && compararEspecie(operacionContexto.getEspecie(), operacionAlyc.getEspecie())
		        && compararCantidadVN(operacionContexto.getCantidadVN(), operacionAlyc.getCantidadVN())
		        && compararPrecioMesa(operacionContexto.getPrecioMesa(), operacionAlyc.getPrecioMesa())
		        && compararPrecioCliente(operacionContexto.getPrecioCliente(), operacionAlyc.getPrecioCliente());
	}
	
    public static boolean compararOperacionesConLasFechasInvertidasYElPrecioDeCompraYVentaINVERTIDOS (OperacionReporteDTO operacionContexto, OperacionReporteDTO operacionALyc) {
		return     compararFechasConFormatosInvertidos(operacionContexto.getFecha(), operacionALyc.getFecha())
				&& compararEspecie(operacionContexto.getEspecie(), operacionALyc.getEspecie())
		        && compararCantidadVN(operacionContexto.getCantidadVN(), operacionALyc.getCantidadVN())
		        && compararPrecioMesa(operacionContexto.getPrecioMesa(), operacionALyc.getPrecioCliente())
		        && compararPrecioCliente(operacionContexto.getPrecioCliente(), operacionALyc.getPrecioMesa());
	}
    
    private static boolean compararFechasConFormatosInvertidos(LocalDate fechaOperacionContexto,LocalDate fechaOperacionAlyc) {
    	boolean verificacion =false;
    	DateTimeFormatter formatoSalidaFechaAlyc = DateTimeFormatter.ofPattern("MM/dd/yy");
    	DateTimeFormatter formatoSalidaFechaContexto = DateTimeFormatter.ofPattern("dd/MM/yy");
    	String fechaFormateadaALYC = fechaOperacionAlyc.format(formatoSalidaFechaAlyc);
    	String fechaFormateadaContexto= fechaOperacionContexto.format(formatoSalidaFechaContexto);
    	
    	if(fechaFormateadaALYC.equals(fechaFormateadaContexto)) {
    		verificacion=true;
    	}
    	
    	return verificacion;
    };

	public static boolean sonIgualesSacandoLaFecha (Row row, OperacionReporteDTO operacionActual) {
		// Validar si la celda de "ESPECIE" no es nula y comparar
	    if (row.getCell(IndiceExcel.ESPECIE.getPos()) == null || 
	        !compararEspecie(operacionActual.getEspecie(), row.getCell(IndiceExcel.ESPECIE.getPos()).getStringCellValue())) {
	        return false;
	    }

	    // Validar si la celda de "CANTIDADVN" no es nula y comparar
	    if (row.getCell(IndiceExcel.CANTIDADVN.getPos()) == null || 
	        !compararCantidadVN(operacionActual.getCantidadVN(), (int) row.getCell(IndiceExcel.CANTIDADVN.getPos()).getNumericCellValue())) {
	        return false;
	    }

	    // Validar si la celda de "PRECIOMESA" no es nula y comparar
	    if (row.getCell(IndiceExcel.PRECIOMESA.getPos()) == null || 
	        !compararPrecioMesa(operacionActual.getPrecioMesa(), row.getCell(IndiceExcel.PRECIOMESA.getPos()).getNumericCellValue())) {
	        return false;
	    }

	    // Validar si la celda de "PRECIOCLIENTE" no es nula y comparar
	    if (row.getCell(IndiceExcel.PRECIOCLIENTE.getPos()) == null || 
	        !compararPrecioCliente(operacionActual.getPrecioCliente(), row.getCell(IndiceExcel.PRECIOCLIENTE.getPos()).getNumericCellValue())) {
	        return false;
	    }

	    // Si todas las comparaciones fueron exitosas, retornar verdadero
	    return true;
	}
	
	public static boolean sonIgualesSacandoLaFechaYInvirtiendoPreciosDeCompraYCliente (Row row, OperacionReporteDTO operacionActual) {
		// Validar si la celda de "ESPECIE" no es nula y comparar
	    if (row.getCell(IndiceExcel.ESPECIE.getPos()) == null || 
	        !compararEspecie(operacionActual.getEspecie(), row.getCell(IndiceExcel.ESPECIE.getPos()).getStringCellValue())) {
	        return false;
	    }

	    // Validar si la celda de "CANTIDADVN" no es nula y comparar
	    if (row.getCell(IndiceExcel.CANTIDADVN.getPos()) == null || 
	        !compararCantidadVN(operacionActual.getCantidadVN(), (int) row.getCell(IndiceExcel.CANTIDADVN.getPos()).getNumericCellValue())) {
	        return false;
	    }

	    // Validar si la celda de "PRECIOMESA" no es nula y comparar
	    if (row.getCell(IndiceExcel.PRECIOMESA.getPos()) == null || 
	        !compararPrecioMesa(operacionActual.getPrecioMesa(), row.getCell(IndiceExcel.PRECIOCLIENTE.getPos()).getNumericCellValue())) {
	        return false;
	    }

	    // Validar si la celda de "PRECIOCLIENTE" no es nula y comparar
	    if (row.getCell(IndiceExcel.PRECIOCLIENTE.getPos()) == null || 
	        !compararPrecioCliente(operacionActual.getPrecioCliente(), row.getCell(IndiceExcel.PRECIOMESA.getPos()).getNumericCellValue())) {
	        return false;
	    }

	    // Si todas las comparaciones fueron exitosas, retornar verdadero
	    return true;
	}
	
	
	private static boolean compararFechas (LocalDate fecha1, LocalDate fecha2) {
		
		System.out.println("comparando fechas de las Operaciones");
		System.out.println("fechaActual: "+fecha1);
		System.out.println("fechaAjena: "+fecha2);
		
		if(fecha1.equals(fecha2)) {
			System.out.println("las fechas son iguales");
		}else {
			System.out.println("las fechas NO son iguales");
		}
		
		return fecha1.equals(fecha2);
	}
	
	private static boolean compararEspecie(String especie1,String especie2) {
		
		boolean validacion=false;
		
		if (especie1 == null || especie2 == null) return validacion;
		
		if(especie1.contains(especie2) || sonIguales(especie1,especie2) || especie2.contains(especie1)) {
			validacion= true;
		}
		
		System.out.println("comparando especies de las Operaciones");
		System.out.println("especieActual: "+especie1);
		System.out.println("especieAjena: "+especie2);
		
		if(validacion) {
			System.out.println("las especies son iguales");
		}else {
			System.out.println("las especies NO son iguales");
		}
		
		
		
		return validacion;
	}
	
	private static boolean compararCantidadVN (int valor1,int valor2) {
		
		System.out.println("comparando compararCantidadVN de las Operaciones");
		System.out.println("valorActual: "+valor1);
		System.out.println("valorAjena: "+valor2);
		
		if(valor1==valor2) {
			System.out.println("las CantidadVN son iguales");
		}else {
			System.out.println("las CantidadVN NO son iguales");
		}
		
		
		return valor1==valor2;
	}
	
	private static boolean compararPrecioMesa (double valor1,double valor2) {
		System.out.println("comparando PrecioMesa de las Operaciones");
		System.out.println("valorActual: "+valor1);
		System.out.println("valorAjena: "+valor2);
		boolean match = false;
		
		if(valor1 == valor2 ||compararConValorX100yDivido10(valor1,valor2)|| compararConValorX100(valor1,valor2) || compararConValorDividido100(valor1,valor2)||compararConMinimaDiferencia(valor1,valor2)) {
			match=true;
			System.out.println("las PrecioMesa son iguales");
		}else {
			System.out.println("las PrecioMesa NO son iguales");
		}

		return match;
	}
	
	// MODULARIZAR TODO
	private static boolean compararPrecioCliente (double valor1,double valor2) {
		
		System.out.println("comparando PrecioCliente de las Operaciones");
		System.out.println("valorActual: "+valor1);
		System.out.println("valorAjena: "+valor2);
		boolean match = false;
		
		if(valor1 == valor2 ||compararConValorX100yDivido10(valor1,valor2) ||compararConValorX100(valor1,valor2) || compararConValorDividido100(valor1,valor2)|| compararConMinimaDiferencia(valor1,valor2)) {
			match=true;
			System.out.println("las PrecioCliente son iguales");
		}else {
			System.out.println("las PrecioCliente NO son iguales");
		}
		
		return match;
	}
	
	private static boolean compararConMinimaDiferencia(double valor1, double valor2) {
		double diferenciaAceptable =0.01;
	    return Math.abs(valor1 - valor2) < diferenciaAceptable;
	}
	
	
	private static boolean compararConValorX100yDivido10 (double valor1,double valor2) {
		System.out.println("comparando PrecioCliente de las Operaciones, compararConValorX100yDivido10");
		double valor1D= valor1/10;
		double valor2D= valor2*100;
		
		int valor1I=(int)valor1D;
		int valor2I=(int)valor2D;
		
		System.out.println("valorActual: "+valor1I);
		System.out.println("valorAjena: "+valor2I);
		
		return valor1I == valor2I;
	}
	
	
	private static boolean compararConValorX100 (double valor1,double valor2) {
		System.out.println("comparando PrecioCliente de las Operaciones, compararConValorX100");
		
		double valor1D= valor1;
		double valor2D= valor2*100;
		
		int valor1I=(int)valor1D;
		int valor2I=(int)valor2D;
		

		System.out.println("valorActual: "+valor1I);
		System.out.println("valorAjena: "+valor2I);
		
		
		return valor1I == valor2I;
	}
	
	private static boolean compararConValorDividido100 (double valor1,double valor2) {
		System.out.println("comparando PrecioCliente de las Operaciones, compararConValorDividido100");
		
		double valor1D= valor1;
		double valor2D= valor2/100;
		
		int val1=(int)valor1D;
		int val2=(int)valor2D;

		System.out.println("valorActual: "+val1);
		System.out.println("valorAjena: "+val2);
		
		
		return val1 == val2;
	}
	
	private static boolean sonIguales(String especie1,String especie2) {
		return especie1.trim().equalsIgnoreCase(especie2.trim());
	}
	
	
}
