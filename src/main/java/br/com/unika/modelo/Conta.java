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
	
	@Column(nullable = false, name = "CONTA")
	private String conta;
	
	@Column(nullable = false, name = "DIGITO_CONTA")
	private String digitoConta;
	
	@Column(nullable = false, name = "AGENCIA")
	private String agencia;
	
	@Column(nullable = false, name = "DIGITO_AGENCIA")
	private String digitoAgencia;
	
	@Column(nullable = false, name = "TIPO_CONTA")
	private int tipoConta;
	
	@Column(nullable = false, name = "ATIVO")
	private boolean ativo;
	
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
	public boolean isAtivo() {
		return ativo;
	}
	public void setAtivo(boolean ativo) {
		this.ativo = ativo;
	}
	public Banco getBanco() {
		return banco;
	}
	public void setBanco(Banco banco) {
		this.banco = banco;
	}
	public int getTipoConta() {
		return tipoConta;
	}
	public void setTipoConta(int tipoConta) {
		this.tipoConta = tipoConta;
	}
	public Usuario getUsuario() {
		return usuario;
	}
	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}
	
}
