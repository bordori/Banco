package br.com.unika.util;

import java.util.ArrayList;
import java.util.List;

public class Retorno {

	boolean sucesso;
	ArrayList<String> retorno = new ArrayList<>();
	ArrayList<?> lista = new ArrayList<>();
	
	public Retorno(boolean sucesso, String retorno ) {
		setSucesso(sucesso);
		addMensagem(retorno);
	}

	public boolean isSucesso() {
		return sucesso;
	}

	public void setSucesso(boolean sucesso) {
		this.sucesso = sucesso;
	}

	public ArrayList<String> getRetorno() {
		return retorno;
	}

	public void setRetorno(ArrayList<String> retorno) {
		this.retorno = retorno;
	}
	public void addMensagem(String mensagem) {
		if(mensagem!=null && !mensagem.equals("")) {
			this.retorno.add(mensagem);
		}
	}
	
	public String getMensagens() {
		String retorno = "";
		for (String string : getRetorno()) {
			retorno= retorno +string+"\n";
		}
		return retorno;
	}

	public ArrayList<?> getLista() {
		return lista;
	}

	public void setLista(ArrayList<?> lista) {
		this.lista = lista;
	}

	
	
	
}
