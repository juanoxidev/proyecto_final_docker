package com.proyecto.base.filter;

import java.util.HashMap;

import org.springframework.util.StringUtils;

import com.proyecto.base.dto.PlazoDTO;
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
public class PlazoFilter implements IFilter {
	private PlazoDTO criteriosDeBusqueda;
	private boolean conAnd = false;
	
	public PlazoFilter(PlazoDTO criteriosDeBusqueda) {
		this.criteriosDeBusqueda = criteriosDeBusqueda;
	}
	
	@Override
	public Sentencia sentenciaHQL() {
		String sql = " from Plazo p ";
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
		
		if (getCriteriosDeBusqueda().getPlazoId() != null) {
			sqlAux.append(conectorSql());
			sqlAux.append("p.id =(:ticker)");
			parametros.put("ticker", getCriteriosDeBusqueda().getPlazoId());
			
		}
		
		if (StringUtils.hasText(getCriteriosDeBusqueda().getPlazoName())) {
			sqlAux.append(conectorSql());
			sqlAux.append("UPPER(p.nombre) LIKE UPPER(:nombre)");
			parametros.put("nombre", "%"+getCriteriosDeBusqueda().getPlazoName());
			
		}
		
		if (StringUtils.hasText(getCriteriosDeBusqueda().getPlazoDescripcion())) {
			sqlAux.append(conectorSql());
			sqlAux.append("UPPER(p.descripcionCompleta) LIKE UPPER(:descripcion)");
			parametros.put("descripcion", "%"+getCriteriosDeBusqueda().getPlazoDescripcion());
			
		}
		
		if (getCriteriosDeBusqueda().getEstado() != null && getCriteriosDeBusqueda().getEstado() != EstadosEnum.TODOS) {
			sqlAux.append(conectorSql());
			sqlAux.append("p.estado =(:estado)");
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
		String sql = "select count(*) from Plazo p ";
		
		return completarSentencia(sql);
	}



}
