package br.com.unika.modelo;

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
@Table(name = "agencia")
@Component
public class Agencia {

	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(nullable = false, name = "ID_AGENCIA", unique = true)
	private Long idAgencia;
	
	@Column(nullable = false, name = "NUMERO")
	private String numero;
	
	@ManyToOne
	@JoinColumn(name = "BANCO_ID" , nullable=false)
	private Banco banco;
	

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
	
}
