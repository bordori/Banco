package br.com.unika.modelo;

import java.io.Serializable;
import java.util.Calendar;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "movimentacao")
@Component
public class Movimentacao implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, name = "ID_MOVIMENTACAO", unique = true)
	private Long idMovimentacao;

	@Column(nullable = false, name = "TIPO_MOVIMENTACAO")
	private Integer tipoMovimentacao;

	@ManyToOne
	@JoinColumn(name = "ID_CONTA", nullable = false)
	private Conta conta;

	@Column(name = "NOME_FAVORECIDO")
	private String nomeFavorecido;

	@Column(name = "CPF_FAVORECIDO")
	private String cpfFavoracido;

	@Column(name = "NUMERO_BANCO_FAVORECIDO")
	private String numeroBancoFavorecido;

	@Column(name = "NOME_BANCO_FAVORECIDO")
	private String nomeBancoFavorecido;

	@Column(name = "NOME_AGENCIA_FAVORECIDO")
	private String nomeAgenciaFavorecido;

	@Column(name = "NUMERO_AGENCIA_FAVORECIDO")
	private String numeroAgenciaFavorecido;

	@Column(name = "CONTA_FAVORECIDO")
	private String contaFavorecido;

	@Column(nullable = false, name = "VALOR")
	private String Valor;

	@Column(nullable = false, name = "DATA")
	private Calendar data;

	public String getNomeAgenciaFavorecido() {
		return nomeAgenciaFavorecido;
	}

	public void setNomeAgenciaFavorecido(String nomeAgenciaFavorecido) {
		this.nomeAgenciaFavorecido = nomeAgenciaFavorecido;
	}

	public String getNumeroAgenciaFavorecido() {
		return numeroAgenciaFavorecido;
	}

	public void setNumeroAgenciaFavorecido(String numeroAgenciaFavorecido) {
		this.numeroAgenciaFavorecido = numeroAgenciaFavorecido;
	}

	public Long getIdMovimentacao() {
		return idMovimentacao;
	}

	public void setIdMovimentacao(Long idMovimentacao) {
		this.idMovimentacao = idMovimentacao;
	}

	public Integer getTipoMovimentacao() {
		return tipoMovimentacao;
	}

	public void setTipoMovimentacao(Integer tipoMovimentacao) {
		this.tipoMovimentacao = tipoMovimentacao;
	}

	public Conta getConta() {
		return conta;
	}

	public void setConta(Conta conta) {
		this.conta = conta;
	}

	public String getValor() {
		return Valor;
	}

	public void setValor(String valor) {
		Valor = valor;
	}

	public String getNomeFavorecido() {
		return nomeFavorecido;
	}

	public void setNomeFavorecido(String nomeFavorecido) {
		this.nomeFavorecido = nomeFavorecido;
	}

	public String getCpfFavoracido() {
		return cpfFavoracido;
	}

	public void setCpfFavoracido(String cpfFavoracido) {
		this.cpfFavoracido = cpfFavoracido;
	}

	public String getNumeroBancoFavorecido() {
		return numeroBancoFavorecido;
	}

	public void setNumeroBancoFavorecido(String numeroBancoFavorecido) {
		this.numeroBancoFavorecido = numeroBancoFavorecido;
	}

	public String getNomeBancoFavorecido() {
		return nomeBancoFavorecido;
	}

	public void setNomeBancoFavorecido(String nomeBancoFavorecido) {
		this.nomeBancoFavorecido = nomeBancoFavorecido;
	}

	public String getContaFavorecido() {
		return contaFavorecido;
	}

	public void setContaFavorecido(String contaFavorecido) {
		this.contaFavorecido = contaFavorecido;
	}

	public Calendar getData() {
		return data;
	}

	public void setData(Calendar data) {
		this.data = data;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
