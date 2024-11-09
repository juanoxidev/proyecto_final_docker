package com.proyecto.base.converter.plantillas;

import java.io.IOException;
import java.time.LocalDate;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;

import com.proyecto.base.constantes.WConstant;
import com.proyecto.base.model.Plantilla;
import com.proyecto.base.repository.PlantillaRepository;

public class ConverterAFormatoEstandar {

	@Autowired
	private PlantillaRepository plantillaRepository;
	
	
	// esto deberia recibir una Workbook o Sheet por parametro TODO. NO UN STRING
	public static Workbook cambiarNombreColumnas(Workbook workbook,Plantilla plantilla) throws IOException {

	    final int  poscolumnas=0; // se entiende que el nombre de las columnas siempre se encuentra en la fila 0 del Sheet
		
		for (int i=0;i<workbook.getNumberOfSheets(); i++) {
			
			Sheet sheet = workbook.getSheetAt(i); // Obtener la primera hoja

			// Obtener la primera fila (supongamos que contiene los nombres de las columnas)
			Row headerRow = sheet.getRow(poscolumnas);
			
			modificarNombresDelEncabezado(headerRow,plantilla);

			
		}
		
		return workbook;
	}

	
	private static void modificarNombresDelEncabezado (Row headerRow,Plantilla plantilla) throws IOException {
		
		if (headerRow == null)throw new IOException("las Columnas que se estan intentado leer en ''modificarNombresColumnas'' estan NULAS o presentan un problema");
        String nombreColumna="";
		
		for (Cell cell : headerRow) {
			
			
			if (cell.getCellType() == CellType.STRING) {
	            nombreColumna = cell.getStringCellValue();
	        } else if (cell.getCellType() == CellType.NUMERIC) {
	            nombreColumna = String.valueOf(cell.getNumericCellValue());
	        }
			
			System.out.println("IMPRIMIENRO NOMBRE DE LA CELDA DURANTE LA ESTANDARIZACION: "+nombreColumna);

			if (nombreColumna.trim().equalsIgnoreCase(plantilla.getFechaOperacion().trim())) {

				cell.setCellValue(WConstant.FECHA_OPERACION);

			} else if (nombreColumna.trim().equalsIgnoreCase(plantilla.getEspecie().trim())) {

				cell.setCellValue(WConstant.ESPECIE);

			} else if (nombreColumna.trim().equalsIgnoreCase(plantilla.getCantidadValorNominal().trim())) {

				cell.setCellValue(WConstant.CANTIDAD_V_N);

			} else if (nombreColumna.trim().equalsIgnoreCase(plantilla.getPrecioMesa().trim())) {

				cell.setCellValue(WConstant.PRECIO_MESA);

			} else if (nombreColumna.trim().equalsIgnoreCase(plantilla.getPrecioCliente().trim())) {

				cell.setCellValue(WConstant.PRECIO_CLIENTE);

			}

		}
	}

	protected void ajustarFechasAFormatoEstandar(LocalDate año) {

	};
	
	

}
