package com.proyecto.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.base.model.Moneda;
import com.proyecto.base.repository.hql.ListRepository;

public interface MonedaRepository extends JpaRepository<Moneda, Long>, ListRepository<Moneda>{

	boolean existsByNombre(String monedaName);

}
