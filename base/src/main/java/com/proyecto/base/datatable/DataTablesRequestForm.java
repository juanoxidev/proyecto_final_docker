package com.proyecto.base.datatable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTablesRequestForm<T> extends DataTablesRequest{
	
	private T formBusqueda;
}

