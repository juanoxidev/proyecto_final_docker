package com.proyecto.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.base.model.Plantilla;
import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.hql.ListRepository;

public interface PlantillaRepository extends JpaRepository<Plantilla, Long>, ListRepository<Plantilla>{

}
