package com.proyecto.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.base.model.Rol;
@Repository
public interface RolRepository extends JpaRepository<Rol, Long> {
}