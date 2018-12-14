package br.com.unika.modelo;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.springframework.stereotype.Component;


@Entity
@Table(name = "conta")
@Component
public class Conta implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(nullable = false, name = "ID_CONTA", unique = true)
	private Long idConta;
	
	@Column(nullable = false, name = "CONTA",unique = true)
	private String conta;
	
	@Column(nullable = false, name = "DIGITO_CONTA")
	private String digitoConta;
	
	@Column(nullable = false, name = "AGENCIA")
	private String agencia;
	
	@Column( name = "DIGITO_AGENCIA")
	private String digitoAgencia;
	
	@Column(nullable = false, name = "TIPO_CONTA")
	private Integer tipoConta;
	
	@Column(nullable = false, name = "ATIVO")
	private Boolean ativo;
	
	@Column(nullable = false, name = "SALDO")
	private Double saldo;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_ID" , nullable=false)
	private Banco banco;
	
	@ManyToOne
	@JoinColumn(name = "USUARIO_ID" , nullable=false)
	private Usuario usuario;

	
	
	public Long getIdConta() {
		return idConta;
	}

	public void setIdConta(Long idConta) {
		this.idConta = idConta;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public String getDigitoConta() {
		return digitoConta;
	}

	public void setDigitoConta(String digitoConta) {
		this.digitoConta = digitoConta;
	}

	public String getAgencia() {
		return agencia;
	}

	public void setAgencia(String agencia) {
		this.agencia = agencia;
	}

	public String getDigitoAgencia() {
		return digitoAgencia;
	}

	public void setDigitoAgencia(String digitoAgencia) {
		this.digitoAgencia = digitoAgencia;
	}

	public Integer getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(Integer tipoConta) {
		this.tipoConta = tipoConta;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public Double getSaldo() {
		return saldo;
	}

	public void setSaldo(Double saldo) {
		this.saldo = saldo;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}
