package wrappersEstructurasDeDatos;

import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;

import com.proyecto.base.dto.OperacionReporteDTO;
import com.proyecto.base.enums.IndiceExcel;
import com.proyecto.base.util.UtilComparacioDeOperaciones;

public class ListaEnlazadaDeOperaciones extends LinkedList<OperacionReporteDTO> {

	private static final long serialVersionUID = 1L;

	public ListaEnlazadaDeOperaciones() {

	}

	public OperacionReporteDTO tenesEstaOperacion(OperacionReporteDTO operacionAjena) {
		OperacionReporteDTO operacion = null;

		System.out.println("ejecutando 'tenesEstaOperacion'");

		Iterator<OperacionReporteDTO> iterator = this.iterator(); // Obtener el iterator
		while (iterator.hasNext() && operacion == null) { // Verificar si hay más elementos
			OperacionReporteDTO operacionActual = iterator.next(); // Obtener el siguiente elemento

			System.out.println("la operacionAjena tiene fecha: " + operacionActual.getFecha());
			System.out.println("la operacionAjena tiene fecha: " + operacionActual.getFecha());
			System.out.println("la operacionAjena tiene fecha: " + operacionActual.getFecha());

			if (sonIguales(operacionActual, operacionAjena)) {
				operacion = operacionActual;
			}
		}

		return operacion;
	}

	public void mostrarOperacionesInternas() {
		for (OperacionReporteDTO element : this) {
			System.out.println(element);
		}
	}

	public int llenarSheet(Sheet sheet, int posRow) {

		for (OperacionReporteDTO operacion : this) {
			Row row = sheet.createRow(posRow++);
			llenarRow(operacion, row);
		}

		return posRow;
	}

	public void eliminarOperaciones(List<OperacionReporteDTO> operacionesAEliminar) {
		System.out.println(" ejecutando (eliminarOperaciones) en (ListaEnlazadaDeOperaciones) ");
		for (OperacionReporteDTO operacion : operacionesAEliminar) {
			System.out.println();
			boolean verificacion = this.remove(operacion);

			if (verificacion) {
				System.out.println("SE ELIMINO CON EXITO DE LA LISTA EL ELEMENTO :" + operacion.getEspecie());
			}

		}
	}

	// ojo aca que el primero de fecha lo detecta bien pero los demas NO TODO
	private void llenarRow(OperacionReporteDTO operacion, Row row) {

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

		String fechaString = operacion.getFecha() != null ? operacion.getFecha().format(formatter)
				: "Fecha no disponible";

		// Crear el estilo de celda
		Workbook workbook = row.getSheet().getWorkbook();
		CellStyle estiloCelda = workbook.createCellStyle();
		// Configuración del fondo rojo y texto en negrita
		estiloCelda.setFillForegroundColor(IndexedColors.RED.getIndex());
		estiloCelda.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		Font font = workbook.createFont();
		font.setBold(true);
		estiloCelda.setFont(font);

		// Configuración de los bordes negros para las celdas
		estiloCelda.setBorderTop(BorderStyle.THIN);
		estiloCelda.setBorderBottom(BorderStyle.THIN);
		estiloCelda.setBorderLeft(BorderStyle.THIN);
		estiloCelda.setBorderRight(BorderStyle.THIN);

		// Color de los bordes
		estiloCelda.setTopBorderColor(IndexedColors.BLACK.getIndex());
		estiloCelda.setBottomBorderColor(IndexedColors.BLACK.getIndex());
		estiloCelda.setLeftBorderColor(IndexedColors.BLACK.getIndex());
		estiloCelda.setRightBorderColor(IndexedColors.BLACK.getIndex());

		System.out.println("llenandoRow");
		System.out.println("este es el valor del getFecha: " + operacion.getFecha());

		// row.createCell(IndiceExcel.FECHA.getPos()).setCellValue(fechaString);
		// Crear las celdas y aplicar el estilo a cada una
		Cell cellFecha = row.createCell(IndiceExcel.FECHA.getPos());
		cellFecha.setCellValue(fechaString);
		cellFecha.setCellStyle(estiloCelda);

		Cell cellEspecie = row.createCell(IndiceExcel.ESPECIE.getPos());
		cellEspecie.setCellValue(operacion.getEspecie());
		cellEspecie.setCellStyle(estiloCelda);

		Cell cellCantidadVN = row.createCell(IndiceExcel.CANTIDADVN.getPos());
		cellCantidadVN.setCellValue(operacion.getCantidadVN());
		cellCantidadVN.setCellStyle(estiloCelda);

		Cell cellPrecioMesa = row.createCell(IndiceExcel.PRECIOMESA.getPos());
		cellPrecioMesa.setCellValue(operacion.getPrecioMesa());
		cellPrecioMesa.setCellStyle(estiloCelda);

		Cell cellPrecioCliente = row.createCell(IndiceExcel.PRECIOCLIENTE.getPos());
		cellPrecioCliente.setCellValue(operacion.getPrecioCliente());
		cellPrecioCliente.setCellStyle(estiloCelda);
	}

	private boolean sonIguales(OperacionReporteDTO operacionPropia, OperacionReporteDTO operacionAjena) {

		return UtilComparacioDeOperaciones.compararOperaciones(operacionPropia, operacionAjena);
	}

	public void llenarListConOperaciones(ArrayList<OperacionReporteDTO> list) {
		for (OperacionReporteDTO operacion : this) {
			list.add(operacion);
		}
	}

}
