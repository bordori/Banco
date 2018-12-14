package br.com.unika.modelo;

import java.io.Serializable;
import java.util.List;

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
@Table(name = "permissoes_de_acesso")
@Component
public class PermissaoDeAcesso implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(nullable = false, name = "ID_PERMISSAO", unique = true)
	private Long idPermissao;
	
	@Column(nullable = false , name ="DESCRICAO")
	private String descricao;
	
	@Column(nullable = false, name = "ALTERAR_BANCO")
	private Boolean alterarBanco;
	
	@Column(nullable = false, name = "ALTERAR_CONTA")
	private Boolean alterarConta;
	
	@Column(nullable = false, name = "ALTERAR_PERMISSOES")
	private Boolean alterarPermissoes;
	
	@OneToMany(mappedBy = "permissaoDeAcesso",targetEntity= Usuario.class ,fetch = FetchType.EAGER ,cascade = CascadeType.MERGE)
	private List<Usuario> usuarios;

	public Long getIdPermissao() {
		return idPermissao;
	}

	public void setIdPermissao(Long idPermissao) {
		this.idPermissao = idPermissao;
	}

	public String getDescricao() {
		return descricao;
	}

	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}

	public Boolean getAlterarBanco() {
		return alterarBanco;
	}

	public void setAlterarBanco(Boolean alterarBanco) {
		this.alterarBanco = alterarBanco;
	}

	public Boolean getAlterarConta() {
		return alterarConta;
	}

	public void setAlterarConta(Boolean alterarConta) {
		this.alterarConta = alterarConta;
	}

	public Boolean getAlterarPermissoes() {
		return alterarPermissoes;
	}

	public void setAlterarPermissoes(Boolean alterarPermissoes) {
		this.alterarPermissoes = alterarPermissoes;
	}

	public List<Usuario> getUsuarios() {
		return usuarios;
	}

	public void setUsuarios(List<Usuario> usuarios) {
		this.usuarios = usuarios;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
	
}
