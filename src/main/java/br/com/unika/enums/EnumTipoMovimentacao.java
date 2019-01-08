package br.com.unika.enums;

import java.util.ArrayList;
import java.util.List;

public enum EnumTipoMovimentacao {

	SAQUE(1, "Saque"), DEPOSITO(2, "Deposito"), TRANSFERENCIA(3, "Transferência");

	private Integer valor;
	private String descricao;

	private EnumTipoMovimentacao(Integer valor, String descricao) {
		this.valor = valor;
		this.descricao = descricao;
		
	}

	public Integer getValor() {
		return valor;
	}

	public String getDescricao() {
		return descricao;
	}
	
	public static List<EnumTipoMovimentacao> getTipoDeMovimentacao(){
		List<EnumTipoMovimentacao> listaEnumTipoMovimentacao = new ArrayList<>();
		listaEnumTipoMovimentacao.add(SAQUE);
		listaEnumTipoMovimentacao.add(DEPOSITO);
		listaEnumTipoMovimentacao.add(TRANSFERENCIA);
		return listaEnumTipoMovimentacao;
	}

}
