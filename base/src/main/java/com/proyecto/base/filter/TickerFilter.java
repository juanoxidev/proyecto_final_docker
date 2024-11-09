package com.proyecto.base.filter;

import java.util.HashMap;

import org.springframework.util.StringUtils;

import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.dto.TickerDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.filter.sentencia.Sentencia;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Slf4j
public class TickerFilter implements IFilter{
	private TickerDTO criteriosDeBusqueda;
	private boolean conAnd = false;
	
	public TickerFilter(TickerDTO criteriosDeBusqueda) {
		this.criteriosDeBusqueda = criteriosDeBusqueda;
	}

	@Override
	public Sentencia sentenciaHQL() {
		String sql = " from Ticker t ";
		String orden =" order by id ";
		Sentencia sentencia = completarSentencia(sql);
		sentencia.append(orden);
		return sentencia;
	}

	private String conectorSql() {
		String resp = null;
		if(conAnd){
			resp = " and ";
		}else{
			resp = " where ";
			conAnd = true;
		}
		return resp;
	}

	private Sentencia completarSentencia(String sql) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		
		StringBuffer sqlAux = new StringBuffer(sql);
		
		Sentencia sentencia = new Sentencia();
		conAnd = false;
		
		if (getCriteriosDeBusqueda().getTickerId() != null) {
			sqlAux.append(conectorSql());
			sqlAux.append("t.id =(:ticker)");
			parametros.put("ticker", getCriteriosDeBusqueda().getTickerId());
			
		}
		
		if (StringUtils.hasText(getCriteriosDeBusqueda().getTickerName())) {
			sqlAux.append(conectorSql());
			sqlAux.append("t.nombre =(:nombre)");
			parametros.put("nombre", getCriteriosDeBusqueda().getTickerName());
			
		}
		
		if (StringUtils.hasText(getCriteriosDeBusqueda().getTickerDescripcion())) {
			sqlAux.append(conectorSql());
			sqlAux.append("t.descripcion =(:descripcion)");
			parametros.put("descripcion", getCriteriosDeBusqueda().getTickerDescripcion());
			
		}
		
		if (getCriteriosDeBusqueda().getEstado() != null && getCriteriosDeBusqueda().getEstado() != EstadosEnum.TODOS) {
			sqlAux.append(conectorSql());
			sqlAux.append("t.estado =(:estado)");
			parametros.put("estado", getCriteriosDeBusqueda().getEstado());
			
		}
		
		
		log.debug("######## SENTENCIA HQL ########## : {}", sqlAux.toString());
		sentencia.setSql(sqlAux.toString());
		sentencia.setParametros(parametros);
		return sentencia;
	}

	@Override
	public Sentencia sentenciaHQLCount() {
		String sql = "select count(*) from Ticker t ";
		
		return completarSentencia(sql);
	}

}
