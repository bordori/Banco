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

	@Column(name = "NOME_FAVORECIDO", nullable = false)
	private String nomeFavorecido;

	@Column(name = "CPF_FAVORECIDO", nullable = false)
	private String cpfFavoracido;

	@Column(name = "NUMERO_BANCO_FAVORECIDO", nullable = false)
	private String numeroBancoFavorecido;

	@Column(name = "NOME_BANCO_FAVORECIDO", nullable = false)
	private String nomeBancoFavorecido;

	@Column(name = "AGENCIA_FAVORECIDO", nullable = false)
	private String agenciaFavorecido;

	@Column(name = "CONTA_FAVORECIDO", nullable = false)
	private String contaFavorecido;

	@Column(nullable = false, name = "VALOR")
	private Double Valor;

	@Column(nullable = false, name = "DATA")
	private Calendar data;

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

	public Double getValor() {
		return Valor;
	}

	public void setValor(Double valor) {
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

	public String getAgenciaFavorecido() {
		return agenciaFavorecido;
	}

	public void setAgenciaFavorecido(String agenciaFavorecido) {
		this.agenciaFavorecido = agenciaFavorecido;
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
