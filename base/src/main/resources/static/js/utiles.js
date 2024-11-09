/**
 * Funciones reutilizables Jquery
 */
$.fn.formAObjeto = function() {
	var o = {};
	var aCamposForm = this.serializeArray();
	$.each(aCamposForm, function() {
	       if (o[this.name]) {
	           if (!o[this.name].push) {
	               o[this.name] = [o[this.name]];
	           }
	           o[this.name].push(this.value || '');
	       } else {
	           o[this.name] = this.value || '';
	       }
	});
	return o;
}

$.fn.formAObjetoExt = function() {
	var o = {};
	var aCamposForm = this.serializeArray();
	$.each(aCamposForm, function() {
	       if (o[this.name]) {
	           if (!o[this.name].push) {
	               o[this.name] = [o[this.name]];
	           }
	           o[this.name].push(this.value || '');
	       } else {
	           o[this.name] = this.value || '';
	       }
	});
	return o;
}

$.fn.muestraMsgError = function(){
	$.each(this,function(key,value){
		$('#'+key).after('<small class="error">'+value+'</small>');
	});
}

function muestraMensajeExito(idMensaje,texto){
	  $('.error').remove();
      $('#'+idMensaje).removeClass("alert-danger");
      $('#'+idMensaje).addClass("alert-success");
      $('#'+idMensaje).html(texto);
      $('#'+idMensaje).show();
}

function muestraMensajeError(idMensaje,texto){
	$('#'+idMensaje).html(texto);
	$('#'+idMensaje).removeClass("alert-success");
	$('#'+idMensaje).addClass("alert-danger");
	$('#'+idMensaje).show();
}

function agregaMsgError(mapErrores){
	$.each(mapErrores,function(key,value){
    		$('#'+key).after('<small class="error">'+value+'</small>');
    });
}

function clearMensajesForm(formu){
	$('#' + formu +'small').remove();
}

function clearMensajeById(idMensaje){
    $('#' +idMensaje).empty();
    $('#' +idMensaje).hide();
}

function hayDatosCargadosEnDIV(nombreDiv){
	 var rta=false;
	 $('#'+nombreDiv+' input:text').each(function(key, value) {
			
		 if(this.value!=""){
			 rta= true;
		 }
			 
			
	});		
	 $('#'+nombreDiv+' input:checkbox').each(function(key, value) {
			
		 if(this.checked){
			 rta= true;
		 }
			 
			
	});
	 $('#'+nombreDiv+' select').each(function(key, value) {
			
		 if(this.value!=""){
			 rta= true;
		 }
			 
			
	});		
	 return rta;
}
/*
 * Transforma el campo nombreCampo de un objeto obj en un array de campos
 * Utilizado para checkbox multiselecón
 */
function campoToArray(obj,nombreCampo){
	if(obj[nombreCampo]){
		console.log("true");
		if($.isArray(obj[nombreCampo])){
			console.log("Es array");
		}else{
			console.log("No es array");
			obj[nombreCampo] = [obj[nombreCampo]]; 
		}
	}else{
		console.log("false");
	}
}

function ajustaBreadrcrumb(){
	 headerHeight = $('#header').outerHeight() +10+'px';
	 $('.breadcrumb-2').css('padding-top', headerHeight);
}

/* Ajusta la posicion del titulo */
function ajustaTitulo(){
	 headerHeight = $('#header').outerHeight();
	 $('.titulo').css('margin-top', headerHeight);
}

/* Ajusta la posicion del main principal*/
function ajustaPresentacion(){
	 headerHeight = $('#header').outerHeight();
	 $('#institucional').css('margin-top', headerHeight);
}
