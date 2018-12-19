$(document).ready(function() {
	$(".soNumeros").bind("keyup blur focus", function(e) {
		e.preventDefault();
		var expre = /[^\d]/g;
		$(this).val($(this).val().replace(expre, ''));
	});
});

$(document).on(
		'submit',
		function() {
			if ($('.soNumeros').val().trim().length < 13
					|| $('.soNumeros').val().trim().length > 13) {
				alert('Codigo de barras deve conter 13 digitos');
				return false;
			}
			return true;
		});

$(function() {
	$('.monetario').maskMoney({

		decimal : ",",
		thousands : "."
	});
});

function alerta() {

	document.getElementById("alerta").style.display = 'block';

}


