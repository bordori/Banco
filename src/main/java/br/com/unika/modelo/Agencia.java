package br.com.unika.modelo;

import java.io.Serializable;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;

@Entity
@Table(name = "agencia")
@Component
public class Agencia implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, name = "ID_AGENCIA", unique = true)
	private Long idAgencia;

	@Column(nullable = false, name = "NUMERO")
	private String numero;
	
	@Column(nullable = false, name = "NOME")
	private String nome;

	@ManyToOne
	@JoinColumn(name = "BANCO_ID", nullable = false)
	private Banco banco;
	
	@OneToMany(mappedBy = "agencia", targetEntity = Conta.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private Set<Conta> conta;
	
	@OneToMany(mappedBy = "agencia", targetEntity = Contato.class, fetch = FetchType.LAZY, cascade = CascadeType.ALL)
	private Set<Contato> contato;

	public String getNumeroNomeAgencia() {
		return getNumero()+"-"+getNome();
	}
	
	public Long getIdAgencia() {
		return idAgencia;
	}

	public void setIdAgencia(Long idAgencia) {
		this.idAgencia = idAgencia;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public Banco getBanco() {
		return banco;
	}

	public void setBanco(Banco banco) {
		this.banco = banco;
	}

	public Set<Conta> getConta() {
		return conta;
	}

	public void setConta(Set<Conta> conta) {
		this.conta = conta;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public Set<Contato> getContato() {
		return contato;
	}

	public void setContato(Set<Contato> contato) {
		this.contato = contato;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	

}
