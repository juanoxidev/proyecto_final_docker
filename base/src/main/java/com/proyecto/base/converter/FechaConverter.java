package com.proyecto.base.converter;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import com.proyecto.base.constantes.WConstant;
import com.proyecto.base.service.ComparacionReportesServiceImpl;

public class FechaConverter {

	private static final DataFormatter formatter = new DataFormatter();

	public static Sheet adaptarDeSerNecesarioLasFechas(Sheet sheet, int año) throws Exception{

		Sheet laHojaQuePuedeSerSuceptibleDeCambiosEnSusFechas = sheet;
		int fechaColIndex = -1;

		try {
			fechaColIndex = ComparacionReportesServiceImpl.obtenerIndiceColumna(sheet, WConstant.FECHA_OPERACION);
			laHojaQuePuedeSerSuceptibleDeCambiosEnSusFechas = estandarizarFechas(sheet, fechaColIndex, año);
		} catch (Exception e) {
			e.getMessage();
			throw new Exception("Fallo al convertir las fechas, en fecha converter " +e.getMessage() );
		}

		return laHojaQuePuedeSerSuceptibleDeCambiosEnSusFechas;
	}

	private static Sheet estandarizarFechas(Sheet sheet, int fechaColIndex, int año) {
		// Iteramos sobre todas las filas, comenzando desde la primera fila de datos
		// (después del encabezado)

		Row fila = null;
		Cell celdaFecha = null;
		String fechaTexto = null;

		// recorremos todas las "operaciones" y les ajustamos las fechas
		// MODULARIZAR BIEN UNA VEZ CORREGIDO TODO TODO TODO TODO TODO TODO TODO TODO
		// TODO
		for (int pos = 1; pos < sheet.getPhysicalNumberOfRows(); pos++) {
			fila = sheet.getRow(pos);
			if (laFilaExiste(fila)) {
				celdaFecha = fila.getCell(fechaColIndex);

				if (laCeldaExiste(celdaFecha)) {

					if (celdaFecha.getCellType() == CellType.STRING) { // aca no deberia ser del
																								// tipo
						// LocalDate?
						fechaTexto = celdaFecha.getStringCellValue().trim(); // OJO que capaz estas transformando algo
																				// de
						// tipo LocalDate a String y es NO SE PUEDE
						
						System.out.println("LA FECHA AL CONVERTIRLA SE DETECTO COMO UN : STRING ");
						System.out.println("la fecha : "+ fechaTexto);
						
						

					} else if (celdaFecha.getCellType() == CellType.NUMERIC&& DateUtil.isCellDateFormatted(celdaFecha)) {
// Convertir NUMERIC a String si es fecha
						Date fechaExcel = celdaFecha.getDateCellValue();
						LocalDate fechaLocalDate = fechaExcel.toInstant().atZone(java.time.ZoneId.systemDefault())
								.toLocalDate();

// Convertir LocalDate a texto en el formato "dd/MM/yyyy"
						DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy"); //TODO , parche . si yo ponia "dd/mm/yy" por alguna razon en el excel se mostraba como "mm/dd/yy" asi que inverti el input aca y lo hice "mm/dd/yy" y en el excel ahora sale como "dd/mm/yy" ta raro, seguro hay algo por aca 
						fechaTexto = fechaLocalDate.format(formatter);

// Reemplazar el contenido de la celda con el valor String
						celdaFecha.setCellValue(fechaTexto);
						
						System.out.println("LA FECHA AL CONVERTIRLA SE DETECTO COMO : NUMERIC");
						System.out.println("la fecha : "+ fechaTexto);

					}

				}

				if (fechaTexto != null) {
					fechaTexto = remplazarGuionesPorBarritas(fechaTexto);
					fechaTexto = agregarAñoPorSiLeFalta(fechaTexto, año);

					celdaFecha.setCellValue(fechaTexto);
				}

			}

		}

		return sheet;
	}
	
	private static boolean laFilaExiste(Row fila) {
		return fila!=null;
	}
	
	private static boolean laCeldaExiste(Cell cell) {
		return cell!=null;
	}

	private static String agregarAñoPorSiLeFalta(String fechaTexto, int año) {

		String fecha = fechaTexto;

		if (fecha.trim().length() <= 5) {
			fecha = fechaTexto + "/" + año;
		}

		return fecha;
	}

	private static String remplazarGuionesPorBarritas(String fechaTexto) {
		return fechaTexto.replace("-", "/");
	}

}
