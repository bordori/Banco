package br.com.unika.modelo;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.springframework.stereotype.Component;


@Entity
@Table(name = "usuario")
@Component
public class Usuario implements Serializable {

	private static final long serialVersionUID = 1L;
	
		
	@Id
	@GeneratedValue
	@Column(nullable = false, name = "ID_USUARIO", unique = true)
	private Long idUsuario;
	
	@Column(nullable = false, name = "NOME")
	private String nome;
	
	@Column(nullable = false, name = "SOBRENOME")
	private String sobrenome;
	
	@Column(nullable = false, name = "TELEFONE", unique = true)
	private String telefone;
	
	@Column(nullable = false, name = "CPF", unique = true)
	private String cpf;
	
	@Column(nullable = false, name = "EMAIL", unique = true)
	private String email;
	
	@Column(nullable = false, name = "DATA_NASCIMENTO")
	private Calendar dataNascimento;
	
	@Column(nullable = false, name = "CEP")
	private String cep;
	
	@Column(nullable = false, name = "ENDERECO")
	private String endereco;
	
	@Column(nullable = false, name = "NUMERO")
	private String numero;
	
	@Column(nullable = false, name = "COMPLEMENTO")
	private String complemento;
	
	@Column(nullable = false, name = "BAIRRO")
	private String bairro;
	
	@Column(nullable = false, name = "SEXO")
	private Boolean sexo; // true masculino false feminino
	
	@ManyToOne
	@JoinColumn(name="PERMISSAO_DE_ACESSO_ID", nullable= false)
	private PermissaoDeAcesso permissaoDeAcesso;
	
	@OneToMany(mappedBy = "usuario", targetEntity = Conta.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private Set<Conta> contas;
	
	@OneToMany(mappedBy = "usuario", targetEntity = Movimentacao.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private Set<Movimentacao> Movimentacoes;
	
	@OneToMany(mappedBy = "usuario", targetEntity = Contato.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
	private Set<Contato> contatos;
	
	@Column(name="ATIVO",nullable=false)
	private Boolean ativo;
	
	@Column(nullable = false, name = "LOGIN", unique = true)
	private String login;
	
	@Column(nullable = false, name = "SENHA")
	private String senha;
	
	

	public Long getIdUsuario() {
		return idUsuario;
	}

	public void setIdUsuario(Long idUsuario) {
		this.idUsuario = idUsuario;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getSobrenome() {
		return sobrenome;
	}

	public void setSobrenome(String sobrenome) {
		this.sobrenome = sobrenome;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public Calendar getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(Calendar dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getNumero() {
		return numero;
	}

	public void setNumero(String numero) {
		this.numero = numero;
	}

	public String getComplemento() {
		return complemento;
	}

	public void setComplemento(String complemento) {
		this.complemento = complemento;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public Boolean getSexo() {
		return sexo;
	}

	public void setSexo(Boolean sexo) {
		this.sexo = sexo;
	}

	public PermissaoDeAcesso getPermissaoDeAcesso() {
		return permissaoDeAcesso;
	}

	public void setPermissaoDeAcesso(PermissaoDeAcesso permissaoDeAcesso) {
		this.permissaoDeAcesso = permissaoDeAcesso;
	}

	public Set<Conta> getContas() {
		return contas;
	}

	public void setContas(Set<Conta> contas) {
		this.contas = contas;
	}

	public Set<Movimentacao> getMovimentacoes() {
		return Movimentacoes;
	}

	public void setMovimentacoes(Set<Movimentacao> movimentacoes) {
		Movimentacoes = movimentacoes;
	}

	public Set<Contato> getContatos() {
		return contatos;
	}

	public void setContatos(Set<Contato> contatos) {
		this.contatos = contatos;
	}

	public Boolean getAtivo() {
		return ativo;
	}

	public void setAtivo(Boolean ativo) {
		this.ativo = ativo;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	

		
	
	
}
