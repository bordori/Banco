package br.com.unika.modelo;

import java.util.Date;

public class Movimentacao {
	
	private Long idMovimentacao;
	private int tipoMovimentacao;
	private Usuario usuario;
	private Contato contato;
	private Double Valor;
	private Date data;
	
	
	
	public Long getIdMovimentacao() {
		return idMovimentacao;
	}
	public void setIdMovimentacao(Long idMovimentacao) {
		this.idMovimentacao = idMovimentacao;
	}
	public int getTipoMovimentacao() {
		return tipoMovimentacao;
	}
	public void setTipoMovimentacao(int tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	public Contato getContato() {
		return contato;
	}
	public void setContato(Contato contato) {
		this.contato = contato;
	}
	public Double getValor() {
		return Valor;
	}
	public void setValor(Double valor) {
		Valor = valor;
	}
	public Date getData() {
		return data;
	}
	public void setData(Date data) {
		this.data = data;
	}

}
