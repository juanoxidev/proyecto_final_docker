package com.proyecto.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.base.model.Alyc;
import com.proyecto.base.repository.hql.ListRepository;

public interface AlycRepository extends JpaRepository<Alyc, Long>, ListRepository<Alyc>{

	boolean existsByNombre(String nombre);

}
