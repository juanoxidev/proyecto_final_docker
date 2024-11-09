package com.proyecto.base.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.ResolverStyle;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Locale;

import org.apache.commons.lang3.StringUtils;

public class UtilesFechas {

		private static Locale locale = new Locale("es", "AR"); 
		
		public static Date getFechaDeHoySinHora(){
			Calendar cal = Calendar.getInstance();
			cal.set(Calendar.HOUR_OF_DAY, 0);
		    cal.set(Calendar.MINUTE, 0);
		    cal.set(Calendar.SECOND, 0);
		    cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();
		}
		
		public static Date getFechaDeHoyConHora(){
			Calendar cal = Calendar.getInstance();
			return cal.getTime();
		}
		
		public static Date getFechaDeHoyConHora(Date d){
			Calendar cal = Calendar.getInstance();
			cal.setTime(d);
			return cal.getTime();
			
		}
		
		public static Date agregarHorasDate(Date date, int hours) {
		    Calendar calendar = Calendar.getInstance();
		    calendar.setTime(date);
		    calendar.add(Calendar.HOUR_OF_DAY, hours);
		    return calendar.getTime();
		}
		
		public static Date getFechaSinHora(Date date) throws ParseException{
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			String auxFecha = df.format(date);
			return df.parse(auxFecha);
		}
		
		public static String getStrFecha(Date d) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy");
			return df.format(d);
		}
		
		public static String getStrFechaConHora(Date d) {
			SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
			return df.format(d);
		}
		
		public static Date getHoyMenosAnios(int anios) {
			Calendar cal = Calendar.getInstance();
			cal.add(Calendar.YEAR,-anios);
			return cal.getTime();
			
		}
		
		public static String getFechaCompleta(Date d) {
			SimpleDateFormat df = new SimpleDateFormat("d 'de' MMMM 'de' yyyy", locale);
			return df.format(d);
		}
		
		
		public static String getFechaFormateada(Date fecha) {
			if (fecha == null) {
	            return "";
	        } else {
	        	SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
	            return formatter.format(fecha);
	        }
		}
		
		public static boolean esStringUnaFechaValida(String fecha){
	        boolean esFechaValida = true;
	        try{
	        	DateTimeFormatter format = DateTimeFormatter.ofPattern("dd/MM/uuuu");
	            //LocalDate.parse(fecha, format);
	            LocalDate parse = LocalDate.parse(fecha, 
	            		format.withResolverStyle(ResolverStyle.STRICT));
	        }catch(DateTimeException e) {
	            esFechaValida = false;
	        }
	        return esFechaValida;
	    }


		public static long difDiasEntre2Dates(Date fechaDesde, Date fechaHasta){
	    	
	    	GregorianCalendar calDesde = new GregorianCalendar();
	    	calDesde.setTime(fechaDesde);
	    	GregorianCalendar calHasta = new GregorianCalendar();
	    	calHasta.setTime(fechaHasta);
	    	long difms=calHasta.getTimeInMillis() - calDesde.getTimeInMillis();
	    	long difd=difms / (1000 * 60 * 60 * 24);
	    	return difd+1;
	   }
		
		public static long difDiasEntre2DatesPlazo(Date fechaDesde, Date fechaHasta){
	    	
	    	GregorianCalendar calDesde = new GregorianCalendar();
	    	calDesde.setTime(fechaDesde);
	    	GregorianCalendar calHasta = new GregorianCalendar();
	    	calHasta.setTime(fechaHasta);
	    	long difms=calHasta.getTimeInMillis() - calDesde.getTimeInMillis();
	    	long difd=difms / (1000 * 60 * 60 * 24);
	    	return difd;
	   }
		
		
		public static int difMesesEntre2Dates(Date fechaDesde, Date fechaHasta) {
			
			GregorianCalendar calDesde = new GregorianCalendar();
	    	calDesde.setTime(fechaDesde);
	    	GregorianCalendar calHasta = new GregorianCalendar();
	    	calHasta.setTime(fechaHasta);
					
			int difA = calHasta.get(Calendar.YEAR) - calDesde.get(Calendar.YEAR);
	        int difM = difA * 12 + calHasta.get(Calendar.MONTH) - calDesde.get(Calendar.MONTH);
			
	        return difM;
		}
		
		public static String convierteDateAString(Date fecha) {
			if(fecha==null)
				return null;
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("dd/MM/yyyy");
			String fechaString = format.format(fecha);
			return fechaString;
		}
		
		
		
		public static String convierteDateAStringDOJO(Date fecha) {
			if(fecha==null)
				return null;
					
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("yyyy-MM-dd");		
			String fechaString = format.format(fecha);		
			return fechaString;
		}
		
		
		
		
		public static Date sumarDias(Date fecha, String dias){
	    	
	    	GregorianCalendar calDesde = new GregorianCalendar();
	    	calDesde.setTime(fecha);
	    	calDesde.add(Calendar.DATE,(new Integer(dias)).intValue());


				SimpleDateFormat format = new SimpleDateFormat();
				format.applyPattern("dd/MM/yyyy");

				try {
					fecha = format.parse(convierteDateAString( calDesde.getTime()));
					} catch (ParseException e) {
						// cachear exception
					}
				return fecha;

	   }
		
		public static Date sumarMeses(Date fecha, String meses){
	    	
	    	GregorianCalendar calDesde = new GregorianCalendar();
	    	calDesde.setTime(fecha);
	    	calDesde.add(Calendar.MONTH,(new Integer(meses)).intValue());


				SimpleDateFormat format = new SimpleDateFormat();
				format.applyPattern("dd/MM/yyyy");

				try {
					fecha = format.parse(convierteDateAString( calDesde.getTime()));
					} catch (ParseException e) {
						// cachear exception
					}
				return fecha;

		}
		
		
		public static Date restarDias(Date fecha, String dias){
	    	
	    	GregorianCalendar calDesde = new GregorianCalendar();
	    	calDesde.setTime(fecha);
	    	calDesde.add(Calendar.DATE,-(new Integer(dias)).intValue());


				SimpleDateFormat format = new SimpleDateFormat();
				format.applyPattern("dd/MM/yyyy");

				try {
					fecha = format.parse(convierteDateAString( calDesde.getTime()));
					} catch (ParseException e) {
						// cachear exception
					}
				return fecha;

	   }
		
		public static Date convierteStringADate(String fechaString) {
			if(fechaString==null)
				return null;
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("dd/MM/yyyy");

			Date fecha = null;
			try {
				fecha = format.parse(fechaString);
			} catch (ParseException e) {
				// cachear exception
			}
			return fecha;
		}
		public static Date convierteStringDOJOADate(String fechaString) {
			Date fecha = null;
			if(fechaString==null)
				return null;
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("yyyy-MM-dd");

			try {
				fecha = format.parse(fechaString);
			} catch (ParseException e) {
				// cachear exception
			}
			return fecha;
		}
		


		public static String convierteDateAStringNoDevuelveNull(GregorianCalendar gregorianCalendar) {
			if(gregorianCalendar==null)
				return "";
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("dd/MM/yyyy");
			String fechaString = format.format(gregorianCalendar);
			return fechaString;
		}	
		public static String convierteDateAStringNoDevuelveNull(Date date) {
			if(date==null)
				return "";
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("dd/MM/yyyy");
			String fechaString = format.format(date);
			return fechaString;
		}	

		
		public static Long convierteDateALong(Date fecha) {
			if(fecha==null)
				return null;
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("yyyyMMdd");
			String fechaString = format.format(fecha);
			return new Long(fechaString);
		}

		public static String convierteDateHoraAString(Date horario) {
			if(horario==null)
				return null;
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("HH:mm");
			String fechaString = format.format(horario);
			return fechaString;
		}
		public static Date convierteLongADate(Long fechaLong) {
			Date fecha = null;
			if(fechaLong==null)
				return null;
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
			try {
				fecha = format.parse(fechaLong.toString());
				return fecha;
			} catch (ParseException e) {
				// cachear exception
			}
			return fecha;
		}
		public static int calculaEdad(Date fechaNac){
			
			if(fechaNac==null){
				return 0;
			}
			Calendar hoy = new GregorianCalendar();
			Calendar nacio = new GregorianCalendar();
			hoy.setTime(new Date());
			nacio.setTime(fechaNac);
			int anios=hoy.get(Calendar.YEAR)-nacio.get(Calendar.YEAR);
			nacio.set(Calendar.YEAR, hoy.get(Calendar.YEAR));
			if(nacio.after(hoy))
				anios--;
			
			return anios;
			
			
			
		}
		public static Long convierteDateFechaHoraALong(Date fecha) {
			if(fecha==null)
				return null;
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("yyyyMMddHHmm");
			String fechaString = format.format(fecha);
			return new Long(fechaString);
		}
		public static Date convierteLongFechaHoraADate(Long fechaLong) {
			Date fecha = null;
			if(fechaLong==null)
				return null;
			SimpleDateFormat format = new SimpleDateFormat("yyyyMMddHHmm");
			try {
				fecha = format.parse(fechaLong.toString());
				return fecha;
			} catch (ParseException e) {
				// cachear exception
			}
			
			return fecha;
		}
		public static String convierteDateFechaHoraAString(Date fecha) {
			if(fecha==null)
				return "";
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			
				String fechaString = format.format(fecha);
				return fechaString;
			
		}
		
		public static String convierteDateFechaHoraAStringHora(Date fecha) {
			if(fecha==null)
				return "";
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");
			
				String fechaString = format.format(fecha);
				return fechaString.substring(11);
			
		}
		public static String getFechaHoyString() {
			SimpleDateFormat formato = new SimpleDateFormat("dd/MM/yyyy");
			String fechaString=formato.format(new Date());
			return fechaString;
		}
		public static Date convierteStringHoraDOJOaDate(String hora) {
			if(hora==null)
				return null;
			hora=StringUtils.substring(hora,1);	
			SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
			

			Date fecha = null;
			try {
				fecha = format.parse(hora);
			} catch (ParseException e) {
				// cachear exception
			}
			return fecha;
		}
		public static String getFechaHoyStringDOJO() {
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			String fechaString=formato.format(new Date());
			return fechaString;
		}
		public static String getHoraHoyStringDOJO(){
			SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
			String fechaString="T"+formato.format(new Date());
			return fechaString;
		}
		public static String getFechaStringDOJO(Date fecha) {
			if(fecha==null)
				return "";	
			SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
			String fechaString=formato.format(fecha);
			return fechaString;
		}
		public static String getHoraStringDOJO(Date fecha){
			if(fecha==null)
				return "";
			SimpleDateFormat formato = new SimpleDateFormat("HH:mm");
			String fechaString="T"+formato.format(fecha);
			return fechaString;
		}
		
		public static String convierteDateFechaHoraAStringFormalFechaHora(Date fecha) {
			if(fecha==null)
				return "";
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
			String fechaTemp = format.format(fecha);
			String mes = UtilesFechas.getMonth(fechaTemp.substring(3,5));
			String fechaHoraFormal = (new Integer(fechaTemp.substring(0, 2))).intValue() + " de " + mes + " de " + fechaTemp.substring(6, 11) + "a las " + fechaTemp.substring(11); 
			return fechaHoraFormal;
			
		}
		public static String convierteDateFechaHoraAStringFormalFechaHoraReconocimiento(Date fecha) {
			if(fecha==null)
				return "";
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
			String fechaTemp = format.format(fecha);
			String mes = UtilesFechas.getMonth(fechaTemp.substring(3,5));
			String fechaHoraFormal ="Buenos Aires, "+ (new Integer(fechaTemp.substring(0, 2))).intValue() + " de " + mes + " de " + fechaTemp.substring(6, 11) + "siendo las " + fechaTemp.substring(11); 
			return fechaHoraFormal;
			
		}	
		public static String convierteDateFechaHoraAStringFormalFecha(Date fecha) {
			if(fecha==null)
				return "";
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
			String fechaTemp = format.format(fecha);
			String mes = UtilesFechas.getMonth(fechaTemp.substring(3,5));
			String fechaHoraFormal = (new Integer(fechaTemp.substring(0, 2))).intValue() + " de " + mes + " de " + fechaTemp.substring(6, 11) ; 
			return fechaHoraFormal;
			
		}
		public static String convierteDateFechaHoraAStringDiaMesAnio_FechaFallecimientoLic(Date fecha) {
			if(fecha==null)
				return "";
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
			String fechaTemp = format.format(fecha);
			String mes = UtilesFechas.getMonth(fechaTemp.substring(3,5));
			String fechaHoraFormal = fechaTemp.substring(0, 2) + "             " + mes + "                                          " + fechaTemp.substring(6, 11) ; 
			return fechaHoraFormal;
			
		}	
		public static String convierteDateFechaHoraAStringDiaMesAnio_FechaDiaLic(Date fecha) {
			if(fecha==null)
				return "";
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
			String fechaTemp = format.format(fecha);
			String mes = UtilesFechas.getMonth(fechaTemp.substring(3,5));
			String fechaHoraFormal = "                                                                                                        "+fechaTemp.substring(0, 2) + "                " + mes + "                                   " + fechaTemp.substring(6, 11) ; 
			return fechaHoraFormal;
			
		}	
		private static  String getMonth(String month) {
			HashMap meses = new HashMap();
			meses.put("01","Enero");
			meses.put("02","Febrero");
			meses.put("03","Marzo");
			meses.put("04","Abril");
			meses.put("05","Mayo");
			meses.put("06","Junio");
			meses.put("07","Julio");
			meses.put("08","Agosto");
			meses.put("09","Septiembre");
			meses.put("10","Octubre");
			meses.put("11","Noviembre");
			meses.put("12","Diciembre");
			return (String) meses.get(month);
		}
		public static String convierteDateFechaHoraAStringDiaMes(Date fecha) {
			if(fecha==null)
				return "";
			SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy HH:mm");		
			String fechaTemp = format.format(fecha);
			String mes = UtilesFechas.getMonth(fechaTemp.substring(3,5));
			String fechaHoraFormal = (new Integer(fechaTemp.substring(0, 2))).intValue() + " de " + mes ; 
			return fechaHoraFormal;
		}
		public static String getAnio(Date date) {
			SimpleDateFormat format = new SimpleDateFormat();
			format.applyPattern("yyyy");
			String anioString = format.format(date);
			return anioString;
			
		}
		public static int getMes(Date date) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			int monthCalendar=cal.get(Calendar.MONTH);
			return (monthCalendar+1);
			
			
		}
		
		public static String getAnioAnterior(Date date) {
			Calendar cal = new GregorianCalendar();
			cal.setTime(date);
			cal.add(Calendar.YEAR, -1);
			return ""+cal.get(Calendar.YEAR);

			
		}
		
		public static Date truncarSegundos(Date fecha){
			GregorianCalendar cal = new GregorianCalendar();
	    	cal.setTime(fecha);
	    	cal.set(Calendar.SECOND, 0);
	    	cal.set(Calendar.MILLISECOND, 0);
			return cal.getTime();	
		}

		/**
		 * Obtiene el dia de la fecha pasada por parametro.
		 */
		public static int getDay(Date date){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			return calendar.get(Calendar.DAY_OF_MONTH);
		}
		/**
		 * Obtiene el mes de la fecha pasada por parametro.
		 */
		public static int getMonth(Date date){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			return calendar.get(Calendar.MONTH) + 1;
		}

		/**
		 * Obtiene el anio de la fecha pasada por parametro.
		 */
		public static int getYear(Date date){
			Calendar calendar = Calendar.getInstance();
			calendar.setTime(date);

			return calendar.get(Calendar.YEAR);
		}
		/**
		 * Dada una fecha, obtiene el mes en formato literal
		 */
		public static String getMonthDescription(Date date) {
			switch (getMonth(date)){
				case 1:	return "Enero";
				case 2:	return "Febrero";
				case 3:	return "Marzo";
				case 4:	return "Abril";
				case 5:	return "Mayo";
				case 6:	return "Junio";
				case 7:	return "Julio";
				case 8:	return "Agosto";
				case 9:	return "Septiembre";
				case 10: return "Octubre";
				case 11: return "Noviembre";
				case 12: return "Diciembre";
				default: return "";
			}
		}
		
}
