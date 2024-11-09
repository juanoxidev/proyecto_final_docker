package com.proyecto.base.datatable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class DataTablesColumn {
	

	private String name;
    private boolean searchable;
    private boolean orderable;
    private DataTablesSearch search;



}