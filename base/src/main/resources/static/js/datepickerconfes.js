if($.datepicker){
$.datepicker.regional['es'] = {
			closeText : 'Cerrar',
			prevText : '<Ant',
	        nextText: 'Sig>',
			currentText : 'Hoy',
			monthNames : [ 'Enero', 'Febrero', 'Marzo', 'Abril', 'Mayo', 'Junio', 'Julio', 'Agosto', 'Septiembre', 'Octubre', 'Noviembre', 'Diciembre' ],
			monthNamesShort : [ 'Ene', 'Feb', 'Mar', 'Abr', 'May', 'Jun', 'Jul', 'Ago', 'Sep', 'Oct', 'Nov', 'Dic' ],
			dayNames : [ 'Domingo', 'Lunes', 'Martes', 'MiÃ©rcoles', 'Jueves', 'Viernes', 'SÃ¡bado' ],
			dayNamesShort : [ 'Dom', 'Lun', 'Mar', 'Mier', 'Juv', 'Vie', 'Sab' ],
			dayNamesMin : [ 'Do', 'Lu', 'Ma', 'Mi', 'Ju', 'Vi', 'S&aacute;' ],
			weekHeader : 'Sm',
			dateFormat : 'dd/mm/yy',
			firstDay : 1,
			isRTL : false,
			showMonthAfterYear : false,
			yearSuffix : ''
	};
	
$.datepicker.setDefaults( $.datepicker.regional[ "es" ] );
$.datepicker.setDefaults({
	 yearRange: "1863:c",
     changeMonth: true,
     changeYear: true 
})};