package com.proyecto.base.repository.hql;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.Query;

import org.apache.commons.collections4.MapUtils;

import com.proyecto.base.filter.sentencia.Sentencia;


public class ListRepositoryImpl<T> implements ListRepository<T> {
	@PersistenceContext
    EntityManager entityManager;
	
	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(String sql, Map<String, Object> map,  Integer primerResultado, Integer cantidadAPedir) {
		List<T> result = null;
		
	    Query query = entityManager.createQuery(sql);
	    if(MapUtils.isNotEmpty(map)) {
	    	Iterator<String> it = map.keySet().iterator();
	    
		    while(it.hasNext()){
		    	String key = it.next();
		    	query.setParameter(key,map.get(key));
		    }
	    }
	    if(primerResultado!=null)
	    	query.setFirstResult(primerResultado.intValue());
	    
	    if(cantidadAPedir!=null)
	    	query.setMaxResults(cantidadAPedir.intValue());
	    
	    result=query.getResultList();
	    
	    return result;				
	}

	@Override
	public Long count(String sql, Map<String, Object> map) {
		Long  result = null;
		Query query = entityManager.createQuery(sql);
		
		Iterator<String> it = map.keySet().iterator();
		
		while(it.hasNext()){
			String key = it.next();
			query.setParameter(key,map.get(key));
		}
		result= (Long)query.getSingleResult();
		
		return result;		
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<T> list(Sentencia sentencia, Integer primerResultado, Integer cantidadAPedir) {
		List<T> result = null;
		
	    Query query = entityManager.createQuery(sentencia.getSql());
	    
	    Iterator<String> it = sentencia.getParametros().keySet().iterator();
	    
	    while(it.hasNext()){
	    	String key = it.next();
	    	query.setParameter(key,sentencia.getParametros().get(key));
	    }
	    if(primerResultado!=null)
	    	query.setFirstResult(primerResultado.intValue());
	    
	    if(cantidadAPedir!=null)
	    	query.setMaxResults(cantidadAPedir.intValue());
	    
	    result=query.getResultList();
	    
	    return result;				
	}

	@Override
	public Long count(Sentencia sentencia) {
		Long  result = null;
		Query query = entityManager.createQuery(sentencia.getSql());
		
		Iterator<String> it = sentencia.getParametros().keySet().iterator();
		
		while(it.hasNext()){
			String key = it.next();
			query.setParameter(key,sentencia.getParametros().get(key));
		}
		result= (Long)query.getSingleResult();
		
		return result;		
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public <U> List<U> listDto(Sentencia sentenciaDto, Integer primerResultado, Integer cantidadAPedir) {
		List<U> result = null;
		
	    Query query = entityManager.createQuery(sentenciaDto.getSql());
	    
	    Iterator<String> it = sentenciaDto.getParametros().keySet().iterator();
	    
	    while(it.hasNext()){
	    	String key = it.next();
	    	query.setParameter(key,sentenciaDto.getParametros().get(key));
	    }
	    if(primerResultado!=null)
	    	query.setFirstResult(primerResultado.intValue());
	    
	    if(cantidadAPedir!=null)
	    	query.setMaxResults(cantidadAPedir.intValue());
	    
	    result=query.getResultList();
	    
	    return result;			
	}

}
