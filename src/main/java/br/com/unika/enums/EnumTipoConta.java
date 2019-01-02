package br.com.unika.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumTipoConta {
	
	CORRENTE(1,"Conta Corrente"), POUPANCA(2,"Conta Poupança");

	private Integer valor;
	private String descricao;
	
	private EnumTipoConta(Integer valor,String descricao) {
		this.valor = valor;
		this.descricao = descricao;
	}

	public Integer getValor() {
		return valor;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static List<EnumTipoConta> listaDeTipoDeConta(){
		List<EnumTipoConta> lista = new ArrayList<>();
		lista.add(CORRENTE);
		lista.add(POUPANCA);
		
		return lista;
	}
	
}
