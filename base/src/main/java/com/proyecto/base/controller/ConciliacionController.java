package com.proyecto.base.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ContentDisposition;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.proyecto.base.datatable.DataTablesRequestForm;
import com.proyecto.base.datatable.DataTablesResponse;
import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.ConciliacionDTO;
import com.proyecto.base.dto.ResponseDTO;
import com.proyecto.base.model.Alyc;
import com.proyecto.base.repository.AlycRepository;
import com.proyecto.base.service.ComparacionReportesService;
import com.proyecto.base.service.ConciliacionService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Controller
@RequestMapping("/conciliacion")
public class ConciliacionController  extends DefaultController{
	
	@Autowired
	private ConciliacionService conciliacionService;
	
	@Autowired
	private ComparacionReportesService comparacionReportesService;
	
	@Autowired
	private AlycRepository alycRepository;
	
	@GetMapping("/bandeja")
	public String bandejaConciliacion(Model model) {
		List<Alyc> alycs = alycRepository.findAll();
		
		ConciliacionDTO form = new ConciliacionDTO();
		model.addAttribute("alycs", alycs);
		model.addAttribute("busquedaForm", form);
		return "views/conciliacion/bandeja";
		
	}

	
	@PostMapping(value = "/getAlycs")
	public @ResponseBody DataTablesResponse<AlycDTO> getUsuarios(
			@RequestBody DataTablesRequestForm<ConciliacionDTO> dtRequest) {
 

		DataTablesResponse<AlycDTO> dtResponse = new DataTablesResponse<>();


		dtResponse = conciliacionService.getRespuestaDatatable(dtRequest);

		
		return dtResponse;
	}
	
	
	// NO SE DESCARGA AUTOMATICAMENTE EL EXCEL RESULTANTE POR QUE ESTA ENVUELTO EN UN RESPONSEDTO
	// NO SE DESCARGA AUTOMATICAMENTE EL EXCEL RESULTANTE POR QUE ESTA ENVUELTO EN UN RESPONSEDTO
	// NO SE DESCARGA AUTOMATICAMENTE EL EXCEL RESULTANTE POR QUE ESTA ENVUELTO EN UN RESPONSEDTO
	
	/*@PostMapping("/comparar")
	public ResponseEntity<ResponseDTO<byte[]>> compararReportes(
	        @RequestParam("contextoReporte") MultipartFile contextoReporte,
	        @RequestParam("alycReporte") MultipartFile alycReporte,
	        @RequestParam("idAlyc") Long idAlyc) {

	    try {
	        byte[] contextoBytes = contextoReporte.getBytes();
	        byte[] alycBytes = alycReporte.getBytes();
	        byte[] excelResultado = comparacionReportesService.compararReportes(contextoBytes, alycBytes, idAlyc);

	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
	        headers.setContentDispositionFormData("attachment", "resultado_comparacion.xlsx");

	        ResponseDTO<byte[]> response = new ResponseDTO<>(excelResultado, true, "Operación exitosa", null);
	        return new ResponseEntity<>(response, headers, HttpStatus.OK);

	    } catch (Exception e) {
	        ResponseDTO<byte[]> response = new ResponseDTO<>(null, false, "Error al generar el archivo: " + e.getMessage(), null);
	        return new ResponseEntity<>(response, HttpStatus.INTERNAL_SERVER_ERROR);
	    }
	}*/
	
   
	// ESTE SI DESCARGA EL EXCEL RESULTANTE DIRECTAMENTE
	// ESTE SI DESCARGA EL EXCEL RESULTANTE DIRECTAMENTE
	// ESTE SI DESCARGA EL EXCEL RESULTANTE DIRECTAMENTE
	// ESTE SI DESCARGA EL EXCEL RESULTANTE DIRECTAMENTE
	
@PostMapping(value={"/comparar"}, consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
public ResponseEntity<byte[]> compararReportes(
		@RequestPart MultipartFile contextoReporte,
        @RequestPart MultipartFile alycReporte,
        @RequestParam(required=true) Long idAlyc) {
		
    try {

        byte[] contextoBytes = contextoReporte.getBytes();
        byte[] alycBytes = alycReporte.getBytes();	

        byte[] excelResultado = comparacionReportesService.compararReportes(contextoBytes, alycBytes, idAlyc);

 
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headers.setContentDisposition(ContentDisposition.builder("attachment").filename("reporte_comparacion.xlsx").build());


        return new ResponseEntity<>(excelResultado, headers, HttpStatus.OK);

    } catch (Exception e) {
    	e.printStackTrace();
        return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
}

	

}
