package com.proyecto.base.filter;


import java.util.HashMap;

import org.springframework.util.StringUtils;

import com.proyecto.base.dto.MonedaDTO;
import com.proyecto.base.enums.EstadosEnum;
import com.proyecto.base.filter.sentencia.Sentencia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Slf4j
public class MonedaFilter implements IFilter {
	private MonedaDTO criteriosDeBusqueda;
	private boolean conAnd = false;
	
	public MonedaFilter(MonedaDTO criteriosDeBusqueda) {
		this.criteriosDeBusqueda = criteriosDeBusqueda;
	}

	@Override
	public Sentencia sentenciaHQL() {
		String sql = " from Moneda m ";
		String orden =" order by id ";
		Sentencia sentencia = completarSentencia(sql);
		sentencia.append(orden);
		return sentencia;
	}

	private Sentencia completarSentencia(String sql) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		
		StringBuffer sqlAux = new StringBuffer(sql);
		
		Sentencia sentencia = new Sentencia();
		conAnd = false;
		
		if (getCriteriosDeBusqueda().getMonedaId() != null) {
			sqlAux.append(conectorSql());
			sqlAux.append("m.id =(:ticker)");
			parametros.put("ticker", getCriteriosDeBusqueda().getMonedaId());
			
		}
		
		if (StringUtils.hasText(getCriteriosDeBusqueda().getMonedaName())) {
			sqlAux.append(conectorSql());
			sqlAux.append("m.nombre =(:nombre)");
			parametros.put("nombre", getCriteriosDeBusqueda().getMonedaName());
			
		}
		
		if (StringUtils.hasText(getCriteriosDeBusqueda().getMonedaDescripcion())) {
			sqlAux.append(conectorSql());
			sqlAux.append("m.descripcion =(:descripcion)");
			parametros.put("descripcion", getCriteriosDeBusqueda().getMonedaDescripcion());
			
		}
		
		if (getCriteriosDeBusqueda().getEstado() != null && getCriteriosDeBusqueda().getEstado() != EstadosEnum.TODOS) {
			sqlAux.append(conectorSql());
			sqlAux.append("m.estado =(:estado)");
			parametros.put("estado", getCriteriosDeBusqueda().getEstado());
			
		}
		
		
		log.debug("######## SENTENCIA HQL ########## : {}", sqlAux.toString());
		sentencia.setSql(sqlAux.toString());
		sentencia.setParametros(parametros);
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

	@Override
	public Sentencia sentenciaHQLCount() {
		String sql = "select count(*) from Moneda m ";
		
		return completarSentencia(sql);
	}

}
