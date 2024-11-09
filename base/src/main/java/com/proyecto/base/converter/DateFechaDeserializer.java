package com.proyecto.base.converter;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import org.apache.commons.lang3.StringUtils;

public class DateFechaDeserializer extends JsonDeserializer<Date> {
	 @Override
	    public Date deserialize(JsonParser jsonparser,
	            DeserializationContext deserializationcontext) throws IOException, JsonProcessingException {
	        SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
	        String date = jsonparser.getText();
	        Date resp = null;
	        try {
	            if(StringUtils.isNotBlank(date)){
	            	resp = format.parse(date);
	            }
	            return resp;
	        } catch (ParseException e) {
	            throw new RuntimeException(e);
	        }

	    }	
}