package com.proyecto.base.service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;

import org.apache.poi.ss.usermodel.BorderStyle;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.DateUtil;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.Font;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.base.constantes.WConstant;
import com.proyecto.base.converter.FechaConverter;
import com.proyecto.base.converter.OperacionReporteDTOConverter;
import com.proyecto.base.converter.plantillas.ConverterAFormatoEstandar;
import com.proyecto.base.dto.OperacionReporteDTO;
import com.proyecto.base.enums.AlycsEnum;
import com.proyecto.base.enums.IndiceExcel;
import com.proyecto.base.model.Alyc;
import com.proyecto.base.model.Plantilla;
import com.proyecto.base.repository.AlycRepository;
import com.proyecto.base.util.UtilComparacioDeOperaciones;

import wrappersEstructurasDeDatos.LinkedHashMapDeOperaciones;

@Service
public class ComparacionReportesServiceImpl implements ComparacionReportesService {

	@Autowired
	private AlycRepository alycRepository;

	private static final DataFormatter formatter = new DataFormatter();
	private static final int primeraHoja = 0;


	public byte[] compararReportes(byte[] contextoReporte, byte[] alycReporte, Long idAlyc) throws Exception {

		Alyc alyc = alycRepository.getReferenceById(idAlyc);
		byte[] excelFinalConLasDispariedades = null;
		String alycNombre = alyc.getNombre();

		if (alycNombre == null)
			throw new Exception("NOMBRE DE LA ALYC ES NULL");

		AlycsEnum alycEnum = AlycsEnum.buscarAlyc(alycNombre);

		switch (alycEnum) {
		case TOMARINVERSIONES:
			excelFinalConLasDispariedades = comparacionParaTomarInversiones(contextoReporte, alycReporte, alyc,alycEnum);
			break;
		case NACIONBURSATIL:
			// Código a ejecutar si variable == valor2
			break;
		// Puedes agregar más casos
		default:
			// Código a ejecutar si no coincide con ningún caso
		}

		
		return excelFinalConLasDispariedades;

	}

	// VOLAR EL TRYCATCH DE ACA

	private byte[] comparacionParaTomarInversiones(byte[] contextoReporte, byte[] alycReporte, Alyc alyc,
			AlycsEnum alycEnum) {

		Workbook excelFinalConLasDispariedades = null;
		LinkedHashMapDeOperaciones contextoOperacionesMap = null;
		LinkedHashMapDeOperaciones alycOperacionesMap = null;
		Workbook AlycExcelModificado = null;
		int año = -1;
		byte[] ExcelFINAL = null;
		ArrayList<OperacionReporteDTO> contextoOperacionesOK = new ArrayList<OperacionReporteDTO> ();
		ArrayList<OperacionReporteDTO> alycOperacionesOK = new ArrayList<OperacionReporteDTO> ();

		try (InputStream contextoInputStream = new ByteArrayInputStream(contextoReporte);
				InputStream alycInputStream = new ByteArrayInputStream(alycReporte)) {

			validarSiAlgunoEsNull(contextoReporte, alycReporte, alyc);

			
			Workbook contextoWorkbook = new XSSFWorkbook(contextoInputStream);
			Workbook alycWorkbook = new XSSFWorkbook(alycInputStream);

			//imprimirEncabezado ( contextoWorkbook.getSheetAt(0));
			//iterarSheet(contextoWorkbook.getSheetAt(0));
			
			System.out.println(" NORMALIZANDO LOS NUMERO DEL EXCEL DE CONTEXTO");
			System.out.println(" NORMALIZANDO LOS NUMERO DEL EXCEL DE CONTEXTO");
			System.out.println(" NORMALIZANDO LOS NUMERO DEL EXCEL DE CONTEXTO");

			normalizarPrecios(contextoWorkbook);
			
			iterarSheet(contextoWorkbook.getSheetAt(0));
			
			System.out.println("MULTIPLICANDO LOS PRECIOS DE CONTEXTO POR 100");
			System.out.println("MULTIPLICANDO LOS PRECIOS DE CONTEXTO POR 100");
			System.out.println("MULTIPLICANDO LOS PRECIOS DE CONTEXTO POR 100");
			
			multiplicarPreciosX100SiEsNecesario(contextoWorkbook,alycEnum.hayQueMultiplicarValores());
			
			iterarSheet(contextoWorkbook.getSheetAt(0));

			// se mete a un map(ordenado por fecha) cada fila del excel
			// se meten los datos del reporteContexto a un map

			System.out.println(" EJECUTANDO PROCESAR REPORTE PARA  CONTEXTO");
			System.out.println(" EJECUTANDO PROCESAR REPORTE PARA  CONTEXTO");
			System.out.println(" EJECUTANDO PROCESAR REPORTE PARA  CONTEXTO");

			contextoOperacionesMap = procesarReporte(contextoWorkbook);
			System.out.println("--------------------------------------------------------");
			System.out.println("--------------------------------------------------------");

			contextoOperacionesMap.mostrarHashMap();
			System.out.println("--------------------------------------------------------");
			System.out.println("--------------------------------------------------------");

			// obtenemos el año actual de las operaciones, el cual usaremos en el siguiente
			// metodo para agregarle la fecha si asi lo precisare
			año = contextoOperacionesMap.obtenerAñoDePrimeraFecha();

			// se adapta el formato de la alyc al formato estandar(el formato de contexto es
			// el formato estandar)
			AlycExcelModificado = estandarizarReporte(alycWorkbook, alyc.getPlantilla(), año, alyc);

			System.out.println(" EJECUTANDO PROCESAR REPORTE PARA LA ALYC");
			System.out.println(" EJECUTANDO PROCESAR REPORTE PARA LA ALYC");
			System.out.println(" EJECUTANDO PROCESAR REPORTE PARA LA ALYC");

			System.out.println("---------------------------------------");
			// se meten los datos del AlycExcelModificado a un map
			alycOperacionesMap = procesarReporte(AlycExcelModificado);
			alycOperacionesMap.mostrarHashMap();
			System.out.println("---------------------------------------");

			System.out.println(" COMPARANDO LOS REPORTES");
			System.out.println(" COMPARANDO LOS REPORTES");
			System.out.println(" COMPARANDO LOS REPORTES");
			System.out.println(" COMPARANDO LOS REPORTES");

			// se comparan los reportes y se eliman las coincidencias, solo quedaran las
			// disparariedades en cada reporte respectivo
			compararOperaciones(contextoOperacionesMap, alycOperacionesMap,contextoOperacionesOK,alycOperacionesOK);

			System.out.println(" GENERAR EXCEL CON DISPARIEDADES");
			System.out.println(" GENERAR EXCEL CON DISPARIEDADES");
			System.out.println(" GENERAR EXCEL CON DISPARIEDADES");
			// se recorren las disparariedades correspondientes y se agregan a un excel
			// final
			excelFinalConLasDispariedades = generarExcelConLasDisparidades(contextoOperacionesMap, alycOperacionesMap,alycEnum);
			
			definicionDeCeldasAmarillas(excelFinalConLasDispariedades,contextoOperacionesMap, alycOperacionesMap);

			llenarExcelConlasOperacionesQueOperacionesQueMatchearon(excelFinalConLasDispariedades,contextoOperacionesOK,alycOperacionesOK);
			
			ExcelFINAL = convertirAVectorDeBytes(excelFinalConLasDispariedades);
			
			return ExcelFINAL;

		} catch (Exception ex) {
			System.err.println("al comparar Reportes hubo un error: " + ex.getMessage());
			System.out.println();
			System.out.println("el TOSTRING : "+ex.toString());
			System.out.println();
			System.out.println("la traza: ");
			ex.printStackTrace();

		}

		return ExcelFINAL;
	}
	
	private void llenarExcelConlasOperacionesQueOperacionesQueMatchearon(Workbook excelFinalConLasDispariedades,ArrayList<OperacionReporteDTO> contextoOperacionesOK, ArrayList<OperacionReporteDTO> alycOperacionesOK ) {
		Sheet sheet = excelFinalConLasDispariedades.getSheetAt(primeraHoja);
		int espacioCeldasContexto= 8;
		int espacioCeldasAlyc=espacioCeldasContexto*2;
		int posRowEncabezado=1;
		int posRowNombreTabla=0;
		
		agregarNombreInicialDescriptivoDeLaTabla(sheet,posRowNombreTabla,espacioCeldasContexto,"CONTEXTO - Operaciones OK");
		agregarEncabezadoConEspacio(sheet,posRowEncabezado,espacioCeldasContexto);
		llenarSheetDeOperacionesOK(sheet,contextoOperacionesOK,espacioCeldasContexto);
		
		agregarNombreInicialDescriptivoDeLaTabla(sheet,posRowNombreTabla,espacioCeldasAlyc,"ALYC - Operaciones OK");
		agregarEncabezadoConEspacio(sheet,posRowEncabezado,espacioCeldasAlyc);
		llenarSheetDeOperacionesOK(sheet,alycOperacionesOK,espacioCeldasAlyc);
		
	}
	
	
	private void agregarNombreInicialDescriptivoDeLaTabla(Sheet sheet, int posRowNombreTabla, int espacioCell, String msg) {
		crearRowSiNoExiste(sheet,posRowNombreTabla);
		Row row = sheet.getRow(posRowNombreTabla);
		crearCeldasPorSiNoExisten(row,1,espacioCell);	
		Cell cell = row.getCell(espacioCell);
		cell.setCellValue(msg);
	}
	
	private void llenarSheetDeOperacionesOK(Sheet sheet,ArrayList<OperacionReporteDTO> operacionesOK, int espacioCell) {
		
		Row row=null;
		OperacionReporteDTO operacion=null;
		int espacio= 2;
		ArrayList<Row> aPintarDeVerde= new ArrayList<Row>();
		
		// para evitar la primera row, para generarEspacio
		//ACA TODO
		for(int i=espacio;i<=operacionesOK.size();i++) {
			row = sheet.getRow(i);
			operacion=operacionesOK.get(i-espacio);
			
			crearRowSiNoExiste(sheet,i);
			
			if(operacion != null) {
				llenarRowConOperacion(row,operacion,espacioCell,sheet.getWorkbook());
				// modularizar los fonts en un utils.fonts. TODO
				//aPintarDeVerde.add(Cell);
				//utilsFonts.PintarDeVerde(aPintarDeVerde)
			}
		}
	}
	
	private void crearRowSiNoExiste(Sheet sheet, int pos) {
		if(sheet.getRow(pos)==null) {
			sheet.createRow(pos);
		}
	}
	
	// modularizar los fonts en un UtilsFonts TODO
	private void llenarRowConOperacion(Row row, OperacionReporteDTO operacion, int espacioCell,Workbook workbook) {
		int cantAtributosDeOperacion = OperacionReporteDTO.class.getDeclaredFields().length;
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
	    String fechaString = operacion.getFecha() != null ? operacion.getFecha().format(formatter) : "Fecha no disponible";

	    // Crear y aplicar el CellStyle para celdas verdes con texto en negrita
	    CellStyle greenBoldStyle = workbook.createCellStyle();

	    // Crear la fuente para el texto en negrita
	    Font font = workbook.createFont();
	    font.setBold(true); // Texto en negrita
	    greenBoldStyle.setFont(font);

	    // Establecer el color de fondo verde claro
	    greenBoldStyle.setFillForegroundColor(IndexedColors.LIGHT_GREEN.getIndex());
	    greenBoldStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

	    // Definir los bordes con color negro
	    greenBoldStyle.setBorderBottom(BorderStyle.THIN); // Borde inferior
	    greenBoldStyle.setBottomBorderColor(IndexedColors.BLACK.getIndex()); // Color del borde inferior
	    greenBoldStyle.setBorderTop(BorderStyle.THIN); // Borde superior
	    greenBoldStyle.setTopBorderColor(IndexedColors.BLACK.getIndex()); // Color del borde superior
	    greenBoldStyle.setBorderLeft(BorderStyle.THIN); // Borde izquierdo
	    greenBoldStyle.setLeftBorderColor(IndexedColors.BLACK.getIndex()); // Color del borde izquierdo
	    greenBoldStyle.setBorderRight(BorderStyle.THIN); // Borde derecho
	    greenBoldStyle.setRightBorderColor(IndexedColors.BLACK.getIndex()); // Color del borde derecho

	    // Crear las celdas si no existen
	    crearCeldasPorSiNoExisten(row, cantAtributosDeOperacion, espacioCell);

	    // Llenar cada celda y aplicar el estilo
	    Cell fechaCell = row.getCell(IndiceExcel.FECHA.getPos() + espacioCell);
	    fechaCell.setCellValue(fechaString);
	    fechaCell.setCellStyle(greenBoldStyle);

	    Cell especieCell = row.getCell(IndiceExcel.ESPECIE.getPos() + espacioCell);
	    especieCell.setCellValue(operacion.getEspecie());
	    especieCell.setCellStyle(greenBoldStyle);

	    Cell cantidadVNCell = row.getCell(IndiceExcel.CANTIDADVN.getPos() + espacioCell);
	    cantidadVNCell.setCellValue(operacion.getCantidadVN());
	    cantidadVNCell.setCellStyle(greenBoldStyle);

	    Cell precioMesaCell = row.getCell(IndiceExcel.PRECIOMESA.getPos() + espacioCell);
	    precioMesaCell.setCellValue(operacion.getPrecioMesa());
	    precioMesaCell.setCellStyle(greenBoldStyle);

	    Cell precioClienteCell = row.getCell(IndiceExcel.PRECIOCLIENTE.getPos() + espacioCell);
	    precioClienteCell.setCellValue(operacion.getPrecioCliente());
	    precioClienteCell.setCellStyle(greenBoldStyle);
		
	}
	
	private void crearCeldasPorSiNoExisten(Row row, int cantCeldasACrear,int espacioEntreCell) {
		Cell cell= null;
		int posCell=0;
		
		for(int i=0; i<cantCeldasACrear;i++) {
			posCell = i+espacioEntreCell;
			cell= row.getCell(posCell);
			
			if(cell==null) {
				row.createCell(posCell);
			}
		}

	}
	
	private void definicionDeCeldasAmarillas (Workbook workbook,LinkedHashMapDeOperaciones contextoMap, LinkedHashMapDeOperaciones alycMap ) {
		ArrayList<OperacionReporteDTO> contextList = contextoMap.generarArrayList();
		ArrayList<OperacionReporteDTO> alyctList = alycMap.generarArrayList();
		ArrayList<OperacionReporteDTO> operacionesQueCasiSonIgualesList = compararOperacionesQueSonCasiIguales(contextList,alyctList);
		ArrayList<Integer> posicionesDeLasRowAPintarDeAmarillo= buscarEnElSheetLasCoordenadasDeLasOperaciones(workbook.getSheetAt(primeraHoja),operacionesQueCasiSonIgualesList);
		
		pintarDeAmarilloLasCeldas(workbook.getSheetAt(primeraHoja),posicionesDeLasRowAPintarDeAmarillo);
		
		
	}
	
	private void pintarDeAmarilloLasCeldas(Sheet sheet,ArrayList<Integer> posicionesDeLasRowAPintarDeAmarillo) {
		
		 Workbook workbook = sheet.getWorkbook();
		    CellStyle yellowStyle = workbook.createCellStyle();
		    yellowStyle.setFillForegroundColor(IndexedColors.YELLOW.getIndex());
		    yellowStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
		    
		    // Borde para que el estilo sea más visible
		    yellowStyle.setBorderTop(BorderStyle.THIN);
		    yellowStyle.setBorderBottom(BorderStyle.THIN);
		    yellowStyle.setBorderLeft(BorderStyle.THIN);
		    yellowStyle.setBorderRight(BorderStyle.THIN);
		    
		Row row=null;
		
		for (Integer pos : posicionesDeLasRowAPintarDeAmarillo) {
			row=sheet.getRow(pos);
			row.getCell(IndiceExcel.FECHA.getPos()).setCellStyle(yellowStyle);
			row.getCell(IndiceExcel.ESPECIE.getPos()).setCellStyle(yellowStyle);
			row.getCell(IndiceExcel.CANTIDADVN.getPos()).setCellStyle(yellowStyle);
			row.getCell(IndiceExcel.PRECIOMESA.getPos()).setCellStyle(yellowStyle);
			row.getCell(IndiceExcel.PRECIOCLIENTE.getPos()).setCellStyle(yellowStyle);
		}
	}
	
	private ArrayList<Integer> buscarEnElSheetLasCoordenadasDeLasOperaciones(Sheet sheet,ArrayList<OperacionReporteDTO> operacionesQueCasiSonIgualesList){
		ArrayList<Integer> posicionesDeLasRowAPintarDeAmarillo = new ArrayList<Integer>();
		
		for(OperacionReporteDTO operacion: operacionesQueCasiSonIgualesList ) {
			buscarYAgregarPosDeLasRowsAList(sheet,operacion,posicionesDeLasRowAPintarDeAmarillo);
		}
		
		
		return posicionesDeLasRowAPintarDeAmarillo;
	}
	
	private void buscarYAgregarPosDeLasRowsAList(Sheet sheet,OperacionReporteDTO operacion,ArrayList<Integer> posicionesDeLasRowAPintarDeAmarillo) {
		
		for (Row row : sheet) {
			if(UtilComparacioDeOperaciones.sonIgualesSacandoLaFecha(row,operacion)) {
				posicionesDeLasRowAPintarDeAmarillo.add(row.getRowNum());
			}
		}
	}
	
	private ArrayList<OperacionReporteDTO> compararOperacionesQueSonCasiIguales(ArrayList<OperacionReporteDTO> contextList, ArrayList<OperacionReporteDTO> alycList) {
		ArrayList<OperacionReporteDTO> operacionesQueCasiSonIgualesList = new ArrayList<OperacionReporteDTO>();
	
		for(OperacionReporteDTO operacion: contextList) {
			if(tieneEstaOperacionYSonCasiIgualesSacandoLaFecha(alycList,operacion)) {
				operacionesQueCasiSonIgualesList.add(operacion);
			}
		}
		
		return operacionesQueCasiSonIgualesList;
	}
	
	private boolean tieneEstaOperacionYSonCasiIgualesSacandoLaFecha (ArrayList<OperacionReporteDTO> alycList,OperacionReporteDTO operacionBuscada) {
		boolean verificacion = false;
		int i=0;
		
		while (i<alycList.size() && !verificacion) {
			OperacionReporteDTO operacionActual= alycList.get(i);
			
			if(UtilComparacioDeOperaciones.compararOperacionesSinLaFecha(operacionActual,operacionBuscada)) {
				verificacion=true;
			}else {
				i++;
			}
		}
		
		return verificacion;
	};
	
	private void multiplicarPreciosX100SiEsNecesario (Workbook workbook, boolean hayQueMultiplicar)throws Exception {
		if(hayQueMultiplicar) {
			
			Sheet sheet = workbook.getSheetAt(primeraHoja);
			int indicePrecioMesa = obtenerIndiceColumna(sheet, WConstant.PRECIO_MESA);
			int indicePrecioCliente = obtenerIndiceColumna(sheet, WConstant.PRECIO_CLIENTE);
			
			multiplicarPreciosX100PorColumna(sheet,indicePrecioMesa);
			multiplicarPreciosX100PorColumna(sheet,indicePrecioCliente);

		}
		
		
	}
	
	private void multiplicarPreciosX100PorColumna (Sheet sheet, int posColumna)  {
		
		double valorCelda=0.0;
		double precioFinalCelda=0.0;
		
		for (Row row : sheet) {
			if (row.getRowNum() != 0) {
				try {
					Cell cell = row.getCell(posColumna);

					 if (cell.getCellType() == CellType.NUMERIC) {
						valorCelda = cell.getNumericCellValue();
					}

					precioFinalCelda = valorCelda*100; 
					
					cell.setCellValue(precioFinalCelda);

				} catch (Exception e) {
					System.out.println("ERROR en (normalizarPreciosPorColumna) : " + e.getMessage());
			    }
			}
			

		}
	}
	
	
	private void imprimirEncabezado(Sheet sheet) {
	    System.out.println("IMPRIMIENDO ENCABEZADO");
	    System.out.println("IMPRIMIENDO ENCABEZADO");

	    Row row = sheet.getRow(0); // Obtener la primera fila (encabezado)
	    if (row != null) {
	        StringBuilder rowOutput = new StringBuilder();

	        for (Cell cell : row) {
	            rowOutput.append(cell.getStringCellValue()).append("\t");
	        }

	        System.out.println(rowOutput.toString().trim()); // Imprimir el encabezado
	    } else {
	        System.out.println("Encabezado no encontrado (fila vacía)");
	    }
	}
	
	private void iterarSheet(Sheet sheet) {
	    System.out.println("Iterando el sheet...");
	    
	    // Formateador de fecha
	    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

	    for (Row row : sheet) {
	        if (row != null) {
	            StringBuilder rowOutput = new StringBuilder();

	            for (Cell cell : row) {
	                if (cell == null || cell.getCellType() == CellType.BLANK) {
	                    rowOutput.append("BLANCA\t");
	                } else {
	                    switch (cell.getCellType()) {
	                        case NUMERIC:
	                            if (DateUtil.isCellDateFormatted(cell)) {
	                                // Muestra la fecha con el formato especificado
	                                rowOutput.append(dateFormat.format(cell.getDateCellValue())).append("\t");
	                            } else {
	                                rowOutput.append(cell.getNumericCellValue()).append("\t");
	                            }
	                            break;
	                        case STRING:
	                            rowOutput.append(cell.getStringCellValue()).append("\t");
	                            break;
	                        case BOOLEAN:
	                            rowOutput.append(cell.getBooleanCellValue()).append("\t");
	                            break;
	                        case FORMULA:
	                            rowOutput.append(cell.getCellFormula()).append("\t");
	                            break;
	                        default:
	                            rowOutput.append("?\t"); // Para otros tipos desconocidos
	                            break;
	                    }
	                }
	            }
	            
	            System.out.println(rowOutput.toString().trim()); // Imprime la fila completa como una línea
	        }
	    }
	}


	private void  normalizarPrecios(Workbook workbook) throws Exception {
		Sheet sheet = workbook.getSheetAt(primeraHoja);
		int indicePrecioMesa = obtenerIndiceColumna(sheet, WConstant.PRECIO_MESA);
		int indicePrecioCliente = obtenerIndiceColumna(sheet, WConstant.PRECIO_CLIENTE);

		normalizarPreciosPorColumna(sheet, indicePrecioMesa);
		normalizarPreciosPorColumna(sheet, indicePrecioCliente);
		
       
	}

	private void normalizarPreciosPorColumna(Sheet sheet, int posColumna) throws Exception {

		String valorCelda = "";

		for (Row row : sheet) {

			if (row.getRowNum() != 0) {
				try {
					Cell cell = row.getCell(posColumna);

					
					
					if (cell.getCellType() == CellType.STRING) {
						valorCelda = cell.getStringCellValue();
					} else if (cell.getCellType() == CellType.NUMERIC) {
						valorCelda = String.valueOf(cell.getNumericCellValue());
					}

					valorCelda = valorCelda.trim().replace("$", "").replace(".", "").replace(",", ".").replace("€", "");

					Double precioNormalizado = Double.parseDouble(valorCelda);
					cell.setCellValue(precioNormalizado);

				} catch (Exception e) {
					System.out.println("ERROR en (normalizarPreciosPorColumna) : " + e.getMessage());
			  }

			}
			
		}

	}

	private void agregarEncabezado(Sheet sheet, int posRow) {
		// Row header = sheet.getRow(0);
		
		crearRowSiNoExiste(sheet,posRow);
		Row row = sheet.getRow(posRow);

		// row.createCell(0).setCellValue("");
		row.createCell(IndiceExcel.FECHA.getPos()).setCellValue(WConstant.FECHA_OPERACION);
		row.createCell(IndiceExcel.ESPECIE.getPos()).setCellValue(WConstant.ESPECIE);
		row.createCell(IndiceExcel.CANTIDADVN.getPos()).setCellValue(WConstant.CANTIDAD_V_N);
		row.createCell(IndiceExcel.PRECIOMESA.getPos()).setCellValue(WConstant.PRECIO_MESA);
		row.createCell(IndiceExcel.PRECIOCLIENTE.getPos()).setCellValue(WConstant.PRECIO_CLIENTE);

	}
	
	private void agregarEncabezadoConEspacio(Sheet sheet, int posRow,int espacio) {
		// Row header = sheet.getRow(0);
		
		crearRowSiNoExiste(sheet,posRow);
		Row row = sheet.getRow(posRow);

		// row.createCell(0).setCellValue("");
		row.createCell(IndiceExcel.FECHA.getPos()+espacio).setCellValue(WConstant.FECHA_OPERACION);
		row.createCell(IndiceExcel.ESPECIE.getPos()+espacio).setCellValue(WConstant.ESPECIE);
		row.createCell(IndiceExcel.CANTIDADVN.getPos()+espacio).setCellValue(WConstant.CANTIDAD_V_N);
		row.createCell(IndiceExcel.PRECIOMESA.getPos()+espacio).setCellValue(WConstant.PRECIO_MESA);
		row.createCell(IndiceExcel.PRECIOCLIENTE.getPos()+espacio).setCellValue(WConstant.PRECIO_CLIENTE);

	}

	private Workbook generarExcelConLasDisparidades(LinkedHashMapDeOperaciones contextoMap,
			LinkedHashMapDeOperaciones alycMap,AlycsEnum alycEnum) throws IOException {
		Workbook workbook = new XSSFWorkbook(); // Creamos el archivo Excel
		Sheet sheet = workbook.createSheet("Dispariedades");
		int posRow = 0;
		String msgSiSeTuvoQueMultiplicarX100ElValorDeLasOperaciones= generarMsgParaPreciosX100(alycEnum.hayQueMultiplicarValores());

		agregarSeparador(sheet, posRow++, "Operaciones de Contexto, a REVISAR"+msgSiSeTuvoQueMultiplicarX100ElValorDeLasOperaciones);
		agregarEncabezado(sheet, posRow++);
		posRow = contextoMap.llenarSheet(sheet, posRow++);

		sheet.createRow(posRow++);
		sheet.createRow(posRow++);

		agregarSeparador(sheet, posRow++, "Operaciones de "+ alycEnum.getNombre()+ ", a REVISAR");
		agregarEncabezado(sheet, posRow++);
		posRow = alycMap.llenarSheet(sheet, posRow++);

		System.out.println("MOSTRANDO EXCEL FINAL DE LAS DISPARIEDADES");
		System.out.println("MOSTRANDO EXCEL FINAL DE LAS DISPARIEDADES");
		System.out.println("MOSTRANDO EXCEL FINAL DE LAS DISPARIEDADES");

		try {
			for (Row row : sheet) {
				for (Cell cell : row) {
					switch (cell.getCellType()) {
					case STRING:
						System.out.print(cell.getStringCellValue() + "\t");
						break;
					case NUMERIC:
						System.out.print(cell.getNumericCellValue() + "\t");
						break;
					case BOOLEAN:
						System.out.print(cell.getBooleanCellValue() + "\t");
						break;
					case FORMULA:
						System.out.print(cell.getCellFormula() + "\t");
						break;
					case BLANK:
						System.out.print(" \t");
						break;
					case ERROR:
						System.out.print("ERROR\t");
						break;
					default:
						System.out.print("UNKNOWN\t");
						break;
					}
				}
				System.out.println(); // Nueva línea después de cada fila
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Cerrar el Workbook para liberar recursos
		return workbook; // Devolver los bytes

	}

	private String generarMsgParaPreciosX100 (boolean verificacion) {
		return (verificacion) ? "  ** LOS PRECIOS SE HAN MULTIPLICADO X100 para CONTEXTO **":"";
	}
	
	public byte[] convertirAVectorDeBytes(Workbook workbook) throws IOException {
		ByteArrayOutputStream bos = new ByteArrayOutputStream();
		workbook.write(bos);
		workbook.close();
		return bos.toByteArray();
	}

	private void agregarSeparador(Sheet sheet, int posRow, String msg) {
		Row encabezado = sheet.createRow(posRow);
		encabezado.createCell(0).setCellValue(msg);
	}

	private void compararOperaciones(LinkedHashMapDeOperaciones contextoMap, LinkedHashMapDeOperaciones alycMap) {
		contextoMap.comparar(alycMap);
	}
	
	private void compararOperaciones(LinkedHashMapDeOperaciones contextoMap, LinkedHashMapDeOperaciones alycMap,ArrayList<OperacionReporteDTO> contextoOperacionesOK,ArrayList<OperacionReporteDTO> alycOperacionesOK) {
		contextoMap.comparar(alycMap,contextoOperacionesOK,alycOperacionesOK);
	}


	// Encarga de convertir el formato del Reporte de la Alyc al formato Estandar
	// (el formato de contexto es el formato estandar)
	private Workbook estandarizarReporte(Workbook alycReporte, Plantilla plantilla, int año, Alyc alyc)
			throws Exception {

		Workbook AlycExcelConNombreDeColumnasAdaptado = null;
		Workbook AlycExcelReporteEstandarizadoCompleto = null;

		System.out.println("MOSTRANDO LA PLANTILLA");
		System.out.println("MOSTRANDO LA PLANTILLA");
		System.out.println("MOSTRANDO LA PLANTILLA");

		System.out.println(plantilla);

		try {

			AlycExcelConNombreDeColumnasAdaptado = ConverterAFormatoEstandar.cambiarNombreColumnas(alycReporte,
					plantilla);
			AlycExcelReporteEstandarizadoCompleto = adaptarFechasAFormatoEstandarDeSerNecesario(
					AlycExcelConNombreDeColumnasAdaptado, año);

			if (AlycExcelReporteEstandarizadoCompleto == null)
				throw new Exception("fallo con la adaptacion del reporte");

		} catch (Exception e) {
			throw new Exception("hubo un error al estandarizar el Reporte de la Alyc: " + e.getMessage());
		}

		/*
		 * Sheet sheet=AlycExcelReporteEstandarizadoCompleto.getSheetAt(0);
		 * 
		 * System.out.println("iterando el excel YA ESTANDARIZADOO");
		 * System.out.println("iterando el excel YA ESTANDARIZADOO");
		 * System.out.println("iterando el excel YA ESTANDARIZADOO");
		 * System.out.println("iterando el excel YA ESTANDARIZADOO");
		 * 
		 * Row rowsito = sheet.getRow(0);
		 * 
		 * for (Cell cell: rowsito) { System.out.println("encabezado: "+cell); }
		 * 
		 * for(Row row: sheet) {
		 * 
		 * System.out.println("CELDA: "+row.getCell(0));
		 * 
		 * }
		 */

		return AlycExcelReporteEstandarizadoCompleto;
	}

	// Sheet TODO
	private Workbook adaptarFechasAFormatoEstandarDeSerNecesario(Workbook reporteAlyc, int año) throws Exception {

		for (int i = 0; i < reporteAlyc.getNumberOfSheets(); i++) {

			Sheet sheet = reporteAlyc.getSheetAt(i);

			sheet = FechaConverter.adaptarDeSerNecesarioLasFechas(sheet, año);
		}

		return reporteAlyc;
	}

	// Método para procesar un reporte y convertirlo a un mapa de operaciones
	private LinkedHashMapDeOperaciones procesarReporte(Workbook reportePath) throws IOException {

		if (reportePath == null)
			throw new IOException("no se puede ejecutar 'procesarReporte(Workbook)' porque lo que se recibio es null");

		Sheet sheet = reportePath.getSheetAt(primeraHoja);
		System.out.println(" EJECUTANDO METODO 'procesarReporte'  ");
		return convertirSheetAMap(sheet);
	}

	// Método para convertir un Sheet en un LinkedHashMapDeOperaciones
	private LinkedHashMapDeOperaciones convertirSheetAMap(Sheet sheet) {

		LinkedHashMapDeOperaciones operacionesMap = new LinkedHashMapDeOperaciones();
		System.out.println(" EJECUTANDO METODO 'convertirSheetAMap' 1 ");
		HashMap<String, Integer> encabezado = getEncabezado(sheet);
		System.out.println(" EJECUTANDO METODO 'convertirSheetAMap' 2 ");

		System.out.println(" EJECUTANDO METODO 'convertirSheetAMap'  bucle ");
		for (int i = 1; i <= sheet.getLastRowNum(); i++) {

			Row row = sheet.getRow(i);
			if (row != null) {
				OperacionReporteDTO operacion = OperacionReporteDTOConverter.rowToDTO(row, encabezado);
				operacionesMap.agregar(operacion); // Usar el método agregar de LinkedHashMapDeOperaciones
			}
		}

		return operacionesMap;
	}
	
	

	private void validarSiAlgunoEsNull(byte[] contextoReporte, byte[] alycReporte, Alyc alyc) throws RuntimeException {

		if (contextoReporte == null)
			throw new RuntimeException("Reporte de Contexto es NULL");
		if (alycReporte == null)
			throw new RuntimeException("Reporte de la ALYC es NULL");
		if (alyc == null)
			throw new RuntimeException("el alyc es NULL");
		if (alyc.getPlantilla() == null)
			throw new RuntimeException(" el ALYC no tiene PLANTILLA");

	}

	private HashMap<String, Integer> getEncabezado(Sheet sheet) {
		Row encabezado = sheet.getRow(0);

		HashMap<String, Integer> posicionesColumnas = new HashMap<>();
		String nombreColumna = null;

		// if(encabezado==null|| sheet.getPhysicalNumberOfRows() == 0) throw new
		// IllegalArgumentException("ENCABEZADO VACIOOOO");

		for (Cell cell : encabezado) {

			if (cell.getCellType() == CellType.STRING) {
				nombreColumna = cell.getStringCellValue();
			} else if (cell.getCellType() == CellType.NUMERIC) {
				nombreColumna = String.valueOf(cell.getNumericCellValue());
			}

			// Imprimir el valor de la columna para depuración
			System.out.println("Nombre de columna encontrado: " + nombreColumna);

			if (WConstant.ESPECIE.trim().equalsIgnoreCase(nombreColumna.trim())) {
				posicionesColumnas.put(WConstant.ESPECIE, cell.getColumnIndex());

			} else if (WConstant.FECHA_OPERACION.trim().equalsIgnoreCase(nombreColumna.trim())) {
				posicionesColumnas.put(WConstant.FECHA_OPERACION, cell.getColumnIndex());

			} else if (WConstant.CANTIDAD_V_N.trim().equalsIgnoreCase(nombreColumna.trim())) {
				posicionesColumnas.put(WConstant.CANTIDAD_V_N, cell.getColumnIndex());

			} else if (WConstant.PRECIO_MESA.trim().equalsIgnoreCase(nombreColumna.trim())) {
				posicionesColumnas.put(WConstant.PRECIO_MESA, cell.getColumnIndex());

			} else if (WConstant.PRECIO_CLIENTE.trim().equalsIgnoreCase(nombreColumna.trim())) {
				posicionesColumnas.put(WConstant.PRECIO_CLIENTE, cell.getColumnIndex());

			}
		}
		return posicionesColumnas;
	}

	// Encuentra el índice de una columna basada en su nombre en la primera fila
	// (row 0)
	public static int obtenerIndiceColumna(Sheet sheet, String nombreColumna) {

		Row headerRow = sheet.getRow(0);
		int cellIndex = 0;
		int numeroDeCeldas = headerRow.getPhysicalNumberOfCells();
		int indiceBuscado = -1;

		while (cellIndex < numeroDeCeldas && indiceBuscado == -1) {
			Cell cell = headerRow.getCell(cellIndex);
			String headerName = formatter.formatCellValue(cell);

			if (nombreColumna.trim().equalsIgnoreCase(headerName.trim())) {
				indiceBuscado = cell.getColumnIndex();
			}

			cellIndex++;
		}

		if (indiceBuscado == -1)
			throw new RuntimeException("No se encontró la columna '" + nombreColumna + "' en el reporte.");

		return indiceBuscado;
	}
}
	
	/*public byte[] compararReportes2(byte[] contextoReporte, byte[] alycReporte, Long idAlyc) throws Exception {

	Alyc alyc = alycRepository.getReferenceById(idAlyc);
	LinkedHashMapDeOperaciones contextoOperacionesMap = null;
	LinkedHashMapDeOperaciones alycOperacionesMap = null;
	Workbook AlycExcelModificado = null;
	int año = -1;
	byte[] excelFinalConLasDispariedades = null;

	try (InputStream contextoInputStream = new ByteArrayInputStream(contextoReporte);
			InputStream alycInputStream = new ByteArrayInputStream(alycReporte)) {

		validarSiAlgunoEsNull(contextoReporte, alycReporte, alyc);

		Workbook contextoWorkbook = new XSSFWorkbook(contextoInputStream);
		Workbook alycWorkbook = new XSSFWorkbook(alycInputStream);

		System.out.println(" EJECUTANDO PROCESAR REPORTE");
		System.out.println(" EJECUTANDO PROCESAR REPORTE");
		// se mete a un map(ordenado por fecha) cada fila del excel
		// se meten los datos del reporteContexto a un map

		System.out.println(" EJECUTANDO PROCESAR REPORTE PARA  CONTEXTO");
		System.out.println(" EJECUTANDO PROCESAR REPORTE PARA  CONTEXTO");
		System.out.println(" EJECUTANDO PROCESAR REPORTE PARA  CONTEXTO");

		contextoOperacionesMap = procesarReporte(contextoWorkbook);
		System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");

		contextoOperacionesMap.mostrarHashMap();
		System.out.println("--------------------------------------------------------");
		System.out.println("--------------------------------------------------------");

		// obtenemos el año actual de las operaciones, el cual usaremos en el siguiente
		// metodo para agregarle la fecha si asi lo precisare
		año = contextoOperacionesMap.obtenerAñoDePrimeraFecha();

		// se adapta el formato de la alyc al formato estandar(el formato de contexto es
		// el formato estandar)
		AlycExcelModificado = estandarizarReporte(alycWorkbook, alyc.getPlantilla(), año, alyc);

		System.out.println(" EJECUTANDO PROCESAR REPORTE PARA LA ALYC");
		System.out.println(" EJECUTANDO PROCESAR REPORTE PARA LA ALYC");
		System.out.println(" EJECUTANDO PROCESAR REPORTE PARA LA ALYC");

		System.out.println("---------------------------------------");
		// se meten los datos del AlycExcelModificado a un map
		alycOperacionesMap = procesarReporte(AlycExcelModificado);
		alycOperacionesMap.mostrarHashMap();
		System.out.println("---------------------------------------");

		System.out.println(" COMPARANDO LOS REPORTES");
		System.out.println(" COMPARANDO LOS REPORTES");
		System.out.println(" COMPARANDO LOS REPORTES");
		System.out.println(" COMPARANDO LOS REPORTES");

		// se comparan los reportes y se eliman las coincidencias, solo quedaran las
		// disparariedades en cada reporte respectivo
		compararOperaciones(contextoOperacionesMap, alycOperacionesMap);

		System.out.println(" GENERAR EXCEL CON DISPARIEDADES");
		System.out.println(" GENERAR EXCEL CON DISPARIEDADES");
		System.out.println(" GENERAR EXCEL CON DISPARIEDADES");
		// se recorren las disparariedades correspondientes y se agregan a un excel
		// final
		excelFinalConLasDispariedades = generarExcelConLasDisparidades(contextoOperacionesMap, alycOperacionesMap);

		return excelFinalConLasDispariedades;

		// chequear este return TODO
		/*
		 * return ResponseDTO.<byte[]>builder() .entity(excelFinalConLasDispariedades)
		 * // El archivo Excel .ok(true) .log("Comparación realizada con éxito")
		 * .build();
		 

	} catch (Exception ex) {
		System.err.println("al comparar Reportes hubo un error: " + ex.getMessage());

		// chequear este return TODO
		return excelFinalConLasDispariedades;
	}
}*/

