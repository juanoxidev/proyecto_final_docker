package com.proyecto.base.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.proyecto.base.model.Alyc;
import com.proyecto.base.model.Operacion;
import com.proyecto.base.repository.hql.ListRepository;

public interface OperacionRepository extends JpaRepository<Operacion, Long>, ListRepository<Operacion>{

	List<Operacion> findByAlycAndFechaBetween(Alyc alyc, Date fechaDesde, Date fechaHasta);

	@Query(value = "SELECT * FROM operacion o WHERE o.id_alyc = :alyc AND o.fecha BETWEEN :fechaDesde AND :fechaHasta", 
	           nativeQuery = true)
	    List<Operacion> getOperacionesDeAlycEntreFechas(@Param("alyc") Long alyc, 
	                                                    @Param("fechaDesde") Date fechaDesde, 
	                                                    @Param("fechaHasta") Date fechaHasta);

}
