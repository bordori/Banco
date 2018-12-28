package br.com.unika.util;

import java.lang.reflect.Field;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.persistence.Column;
import javax.persistence.Id;

import br.com.unika.enums.EnumTipoConta;

public class Validacao {

	/*
	 * public static Retorno verificarCamposObrigatorios(Object entidade) { Retorno
	 * retorno = new Retorno(true, null); Field[] campos =
	 * Reflexao.getCampos(entidade); if (campos != null) { for (Field campo :
	 * campos) { if (campo.isAnnotationPresent(Column.class)) { Boolean nullable =
	 * campo.getAnnotation(Column.class).nullable(); Object valor =
	 * Reflexao.getValorDoObjeto(campo, entidade); if (valor == null && nullable ==
	 * false && !campo.isAnnotationPresent(Id.class)) { retorno.setSucesso(false);
	 * retorno.addMensagem("O Campo " + campo.getName() + " Deve Ser Prencido!"); }
	 * else if (campo.getType() == String.class) { String string = (String) valor;
	 * if (valor == null || string.trim().isEmpty()) { retorno.setSucesso(false);
	 * retorno.addMensagem("O Campo " + campo.getName() + " Deve Ser Prencido!"); }
	 * }
	 * 
	 * } } } else { retorno.setSucesso(false); return retorno; }
	 * 
	 * return retorno; }
	 */

	public static Object retiraEspacoDesnecessarios(Object entidade) {
		Field[] campos = Reflexao.getCampos(entidade);
		if (campos != null) {
			for (Field campo : campos) {
				if (!campo.isAnnotationPresent(Id.class) && campo.isAnnotationPresent(Column.class)) {
					Object valor = Reflexao.getValorDoObjeto(campo, entidade);

					if (campo.getType() == String.class && valor != null) {
						String string = (String) valor;

						string = (String) valor;
						string = string.trim();
						while (string.contains("  ")) {
							string = string.replaceAll("  ", " ");
						}
						Reflexao.setValorDoObjeto(campo, entidade, string);
					}
				}
			}
		}

		return entidade;
	}

	public static String retiraEspacoDesnecessarios(String texto) {
		if (texto != null) {
			texto = texto.trim();
			while (texto.contains("  ")) {
				texto = texto.replaceAll("  ", " ");
			}
		}

		return texto;
	}

	public static String tipoConta(Integer numero) {
		switch (numero) {
		case 1:
			return EnumTipoConta.CORRENTE.getDescricao();
			
		case 2:
			return EnumTipoConta.POUPANCA.getDescricao();
		
		}
		return null;
	}
	
	public static String converterBoolean(Boolean booleano) {
		if (booleano == true) {
			return "Sim";
		}else {
			return "Não";
		}
	}

	public static boolean validaSeTemSoLetraENumeros(String string) {
		Pattern padrao = Pattern.compile("[a-z A-Z 0-9]*");
		Matcher pesquisa = padrao.matcher(string);
		if (pesquisa.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validaSeTemSoLetras(String string) {
		Pattern padrao = Pattern.compile("[a-z A-Z]*");
		Matcher pesquisa = padrao.matcher(string);
		if (pesquisa.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validaSeTemSoNumeros(String string) {
		Pattern padrao = Pattern.compile("[0-9]*");
		Matcher pesquisa = padrao.matcher(string);
		if (pesquisa.matches()) {
			return true;
		} else {
			return false;
		}
	}

	public static boolean validarCPF(String cpf) {
		cpf = cpf.replaceAll("[^0-9]", "");
		int primeiroDigito = 0;
		int segundoDigito = 0;
		int aux = 0;
		int j = 10;

		for (int i = 0; i < cpf.length() - 2; i++) {
			int d = Integer.parseInt(cpf.substring(i, i + 1));
			primeiroDigito = primeiroDigito + (d * j);
			j--;
		}

		aux = 11 - primeiroDigito % 11;
		if (aux > 9) {
			primeiroDigito = 0;
		} else {
			primeiroDigito = aux;
		}

		j = 11;
		for (int i = 0; i < cpf.length() - 1; i++) {
			int d = Integer.parseInt(cpf.substring(i, i + 1));
			segundoDigito = segundoDigito + (d * j);
			j--;
		}

		aux = 11 - segundoDigito % 11;
		if (aux > 9) {
			segundoDigito = 0;
		} else {
			segundoDigito = aux;
		}

		if (cpf.substring(cpf.length() - 2, cpf.length()).equals("" + primeiroDigito + segundoDigito)) {
			return true;
		} else {
			return false;
		}
	}

}
