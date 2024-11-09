package com.proyecto.base.filter;

import java.util.HashMap;

import org.springframework.util.StringUtils;

import com.proyecto.base.dto.UsuarioDTO;
import com.proyecto.base.filter.sentencia.Sentencia;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioFilter implements IFilter {
	
	private UsuarioDTO criteriosDeBusqueda;
	private boolean conAnd = false;
	
	
	@Override
	public Sentencia sentenciaHQL() {
		String sql = " from Usuario u ";
		String orden =" order by id ";
		Sentencia sentencia = completarSentencia(sql);
		sentencia.append(orden);
		return sentencia;
	}

	public UsuarioFilter(UsuarioDTO criteriosDeBusqueda) {
		this.criteriosDeBusqueda = criteriosDeBusqueda;
	}

	private Sentencia completarSentencia(String sql) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		
		StringBuffer sqlAux = new StringBuffer(sql);
		
		Sentencia sentencia = new Sentencia();
		conAnd = false;
		
		if (StringUtils.hasText(getCriteriosDeBusqueda().getUsername())) {
			sqlAux.append(conectorSql());
			sqlAux.append("LOWER(u.username) LIKE LOWER(:username)");
			parametros.put("username","%"+ getCriteriosDeBusqueda().getUsername().toLowerCase()+"%");
			
		}
		
		
		if (getCriteriosDeBusqueda().getEstado() != null && !getCriteriosDeBusqueda().getEstado().getCodigo().equals("")) {
			sqlAux.append(conectorSql());
			sqlAux.append("u.estado LIKE (:estado)");
			parametros.put("estado", getCriteriosDeBusqueda().getEstado());
			
		}
		
		log.debug("######## SENTENCIA HQL ########## : {}", sqlAux.toString());
		sentencia.setSql(sqlAux.toString());
		sentencia.setParametros(parametros);
		return sentencia;
	}





	@Override
	public Sentencia sentenciaHQLCount() {
		String sql = "select count(*) from Usuario u ";
		
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
