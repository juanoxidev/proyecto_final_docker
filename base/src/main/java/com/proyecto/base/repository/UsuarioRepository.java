package com.proyecto.base.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyecto.base.model.Usuario;
import com.proyecto.base.repository.hql.ListRepository;

@Repository
public interface UsuarioRepository extends JpaRepository<Usuario, Long>, ListRepository<Usuario>{
    
	Optional<Usuario> findByUsername(String username);
    
    Usuario findByEmail(String email);
    
    boolean existsByUsername(String username);
    
    boolean existsByEmail(String email);

	Usuario findByEmailIgnoreCase(String email);
}