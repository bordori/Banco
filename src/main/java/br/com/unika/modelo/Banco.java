package br.com.unika.modelo;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;


import org.springframework.stereotype.Component;

@Entity
@Table(name = "banco")
@Component
public class Banco implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(nullable = false,name ="ID_BANCO",unique = true)
	private Long idBanco;
	
	@Column(nullable = false, name = "NUMERO" ,unique=true)
	private String numero;
	
	@Column(nullable = false, name = "NOME",unique=true)
	private String nome;
	
	@OneToMany(mappedBy="banco", targetEntity = Conta.class,  fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private Set<Conta> conta;
	
	@OneToMany(mappedBy="banco", targetEntity = Agencia.class,  fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<Agencia> agencia;
	
	
	public Long getIdBanco() {
		return idBanco;
	}
	public void setIdBanco(Long idBanco) {
		this.idBanco = idBanco;
	}
	public String getNumero() {
		return numero;
	}
	public void setNumero(String numero) {
		this.numero = numero;
	}
	public String getNome() {
		return nome;
	}
	public void setNome(String nome) {
		this.nome = nome;
	}
	public Set<Conta> getConta() {
		return conta;
	}
	public void setConta(Set<Conta> conta) {
		this.conta = conta;
	}
	public Set<Agencia> getAgencia() {
		return agencia;
	}
	public void setAgencia(Set<Agencia> agencia) {
		this.agencia = agencia;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
}
