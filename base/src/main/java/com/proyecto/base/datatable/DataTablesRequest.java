package com.proyecto.base.datatable;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTablesRequest {
	
	private int draw;
	private int start;
	private int length;
	private DataTablesSearch search;
	private List<DataTablesColumn> columns = Collections.emptyList();
	private List<DataTablesOrder> order = Collections.emptyList();
}
