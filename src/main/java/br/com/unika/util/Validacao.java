package br.com.unika.util;

import java.lang.reflect.Field;

import javax.persistence.Column;
import javax.persistence.Id;

public class Validacao {

	public static Retorno verificarCamposObrigatorios(Object entidade) {
		Retorno retorno = new Retorno(true, null);
		Field[] campos = Reflexao.getCampos(entidade);
		if (campos != null) {
			for (Field field : campos) {
				if (field.isAnnotationPresent(Column.class)) {
					Boolean nullable = field.getAnnotation(Column.class).nullable();
					Object valor = Reflexao.getValorDoObjeto(field, entidade);
					if (valor == null && nullable == false && !field.isAnnotationPresent(Id.class) ) {
						retorno.setSucesso(false);
						retorno.addMensagem("O Campo " + field.getName() + " Deve Ser Prencido!");
					}else if (field.getType() == String.class) {
						String string = (String) valor;
						if(valor == null || string.trim().isEmpty()) {
							retorno.setSucesso(false);
							retorno.addMensagem("O Campo " + field.getName() + " Deve Ser Prencido!");
						}
					}
					
				}
			}
		} else {
			retorno.setSucesso(false);
			return retorno;
		}

		return retorno;
	}

}
