package com.proyecto.base.filter;

import java.util.Calendar;
import java.util.HashMap;

import org.springframework.util.StringUtils;

import com.proyecto.base.dto.OperacionDTO;
import com.proyecto.base.dto.PlazoDTO;
import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.enums.TipoOperacion;
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
		
		if (getCriteriosDeBusqueda().getFechaDesde() != null) {
		    sqlAux.append(conectorSql());
		    sqlAux.append("o.fecha >= :fechaDesde");
		    parametros.put("fechaDesde", getCriteriosDeBusqueda().getFechaDesde());
		}

		if (getCriteriosDeBusqueda().getFechaHasta() != null) {
		    sqlAux.append(conectorSql());
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(getCriteriosDeBusqueda().getFechaHasta());
		    calendar.add(Calendar.DAY_OF_MONTH, 1);
		    sqlAux.append("o.fecha < :fechaHasta");
		    parametros.put("fechaHasta", calendar.getTime());
		}
		
		if (getCriteriosDeBusqueda().getTicker() != null) {
			sqlAux.append(conectorSql());
			sqlAux.append("o.ticker.id =(:ticker)");
			parametros.put("ticker", getCriteriosDeBusqueda().getId());
			
		}
		
		if (getCriteriosDeBusqueda().getMoneda() != null) {
			sqlAux.append(conectorSql());
			sqlAux.append("o.moneda.id =(:moneda)");
			parametros.put("moneda", getCriteriosDeBusqueda().getId());
			
		}
		
		if (getCriteriosDeBusqueda().getAlyc() != null) {
			sqlAux.append(conectorSql());
			sqlAux.append("o.alyc.id =(:alyc)");
			parametros.put("alyc", getCriteriosDeBusqueda().getAlyc());
			
		}
		
		if (StringUtils.hasText(getCriteriosDeBusqueda().getCliente())) {
			sqlAux.append(conectorSql());
			sqlAux.append("LOWER(o.cliente) LIKE LOWER(:cliente)");
			parametros.put("cliente", getCriteriosDeBusqueda().getCliente().toLowerCase());
			
		}
		
		if (getCriteriosDeBusqueda().getPlazo() != null) {
			sqlAux.append(conectorSql());
			sqlAux.append("o.plazo.id =(:plazo)");
			parametros.put("plazo", getCriteriosDeBusqueda().getPlazo());
			
		}
		
		if (getCriteriosDeBusqueda().getTipoOperacion() != null && getCriteriosDeBusqueda().getTipoOperacion() != TipoOperacion.TODOS) {
			sqlAux.append(conectorSql());
			sqlAux.append("o.tipoOperacion =(:tipoOperacion)");
			parametros.put("tipoOperacion", getCriteriosDeBusqueda().getTipoOperacion());
			
		}
		
		if (getCriteriosDeBusqueda().getEstado() != null && !getCriteriosDeBusqueda().getEstado().getCodigo().equals("")) {
			sqlAux.append(conectorSql());
			sqlAux.append("o.estado = (:estado)");
			parametros.put("estado", getCriteriosDeBusqueda().getEstado());
			
		}
		
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
