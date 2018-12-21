$(document).ready(function() {
	$(".soNumeros").bind("keyup blur focus", function(e) {
		e.preventDefault();
		var expre = /[^\d]/g;
		$(this).val($(this).val().replace(expre, ''));
	});
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




