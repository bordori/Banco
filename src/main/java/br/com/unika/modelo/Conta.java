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

import org.apache.regexp.recompile;
import org.springframework.stereotype.Component;

@Entity
@Table(name = "conta")
@Component
public class Conta implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(nullable = false, name = "ID_CONTA", unique = true)
	private Long idConta;

	@Column(nullable = false, name = "CONTA")
	private String conta;

	@Column(nullable = false, name = "TIPO_CONTA")
	private Integer tipoConta;

	@Column(nullable = false, name = "ATIVO")
	private Boolean ativo;

	@Column(nullable = false, name = "SALDO")
	private Double saldo;

	@ManyToOne
	@JoinColumn(name = "ID_AGENCIA", nullable = false)
	private Agencia agencia;

	@ManyToOne
	@JoinColumn(name = "ID_USUARIO", nullable = false)
	private Usuario usuario;

	@OneToMany(mappedBy = "conta", targetEntity = Movimentacao.class, fetch = FetchType.EAGER, cascade = CascadeType.REMOVE)
	private Set<Movimentacao> Movimentacoes;

	@Override
	public String toString() {
		return "Conta [idConta=" + idConta + ", conta=" + conta + ", tipoConta=" + tipoConta + ", ativo=" + ativo
				+ ", saldo=" + saldo + ", agencia=" + agencia + ", usuario=" + usuario + ", Movimentacoes="
				+ Movimentacoes + "]";
	}

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

	public Usuario getUsuario() {
		return usuario;
	}

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

	public Agencia getAgencia() {
		return agencia;
	}

	public void setAgencia(Agencia agencia) {
		this.agencia = agencia;
	}

	public Set<Movimentacao> getMovimentacoes() {
		return Movimentacoes;
	}

	public void setMovimentacoes(Set<Movimentacao> movimentacoes) {
		Movimentacoes = movimentacoes;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

}
