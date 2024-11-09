package com.proyecto.base.service;

import java.util.Date;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
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
import com.proyecto.base.converter.OperacionConverter;
import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.datatable.IDatatable;
import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.enums.IndiceExcelOperacionesDesdeHasta;
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
		Date fechaForm = formCreacion.getFecha();
		
		Long monedaId = formCreacion.getMoneda();
		Long tickerId = formCreacion.getTicker();
		Long alycId   = formCreacion.getAlyc();
		
		Alyc alycForm = alycRepository.getReferenceById(alycId);
		Moneda monedaForm = monedaRepository.getReferenceById(monedaId);
		Ticker tickerForm = tickerRepository.getReferenceById(tickerId);

		boolean cambio = false;
		
			
			Operacion operacionBD = operacionRepository.getReferenceById(formCreacion.getId());
			
			verificarSiCambioCantidadNominal(cantidadNominalForm,operacionBD,cambio);
			
		    verificarSiCambioPrecioMesa(precioMesaForm,operacionBD,cambio);
		    
		    verificarSiCambioPrecioCliente(precioClienteForm,operacionBD,cambio);
			
			verificarSiCambioFechaOperacion(fechaForm,operacionBD,cambio);
		    
			verificarSiCambioAlyc(alycForm,operacionBD,cambio);
			
			verificarSiCambioTicker(tickerForm,operacionBD,cambio);
			
			verificarSiCambioDeMoneda(monedaForm,operacionBD,cambio);
			
			definirResp(cambio,resp,operacionBD,usuario);
			
		return resp;
	}
	
	private void definirResp(boolean cambio,ResponseDTO<OperacionDTO> resp,Operacion operacionBD, Usuario user) {
		
		if (cambio) {
			Auditoria.modificar(operacionBD.getAuditoria(), user );
			operacionRepository.save(operacionBD);
			resp.setLog(String.format("Se ha modificado la operacion %s ", operacionBD));	 		 
		} else {
			resp.setLog(String.format("No se han enviado modificaciones para la operacion %s", operacionBD)); 
		}
		
		resp.setOk(cambio);
	}
	
	private void verificarSiCambioCantidadNominal (Integer cantidadNominalForm, Operacion operacionBD,boolean cambio) {	
		if(cantidadNominalForm != null && !operacionBD.esMiCantidadNominal(cantidadNominalForm)) {
			operacionBD.setCantidadNominal(cantidadNominalForm);
			cambio = true;
		}	
	}
	
	private void verificarSiCambioPrecioMesa (Double precioMesaForm, Operacion operacionBD,boolean cambio) {	
		if(precioMesaForm != null && !operacionBD.esMiPrecioMesa(precioMesaForm)) {
			operacionBD.setPrecioMesa(precioMesaForm);
			cambio = true;
		}	
	}
	
	private void verificarSiCambioPrecioCliente (Double precioClienteForm, Operacion operacionBD,boolean cambio) {	
		if(precioClienteForm != null && !operacionBD.esMiPrecioCliente(precioClienteForm)) {
			operacionBD.setPrecioCliente(precioClienteForm);
			cambio = true;
		}	
	}
	
	private void verificarSiCambioFechaOperacion (Date fechaForm, Operacion operacionBD,boolean cambio) {	
		if(!operacionBD.esMiFechaOperacion(fechaForm)) {
			operacionBD.setFecha(fechaForm);
			cambio = true;
		}	
	}
	
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
		log.info("La fecha desde es: {}", UtilesFechas.getFechaFormateada(operacionDTO.getFechaDesde()));
		log.info("La fecha hasta es: {}", UtilesFechas.getFechaFormateada(operacionDTO.getFechaHasta()));
		List<Operacion> operacionesBD = operacionRepository.getOperacionesDeAlycEntreFechas(operacionDTO.getAlyc(),
				operacionDTO.getFechaDesde(), operacionDTO.getFechaHasta());
		log.info("Se exportaron {} operaciones", operacionesBD.size());

		Workbook wb = new XSSFWorkbook();
		Sheet sheet = wb.createSheet("operaciones");
		int i = 0;
		byte[] resultado = null;

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
	
	private void setearValoresEnLasCeldas(Row row, Operacion operacion) {
		
		int comision = (int)(operacion.getPrecioMesa()-operacion.getPrecioCliente());
		comision = (comision<0) ? comision*-1:comision;
		
		row.getCell(IndiceExcelOperacionesDesdeHasta.FECHA.getPos()).setCellValue(operacion.getFecha());
		
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

	
	



