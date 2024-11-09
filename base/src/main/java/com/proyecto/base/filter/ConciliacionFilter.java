package com.proyecto.base.filter;

import java.util.HashMap;

import org.springframework.util.StringUtils;

import com.proyecto.base.dto.AlycDTO;
import com.proyecto.base.dto.ConciliacionDTO;
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
public class ConciliacionFilter  implements IFilter{
	private ConciliacionDTO criteriosDeBusqueda;
	private boolean conAnd = false;
	
	@Override
	public Sentencia sentenciaHQL() {
		String sql = " from Alyc a ";
		String orden =" order by id ";
		Sentencia sentencia = completarSentencia(sql);
		sentencia.append(orden);
		return sentencia;
	}

	public ConciliacionFilter(ConciliacionDTO criteriosDeBusqueda2) {
		this.criteriosDeBusqueda = criteriosDeBusqueda2;
	}

	private Sentencia completarSentencia(String sql) {
		HashMap<String,Object> parametros = new HashMap<String, Object>();
		
		StringBuffer sqlAux = new StringBuffer(sql);
		
		Sentencia sentencia = new Sentencia();
		conAnd = false;
		
		if (getCriteriosDeBusqueda().getIdAlyc() != null) {
			sqlAux.append(conectorSql());
			sqlAux.append("a.id  = :id");
			parametros.put("id",getCriteriosDeBusqueda().getIdAlyc());
			
		}
	
		log.debug("######## SENTENCIA HQL ########## : {}", sqlAux.toString());
		sentencia.setSql(sqlAux.toString());
		sentencia.setParametros(parametros);
		return sentencia;
	}





	@Override
	public Sentencia sentenciaHQLCount() {
		String sql = "select count(*) from Alyc a ";
		
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
