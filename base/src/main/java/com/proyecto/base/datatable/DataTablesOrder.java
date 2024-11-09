package com.proyecto.base.datatable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTablesOrder {
	
	public enum Direction {
		asc, desc
	}

	private int column;
	private Direction dir;
}
