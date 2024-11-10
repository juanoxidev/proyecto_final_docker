package com.proyecto.base.service;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyecto.base.constantes.WConstant;
import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.datatable.IDatatable;
import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.enums.IndiceExcel;
import com.proyecto.base.enums.IndiceExcelOperacionesDesdeHasta;
import com.proyecto.base.enums.TipoOperacion;
import com.proyecto.base.excepcion.BaseException;
import com.proyecto.base.filter.OperacionFilter;
import com.proyecto.base.model.Alyc;
import com.proyecto.base.model.Auditoria;
import com.proyecto.base.model.Moneda;
import com.proyecto.base.model.Operacion;
import com.proyecto.base.model.Plazo;
import com.proyecto.base.model.Ticker;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.AlycRepository;
import com.proyecto.base.repository.MonedaRepository;
import com.proyecto.base.repository.OperacionRepository;
import com.proyecto.base.repository.PlazoRepository;
import com.proyecto.base.repository.TickerRepository;
import com.proyecto.base.util.UtilesFechas;

import lombok.extern.slf4j.Slf4j;


@Slf4j
@Service
public class OperacionServiceImpl implements OperacionService, IDatatable<Operacion>{

	@Autowired
	private OperacionRepository operacionRepository;
	
	@Autowired
	private MonedaRepository monedaRepository;
	
	@Autowired
	private AlycRepository alycRepository;
	
	@Autowired
	private TickerRepository tickerRepository;
	
	
	@Autowired
	private PlazoRepository plazoRepository;
	
	@Autowired
	private ComparacionReportesService comparacionReportesService;
	 
	@Transactional
	public ResponseDTO<OperacionDTO> altaOperacion (OperacionDTO dto, Usuario usuarioAuditoria){
		ResponseDTO<OperacionDTO> resp = new ResponseDTO<OperacionDTO>();
		
		
		try {
			validarOperacion(dto);
			
			Moneda moneda = monedaRepository.getReferenceById(dto.getMoneda());
			Alyc alyc = alycRepository.getReferenceById(dto.getAlyc());
			Ticker ticker = tickerRepository.getReferenceById(dto.getTicker());
			Plazo plazo = plazoRepository.getReferenceById(dto.getPlazo());
			
			Operacion operacion = Operacion.builder()
								  .alyc(alyc)
								  .auditoria(Auditoria.crearAuditoria(usuarioAuditoria))
								  .cantidadNominal(dto.getCantidadNominal())
								  .fecha(new Date())
								  .moneda(moneda)
								  .ticker(ticker)
								  .cliente(dto.getCliente())
								  .precioMesa(dto.getPrecioMesa())
								  .precioCliente(dto.getPrecioCliente())
								  .tipoOperacion(dto.getTipoOperacion())
								  .operado(dto.getCantidadNominal()*dto.getPrecioCliente())
								  .comision(((dto.getCantidadNominal()*(dto.getPrecioCliente() - dto.getPrecioMesa()))*(alyc.getPorcentajeComision()/100)))
								  .plazo(plazo)
								  .estado(EstadosEnum.ACTIVO)
								  .build();
			
			Operacion operacionBD = operacionRepository.save(operacion);

	        log.info("Se ha creado la operacion: #{}",operacionBD);
	        resp.setOk(true);
	        resp.setLog("Se creo la operacion correctamente.");
			
		} catch (Exception e) {
			log.info(e.getMessage());
			throw new BaseException(e.getMessage());
		}
			
		return resp;
	}
	

	@Override
	public DataTablesResponse<OperacionDTO> getRespuestaDatatable(DataTablesRequestForm<OperacionDTO> dtRequest) {
		int start = dtRequest.getStart();
		int limit = dtRequest.getLength();
		OperacionDTO criteriosDeBusqueda = dtRequest.getFormBusqueda();
		OperacionFilter filter = new OperacionFilter(criteriosDeBusqueda);
		List<Operacion> operacionBD = this.operacionRepository.list(filter.sentenciaHQL(), start, limit);
		List<OperacionDTO> operacionesDTO = this.getDTOs(OperacionDTO::crearDTOBusqueda, operacionBD);
		Long cantidadRegistros = this.operacionRepository.count(filter.sentenciaHQLCount());
		return this.getRespuestaDatatable(operacionesDTO, cantidadRegistros);
	}
	
	public void validarOperacion(OperacionDTO dto) throws Exception {
		if(dto==null) throw new Exception ("La operacion no pudo ser validada correctamente");
	}
	
	@Override
	@Transactional
	public ResponseDTO<OperacionDTO> editarOperacion(OperacionDTO formCreacion, Usuario usuario) {
		ResponseDTO<OperacionDTO> resp = new ResponseDTO<OperacionDTO>();

		Integer cantidadNominalForm = formCreacion.getCantidadNominal();
		Double precioMesaForm = formCreacion.getPrecioMesa();
		Double precioClienteForm = formCreacion.getPrecioCliente();
		TipoOperacion tipoOperacionForm = formCreacion.getTipoOperacion();
		Long plazoId = formCreacion.getPlazo();
		//Date fechaForm = formCreacion.getFecha();
		
		Long monedaId = formCreacion.getMoneda();
		Long tickerId = formCreacion.getTicker();
		Long alycId   = formCreacion.getAlyc();
		

		boolean cambio = false;
		boolean recalcular = false;
		
			
			Operacion operacionBD = operacionRepository.getReferenceById(formCreacion.getId());
			
			if(cantidadNominalForm != null && !operacionBD.esMiCantidadNominal(cantidadNominalForm)) {
				operacionBD.setCantidadNominal(cantidadNominalForm);
				cambio = true;
				recalcular = true;
			}	
			
			if(precioMesaForm != null && !operacionBD.esMiPrecioMesa(precioMesaForm)) {
				operacionBD.setPrecioMesa(precioMesaForm);
				cambio = true;
				recalcular = true;
			}	
			
			if(precioClienteForm != null && !operacionBD.esMiPrecioCliente(precioClienteForm)) {
				operacionBD.setPrecioCliente(precioClienteForm);
				cambio = true;
				recalcular = true;
			}	
			
			if(tipoOperacionForm != null && !operacionBD.esMiTipoOperacion(tipoOperacionForm)) {
				cambio = true;
				operacionBD.setTipoOperacion(tipoOperacionForm);
			}	
			
			if(!operacionBD.esMiPlazo(plazoId)) {
				Plazo plazoBD = plazoRepository.getReferenceById(plazoId);
				operacionBD.setPlazo(plazoBD);
				cambio = true;
			}
			

				if(!operacionBD.esMiAlyc(alycId)) {
					Alyc alycBD = alycRepository.getReferenceById(alycId);
					operacionBD.setAlyc(alycBD);
					cambio = true;
					recalcular = true;
				}	
		    
				if(!operacionBD.esMiTicker(tickerId)) {
					Ticker tickerBD = tickerRepository.getReferenceById(tickerId);
					operacionBD.setTicker(tickerBD);
					cambio = true;
				}	
				
				if(!operacionBD.esMiMoneda(monedaId)) {
					Moneda monedaBD = monedaRepository.getReferenceById(monedaId);
					operacionBD.setMoneda(monedaBD);
					cambio = true;
				}	
				
				recalcularOperacion(recalcular, operacionBD);
			
			//verificarSiCambioFechaOperacion(fechaForm,operacionBD,cambio);
		    
		
			definirResp(cambio,resp,operacionBD,usuario);
			
		return resp;
	}
	
	private void recalcularOperacion(boolean recalcular, Operacion op) {
		if (recalcular) {
			op.setOperado(op.getCantidadNominal()*op.getPrecioCliente());
			op.setComision(((op.getCantidadNominal()*(op.getPrecioCliente() - op.getPrecioMesa()))*(op.getAlyc().getPorcentajeComision()/100)));
		}
	}


	private void definirResp(boolean cambio,ResponseDTO<OperacionDTO> resp,Operacion operacionBD, Usuario user) {
		
		if (cambio) {
			Auditoria.modificar(operacionBD.getAuditoria(), user );
			operacionRepository.save(operacionBD);
			resp.setLog(String.format("Se ha modificado la operacion #%d ", operacionBD.getId()));	 		 
		} else {
			resp.setLog(String.format("No se han enviado modificaciones para la operacion #%d", operacionBD.getId())); 
		}
		
		resp.setOk(cambio);
	}
	

	
	

	
//	private void verificarSiCambioFechaOperacion (Date fechaForm, Operacion operacionBD,boolean cambio) {	
//		if(!operacionBD.esMiFechaOperacion(fechaForm)) {
//			operacionBD.setFecha(fechaForm);
//			cambio = true;
//		}	
//	}
	
	private void verificarSiCambioAlyc (Alyc alycForm, Operacion operacionBD,boolean cambio) {	
		if(!operacionBD.esMiAlyc(alycForm)) {
			operacionBD.setAlyc(alycForm);
			cambio = true;
		}	
	}
	
	private void verificarSiCambioTicker (Ticker tickerForm, Operacion operacionBD,boolean cambio) {	
		if(!operacionBD.esMiTicker(tickerForm)) {
			operacionBD.setTicker(tickerForm);
			cambio = true;
		}	
	}
	
	private void verificarSiCambioDeMoneda (Moneda monedaForm, Operacion operacionBD,boolean cambio) {	
		if(!operacionBD.esMiMoneda(monedaForm)) {
			operacionBD.setMoneda(monedaForm);
			cambio = true;
		}	
	}


	@Override
	@Transactional
	public byte[] getOperacionesSegunAlycEntreFechas(OperacionDTO operacionDTO)  {
		List<Operacion> operacionesBD = operacionRepository.getOperacionesDeAlycEntreFechas(operacionDTO.getAlyc(),
				operacionDTO.getFechaDesde(), operacionDTO.getFechaHasta());
		log.info("Se exportaron {} operaciones", operacionesBD.size());

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("operaciones");
		int i = 0;
		byte[] resultado = null;

		
		agregarEncabezado(sheet,i++);
	   
		
		for (Operacion operacion : operacionesBD) {
			Row row = sheet.createRow(i++);
			crearCeldas(row, operacion);
			setearValoresEnLasCeldas(row, operacion);

		}

		try {
			resultado = comparacionReportesService.convertirAVectorDeBytes(wb);
		}catch (Exception e) {
			resultado = null;
		}
		
		// metodo que retorna un excel, tenes la alyc y las operaciones que se
		// realizaron en el rango de fechas que seleccionaste.
		
		return resultado;
	}

	private void crearCeldas(Row row, Operacion operacion) {
		int cantAtributosOperacion = 20;
		
		for (int i = 0; i < cantAtributosOperacion; i++) {
			row.createCell(i);
		}
	}
	
	public void agregarEncabezado(Sheet sheet, int posRow) {
		// Row header = sheet.getRow(0);
		
		crearRowSiNoExiste(sheet,posRow);
		Row row = sheet.getRow(posRow);

		// row.createCell(0).setCellValue("");
		row.createCell(IndiceExcelOperacionesDesdeHasta.FECHA.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.FECHA.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.ALYC.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.ALYC.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.CLIENTE.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.CLIENTE.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.T_OPERACION.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.T_OPERACION.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.PLAZO.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.PLAZO.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.ESPECIE.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.ESPECIE.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.CANTIDADNOMINAL.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.CANTIDADNOMINAL.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.MONEDA.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.MONEDA.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.PRECIOMESA.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.PRECIOMESA.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.PRECIOCLIENTE.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.PRECIOCLIENTE.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.MONTOOPERADO.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.MONTOOPERADO.getNombre());
		row.createCell(IndiceExcelOperacionesDesdeHasta.MONTOCOMISION.getPos()).setCellValue(IndiceExcelOperacionesDesdeHasta.MONTOCOMISION.getNombre());
		
		
		
		
	}
	
	private void crearRowSiNoExiste(Sheet sheet, int pos) {
		if(sheet.getRow(pos)==null) {
			sheet.createRow(pos);
		}
	}
	
	private void setearValoresEnLasCeldas(Row row, Operacion operacion) {
		
		int comision = (int)(operacion.getPrecioMesa()-operacion.getPrecioCliente());
		comision = (comision<0) ? comision*-1:comision;
		
	    Date fecha = operacion.getFecha();
	    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yy");
	    String fechaFormateada = formatter.format(fecha.toInstant().atZone(ZoneId.systemDefault()).toLocalDate());

		row.getCell(IndiceExcelOperacionesDesdeHasta.FECHA.getPos()).setCellValue(fechaFormateada);
		
		row.getCell(IndiceExcelOperacionesDesdeHasta.ALYC.getPos()).setCellValue(operacion.getAlyc().getNombre());

		row.getCell(IndiceExcelOperacionesDesdeHasta.CLIENTE.getPos()).setCellValue(operacion.getCliente());

		row.getCell(IndiceExcelOperacionesDesdeHasta.T_OPERACION.getPos()).setCellValue(operacion.getTipoOperacion().getCodigo());

		row.getCell(IndiceExcelOperacionesDesdeHasta.PLAZO.getPos()).setCellValue(operacion.getPlazo().getNombre());

		row.getCell(IndiceExcelOperacionesDesdeHasta.ESPECIE.getPos()).setCellValue(operacion.getTicker().getNombre());
		
		row.getCell(IndiceExcelOperacionesDesdeHasta.CANTIDADNOMINAL.getPos()).setCellValue(operacion.getCantidadNominal());
		
		row.getCell(IndiceExcelOperacionesDesdeHasta.MONEDA.getPos()).setCellValue(operacion.getMoneda().getNombre());
		
		row.getCell(IndiceExcelOperacionesDesdeHasta.PRECIOMESA.getPos()).setCellValue(operacion.getPrecioMesa());
		
		row.getCell(IndiceExcelOperacionesDesdeHasta.PRECIOCLIENTE.getPos()).setCellValue(operacion.getPrecioCliente());
		
		row.getCell(IndiceExcelOperacionesDesdeHasta.MONTOOPERADO.getPos()).setCellValue(operacion.getOperado());

		row.getCell(IndiceExcelOperacionesDesdeHasta.MONTOCOMISION.getPos()).setCellValue(comision);

	}

	
	

}

	
	



