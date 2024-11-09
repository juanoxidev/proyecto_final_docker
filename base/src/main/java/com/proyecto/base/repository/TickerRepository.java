package com.proyecto.base.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.proyecto.base.model.Ticker;
import com.proyecto.base.repository.hql.ListRepository;

public interface TickerRepository extends JpaRepository<Ticker, Long>, ListRepository<Ticker>{

}
