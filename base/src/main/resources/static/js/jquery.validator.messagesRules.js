jQuery.extend(jQuery.validator.messages, {
    required: "Campo obligatorio.",
    remote: "Valor incorrecto.",
    email: "Formato de correo incorrecto.",
    url: "Formato de URL incorrecto.",
    date: "Fecha Incorrecta.",
    dateISO: "Ingrese una fecha válida.",
    number: "Campo Numerico.",
    digits: "Solo se admiten digitos.",
    creditcard: "Ingrese un n&uacute;mero de tarjeta v&aacute;lido.",
    equalTo: "Ingrese nuevamente el valor.",
    accept: "Ingrese un archivo con una extensi&oacute;n v&aacute;lida.",
    maxlength: jQuery.validator.format("El m&aacute;ximo de caracteres permitidos es {0}."),
    minlength: jQuery.validator.format("Ingrese al menos {0} caracteres."),
    rangelength: jQuery.validator.format("Ingrese un valor entre {0} y {1} caracteres de largo."),
    range: jQuery.validator.format("Ingrese un valor entre {0} y {1}."),
    max: jQuery.validator.format("Ingrese un valor menor o iagual a {0}."),
    min: jQuery.validator.format("Ingrese un valor mayor o igual a {0}."),
    extension: jQuery.validator.format("No es un archivo v&aacute;lido."),
    dateITA: jQuery.validator.format("No es una fecha v&aacute;lida."),
    require_from_group: jQuery.validator.format("Ingrese al menos un valor.")
});