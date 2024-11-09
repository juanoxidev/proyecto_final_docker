function isNumeric(sText) {
	var validChars = "0123456789";
	var char;
	for (i = 0; i < sText.length; i++) {
  		char = sText.charAt(i);
   		if (validChars.indexOf(char) == -1) {
       		return false;
   		}
  	}
	return true;

}

function validaTiposNum(valor){

		if ((valor.value == "")||((valor.value != "") && (isNumeric(valor.value)))) {
			return true;
			} else {
			alert ("Error en tipo de dato, debe ser num�rico");
			}
return false;
}
function validaTiposStr(valor){
		if ((valor.value == "")|| ((valor.value != "") && (isAlphanum(valor.value)) ) ) {
			return true;
			} else {
			alert ("Error en tipo de dato, debe ser alfanum�rico");
			}
return false;
}

function validaTiposNumOblig(valor){
if(valor.value != ""){
		if ((isNumeric(valor.value)) ){
			return true;
			} else {
				alert ("Error en tipo de dato, debe ser num�rico");
			}
		} else {
			alert ("Debe completar todos los campos obligatorios (*)");
		}
return false;
}
function validaTiposStrOblig(valor){
if(valor.value != ""){
		if ((valor.value != "") && (isAlphanum(valor.value))) {
			return true;
			} else {
			alert ("Error en tipo de dato, debe ser alfanum�rico");
			}
			
					} else {
			alert ("Debe completar todos los campos obligatorios (*)");
		}
return false;
}

function validaTiposStrObligEspacio(valor){
	if(valor.value != ""){
			if ((valor.value != "") && (isAlphanumEspacio(valor.value))) {
				return true;
				} else {
				alert ("Error en tipo de dato, debe ser alfanum�rico");
				}
				
						} else {
				alert ("Debe completar todos los campos obligatorios (*)");
			}
	return false;
	}


function validaFecha(fecha){
    
   
    var array_fecha = fecha.split("/")
  
    if (array_fecha.length!=3)
       return false;

   if((array_fecha[2].length!=4) || (array_fecha[1].length!=2) || (array_fecha[0].length!=2))
   		return false;
   
    var anio;
    anio = array_fecha[2];
    
    if (isNaN(anio))
       return false;

    var mes;
    mes = array_fecha[1];
    
    if (isNaN(mes))
       return false;

    var dia;
    dia = array_fecha[0];
    if (isNaN(dia))
       return false;
    
    if(dia>0 && dia <32 && mes >0 && mes<13 && anio>1900)
    	return true;
    return false;	    
}  
function valido(identificador, permitidos) {
	if (identificador == "")
		return false;
	else{
		var t = identificador;
		
		for (var p=0;p<t.length;p++){
			if(permitidos.indexOf(t.charAt(p))<0){
				return false;
			}
		}
		return true;
	}
}

function noValidos(identificador) {
	var t = identificador;
	if (t.indexOf("\"") >= 0 ||
		t.indexOf("'")  >= 0 ||
		t.indexOf("|")  >= 0 ||
		t.indexOf("\&") >= 0 ||
		t.indexOf("\\") >= 0 ||
		t.indexOf("�")  >= 0 ||
		t.indexOf("^")  >= 0 ||
		t.indexOf("~")  >= 0 )
		
		return true;
	return false;
}

function isValidEmail(vStringToTest){
	var erEmail = /^[^@]+@[^\.]+(\.[^\.]+)+$/;

	if (vStringToTest.indexOf(" ")  >= 0 ||
		vStringToTest.indexOf("\"") >= 0 ||
		vStringToTest.indexOf("'")  >= 0 ||
		vStringToTest.indexOf("%")  >= 0 ||
		vStringToTest.indexOf("/")  >= 0 ||
		vStringToTest.indexOf("\&") >= 0 ||
		vStringToTest.indexOf("$")  >= 0 ||
		vStringToTest.indexOf(":")  >= 0 ||
		vStringToTest.indexOf(";")  >= 0 ||
		vStringToTest.indexOf("\<") >= 0 ||
		vStringToTest.indexOf("\>") >= 0 ||
		vStringToTest.indexOf("+")  >= 0 ||
		vStringToTest.indexOf(",")  >= 0 ||
		vStringToTest.indexOf("*")  >= 0 ||
		vStringToTest.indexOf("?")  >= 0 ||
		vStringToTest.indexOf("#")  >= 0
	)
		return false;

    if (!erEmail.test(vStringToTest))
		return false;
	if (vStringToTest.indexOf("\@") != vStringToTest.lastIndexOf("\@"))
		return false;
    if ((vStringToTest.indexOf(".") +1) == vStringToTest.lastIndexOf("."))
    	return false;
	if (vStringToTest.length > 50)
		return false;

	return true;
}

function isAlphanum (s){
   var ret = true;
    for (var i = 0; i < s.length; i++){
        var c = s.charAt(i);

        if (!isLetterOrDigit(c)) ret = false;
    }

    return ret;
}
function isAlphanumEspacio (s){
	   var ret = true;
	    for (var i = 0; i < s.length; i++){
	        var c = s.charAt(i);

	        if (!isLetterOrDigitEspacio(c)) ret = false;
	    }

	    return ret;
	}
function isSignedFloat (s){
	if (isEmpty(s))
       if (isSignedFloat.arguments.length == 1) return false;
       else return (isSignedFloat.arguments[1] == true);

    else {
        var startPos = 0;
        var secondArg = false;

        if (isSignedFloat.arguments.length > 1)
            secondArg = isSignedFloat.arguments[1];

        if ( (s.charAt(0) == "-") || (s.charAt(0) == "+") )
           startPos = 1;
        return (isFloat(s.substring(startPos, s.length), secondArg))
    }
}

function isDigit (c){
	return ((c >= "0") && (c <= "9"))
}

function isLetterOrDigit (c){
	return (isLetter(c) || isDigit(c))
}
function isLetterOrDigitEspacio (c){
	return (isLetter(c) || isDigit(c) || isBlanco(c))
}

function isBlanco (c){
	return ((c == " ")|| (c="�") ||(c="�")  ||(c="�") ||(c="�") ||(c="�")|| (c="�") ||(c="�")  ||(c="�") ||(c="�") ||(c="�"))
}
function isFloat (s){
	var i;
    var seenDecimalPoint = false;

    if (isEmpty(s))
       if (isFloat.arguments.length == 1) return false;
       else return (isFloat.arguments[1] == true);

    if (s == decimalPointDelimiter) return false;

    for (i = 0; i < s.length; i++){
        var c = s.charAt(i);
        if ((c == decimalPointDelimiter) && !seenDecimalPoint) seenDecimalPoint = true;
        else if (!isDigit(c)) return false;
    }
    return true;
}

function isInteger (s){
	var i;
    if (isEmpty(s))
       if (isInteger.arguments.length == 1) return false;
       else return (isInteger.arguments[1] == true);

    for (i = 0; i < s.length; i++){
        var c = s.charAt(i);
        if (!isDigit(c)) return false;
    }

    return true;
}

function isLetter (c){
	return ( ((c >= "a") && (c <= "z")) || ((c >= "A") && (c <= "Z")) )
}

function isEmpty(s){
	return ((s == null) || (s.length == 0))
}

function isSignedInteger (s){
	if (isEmpty(s)){
       if (isSignedInteger.arguments.length == 1) return false;
       else return (isSignedInteger.arguments[1] == true);

   } else {
        var startPos = 0;
        var secondArg = false;

        if (isSignedInteger.arguments.length > 1)
            secondArg = isSignedInteger.arguments[1];

        if ( (s.charAt(0) == "-") || (s.charAt(0) == "+") )
           startPos = 1;
        return (isInteger(s.substring(startPos, s.length), secondArg))
    }
}

function Trim( str ) {

var resultStr = "";
resultStr = TrimLeft(str);
resultStr = TrimRight(resultStr);

return resultStr;
}

function TrimLeft( str ) {
var resultStr = "";
var i = len = 0;
if (str+"" == "undefined" || str == null) 
return "";
str += "";
if (str.length == 0) 
resultStr = "";
else { 
len = str.length;
while ((i <= len) && (str.charAt(i) == " "))
i++;
resultStr = str.substring(i, len);
}
return resultStr;
}

function TrimRight( str ) {
var resultStr = "";
var i = 0;
if (str+"" == "undefined" || str == null) 
return "";
str += "";
if (str.length == 0) 
resultStr = "";
else {
i = str.length - 1;
while ((i >= 0) && (str.charAt(i) == " "))
i--;
resultStr = str.substring(0, i + 1);
}

return resultStr; 
}





function esMenorAHoy(fecha){

    
    hoy=new Date();
   
    var array_fecha = fecha.split("/")    
    var anio;
    anio = parseInt(array_fecha[2]);
   
    var mes;
    mes = parseInt(array_fecha[1]);
    
    var dia;
    dia = parseInt(array_fecha[0]);
    
	  

    var anioHoy = hoy.getYear();
      
       
    if (anioHoy<=999) //firefox da el a�o con dos o tres cifras
       anioHoy+=1900;
    
   
    var fechaLongHoy = (anioHoy*10000)+((hoy.getMonth()+1)*100)+hoy.getUTCDate();
    var fechaLongIngresada=(anio*10000)+(mes*100)+dia;
 	 
 	if(fechaLongIngresada<=fechaLongHoy){
 		return true;
 	}else{
 		return false;
 	}  
   
 } 
 function popupAyuda(codAyuda){
		var w = window.open('./VerAyudaAction.do?method=verAyudaPopup&codigo='+codAyuda,'Ayuda','width=400,height=400,scrollbars=1');
  		w.focus();
  		return ;
 }
 
 function validaFechaDesdeMenorIgualAHasta(fechaDesde, fechaHasta){
	    var array_fechaDesde = fechaDesde.split("/");    
	    var array_fechaHasta = fechaHasta.split("/");
	    if ( Number(array_fechaDesde[2]+array_fechaDesde[1]+array_fechaDesde[0]) > Number(array_fechaHasta[2]+array_fechaHasta[1]+array_fechaHasta[0]))
	    	return false;
	    return true;
 }
 
 function validarLongitud(elemento ,numero,evento){
	   var enters=0;
	   for(var i=0;i<elemento.value.length;i++){ 	  	 	
	 	  	if(elemento.value.substr(i,1).charCodeAt(0)==10){
	 	  	 	 	enters++;
	 	  	  }
	   }
	   if(elemento.value.length+enters>numero){
	   		alert("El texto excede el maximo permitido("+numero+" caracteres)");
	   		elemento.value = elemento.value.substring(0, numero-enters);
	   		return false;
	   }
	   

	   return true;   
	}
 
 function mensajeACreador(usuario,asunto){
	    var w = window.open('./MensajesAction.do?method=datosUsuarioPrevioMensaje&asunto='+asunto +'&codUsuario='+usuario,'nombre_popup','width=600,height=400,scrollbars=1');
		//var w = window.open('./MensajesAction.do?method=mostrarPopUpNuevo&asunto='+asunto +'&codUsuario='+usuario,'nombre_popup','width=600,height=400,scrollbars=1');
		w.focus();

	}
 
 
 function redireccionarCambioIdentidad(codMenor){
	 window.open('./CambioIdentidadAction.do?method=iniciaCambioIdentidad&codigoNNA='+codMenor,'Cambio_identidad','width=550,height=400,scrollbars=1');
 }
 
 
 function agregaValidadorJQUERYFechaHora(){
	 jQuery.validator.addMethod(
		        "dateArg",
		        function(value, element) {
		            var check = false;
		            var re = /^\d{1,2}\/\d{1,2}\/\d{4}$/;
		            if( re.test(value)){
		                var adata = value.split('/');
		                var dd = parseInt(adata[0],10); // was gg (giorno / day)
		                var mm = parseInt(adata[1],10); // was mm (mese / month)
		                var yyyy = parseInt(adata[2],10); // was aaaa (anno / year)
		                var xdata = new Date(yyyy,mm-1,dd);
		                if ((value.length==10) && ( xdata.getFullYear() == yyyy ) && ( xdata.getMonth () == mm - 1 ) && ( xdata.getDate() == dd ) )
		                    check = true;
		                else
		                    check = false;
		            } else
		                check = false;
		            return this.optional(element) || check;
		        },
		        "Formato de fecha incorrecto."
		    );
	 jQuery.validator.addMethod('timeArg', function(value, element) {
		    return value == '' || value.match(/^([01][0-9]|2[0-3]):[0-5][0-9]$/);
		}, 'Ingrese un horario válido');

		
 }
 
 function agregaValidadorJQUERYXOR(){
	 $.validator.addMethod("xor", function(val, el, param) {
		 var valid = false;
		    // loop through sets of nested rules
		 for(var i = 0; i < param.length; ++i) {
			 var setResult = true;
	
		      // loop through nested rules in the set
		     for(var x in param[i]) {
		    	 var result = $.validator.methods[x].call(this, val, el, param[i][x]);
	
		          // If the input breaks one rule in a set we stop and move
		          // to the next set...
		         if(!result) {
		        	 setResult = false;
		             break;
		         }
		     }
	
		        // If the value passes for one set we stop with a true result
		     if(setResult == true) {
		    	 valid = true;
		         break;
		     }
		 }
	
		    // Return the validation result
		 return this.optional(el) || valid;
	 }, "The value entered is invalid");
 }
 
 function agregaValidadorJQUERYRoman(){
	 jQuery.validator.addMethod(
		        "roman",
		        function(value, element) {
		        	var check = false;		        
		        	var matchArr = value.match(/^M{0,4}(CM|CD|D?C{0,3})(XC|XL|L?X{0,3})(IX|IV|V?I{0,3})$/g);
		        	if(matchArr) {
		        		check = true;	
		        	} else {
		        		check = false;	
		        	}
		        	return this.optional(element) || check;
		        },"El numero no es un numero romano valido");
}
 function nuevoAlertMostrarAlertas(mensaje){
	 if(mensaje!=null && mensaje!=""){
		 $("#content").prepend('<div class="alert alert-warning"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a><strong>'+mensaje+'</strong></div>'); 
	 }
		
		
	}
 function nuevoAlertMensaje(mensaje){
	 if(mensaje!=null && mensaje!=""){
	 	 $("#content").prepend('<div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a><strong>'+mensaje+'</strong></div>');
	 }
	 
 }
 function borrarAlertas(){
	 if(mensaje!=null && mensaje!=""){
		 $( ".alert" ).remove();
	 }
	 
 }

 
	function nuevoAlertError(mensaje){
		if(mensaje!=null && mensaje!=""){
			$("#content").prepend('<div class="alert alert-danger"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a><strong>Error: </strong>'+mensaje+'</div>');	
		}
		
		
	}
	 function nuevoAlertMensajePopup(mensaje,popUpContainer){
		 if(mensaje!=null && mensaje!=""){	
			 $(popUpContainer).prepend('<div class="alert alert-success"><a href="#" class="close" data-dismiss="alert" aria-label="close">&times;</a><strong>'+mensaje+'</strong></div>');
		 }	
	}

	 function clearForm()
	 {
	     $(':input').not(':button, :submit, :reset, :hidden, :checkbox, :radio').val('');
	     $(':checkbox').prop('checked', false);
	 }
	 function resetForm(formu)
	 {
		 formu[0].reset();
		 
	 }
	 
	 
	 function mostrarDialogInformacion(info) {
			$('#divInformacion').html(info);
			$('#modalInfo').modal('show');
			
		}	
 