package br.com.unika.enums;

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

}
