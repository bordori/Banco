package br.com.unika.modelo;

import java.io.Serializable;

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
@Table(name = "contato")
@Component
public class Contato implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, name = "ID_CONTATO", unique = true)
	private Long idContato;

	@Column(nullable = false, name = "APELIDO")
	private String apelido;

	@Column(nullable = false, name = "CPF")
	private String cpf;

	@ManyToOne
	@JoinColumn(name = "USUARIO_ID", nullable = false)
	private Usuario usuario;

	@Column(nullable = false, name = "NUMERO_BANCO")
	private String numeroBanco;

	@Column(nullable = false, name = "NOME_BANCO")
	private String nomeBanco;

	@Column(nullable = false, name = "NUMERO_AGENCIA")
	private String numeroAgencia;

	@Column(nullable = false, name = "NOME_AGENCIA")
	private String nomeAgencia;

	@Column(nullable = false, name = "CONTA")
	private String conta;

	@Column(nullable = false, name = "TIPO_CONTA")
	private Integer tipoConta;

	public Long getIdContato() {
		return idContato;
	}

	public void setIdContato(Long idContato) {
		this.idContato = idContato;
	}

	public String getApelido() {
		return apelido;
	}

	public void setApelido(String apelido) {
		this.apelido = apelido;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public String getNumeroBanco() {
		return numeroBanco;
	}

	public void setNumeroBanco(String numeroBanco) {
		this.numeroBanco = numeroBanco;
	}

	public String getNomeBanco() {
		return nomeBanco;
	}

	public void setNomeBanco(String nomeBanco) {
		this.nomeBanco = nomeBanco;
	}

	public String getConta() {
		return conta;
	}

	public void setConta(String conta) {
		this.conta = conta;
	}

	public Integer getTipoConta() {
		return tipoConta;
	}

	public void setTipoConta(Integer tipoConta) {
		this.tipoConta = tipoConta;
	}

	public String getNumeroAgencia() {
		return numeroAgencia;
	}

	public void setNumeroAgencia(String numeroAgencia) {
		this.numeroAgencia = numeroAgencia;
	}

	public String getNomeAgencia() {
		return nomeAgencia;
	}

	public void setNomeAgencia(String nomeAgencia) {
		this.nomeAgencia = nomeAgencia;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
