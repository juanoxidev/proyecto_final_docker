package com.proyecto.base.service;

import java.io.IOException;

import org.apache.poi.ss.usermodel.Workbook;

import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.ResponseDTO;

public interface ComparacionReportesService {

	public byte[] compararReportes(byte[] contextoReporte, byte[] alycReporte, Long nombreDeLaAlyc) throws Exception;

	public byte[] convertirAVectorDeBytes(Workbook wb) throws IOException;
	
	
	
}
