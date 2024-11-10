package com.proyecto.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.base.model.Plazo;
import com.proyecto.base.repository.hql.ListRepository;

public interface PlazoRepository extends JpaRepository<Plazo,Long>, ListRepository<Plazo> {

	boolean existsByNombre(String trim);

}
