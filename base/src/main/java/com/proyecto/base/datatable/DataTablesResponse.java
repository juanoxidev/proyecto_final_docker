package com.proyecto.base.datatable;

import java.util.Collections;
import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DataTablesResponse<T>{
    private int recordsTotal;
    private int recordsFiltered;
    private List<T> content;
}
