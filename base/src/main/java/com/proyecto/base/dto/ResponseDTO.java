package com.proyecto.base.dto;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ResponseDTO<T> {

	private T entity;
	private boolean ok;
	private String log;
	private String url;
	
	
}
