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

	@ManyToOne
	@JoinColumn(name = "AGENCIA_ID", nullable = false)
	private Agencia agencia;

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

	public Agencia getAgencia() {
		return agencia;
	}

	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
