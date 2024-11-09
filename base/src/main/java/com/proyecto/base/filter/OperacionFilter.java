package com.proyecto.base.filter;

import java.util.HashMap;

import org.springframework.util.StringUtils;

import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.dto.PlazoDTO;
import com.proyecto.base.dto.UsuarioDTO;
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
public class OperacionFilter implements IFilter {
	private OperacionDTO criteriosDeBusqueda;
	private boolean conAnd = false;
	
	
	@Override
	public Sentencia sentenciaHQL() {
		String sql = " from Operacion o ";
		String orden =" order by id";
		Sentencia sentencia = completarSentencia(sql);
		sentencia.append(orden);
		return sentencia;
	}

	public OperacionFilter(OperacionDTO criteriosDeBusqueda) {
		this.criteriosDeBusqueda = criteriosDeBusqueda;
	}

	private Sentencia completarSentencia(String sql) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		
		StringBuffer sqlAux = new StringBuffer(sql);
		
		Sentencia sentencia = new Sentencia();
		conAnd = false;
		
//		if (getCriteriosDeBusqueda().getId() != null) {
//			sqlAux.append(conectorSql());
//			sqlAux.append("o.id =(:operacion)");
//			parametros.put("operacion", getCriteriosDeBusqueda().getId());
//			
//		}
//		
//		if (getCriteriosDeBusqueda().getTicker() != null) {
//			sqlAux.append(conectorSql());
//			sqlAux.append("o.ticker.id =(:ticker)");
//			parametros.put("ticker", getCriteriosDeBusqueda().getId());
//			
//		}
//		
//		if (!getCriteriosDeBusqueda().getMoneda().equals(null)) {
//			sqlAux.append(conectorSql());
//			sqlAux.append("o.moneda.id =(:moneda)");
//			parametros.put("moneda", getCriteriosDeBusqueda().getId());
//			
//		}
//		
//		if (getCriteriosDeBusqueda().getAlyc() != null) {
//			sqlAux.append(conectorSql());
//			sqlAux.append("o.alyc.id =(:alyc)");
//			parametros.put("alyc", getCriteriosDeBusqueda().getAlyc());
//			
//		}
//		
//		if (getCriteriosDeBusqueda().getEstado() != null && !getCriteriosDeBusqueda().getEstado().getCodigo().equals("")) {
//			sqlAux.append(conectorSql());
//			sqlAux.append("o.estado LIKE (:estado)");
//			parametros.put("estado", getCriteriosDeBusqueda().getEstado());
//			
//		}
		
		log.debug("######## SENTENCIA HQL ########## : {}", sqlAux.toString());
		sentencia.setSql(sqlAux.toString());
		sentencia.setParametros(parametros);
		return sentencia;
	}





	@Override
	public Sentencia sentenciaHQLCount() {
		String sql = "select count(*) from Operacion o ";
		
		return completarSentencia(sql);
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

}
