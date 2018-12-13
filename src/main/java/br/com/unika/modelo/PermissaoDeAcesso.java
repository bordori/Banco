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
	
	@Column(nullable = false, name = "ALTERAR_BANCO")
	private boolean alterarBanco;
	
	@Column(nullable = false, name = "ALTERAR_CONTA")
	private boolean alterarConta;
	
	@Column(nullable = false, name = "ALTERAR_PERMISSOES")
	private boolean alterarPermissoes;
	
	@OneToMany(mappedBy = "permissaoDeAcesso",targetEntity= Usuario.class ,fetch = FetchType.EAGER ,cascade = CascadeType.MERGE)
	private List<Usuario> usuarios;
	
	
	public Long getIdPermissao() {
		return idPermissao;
	}
	public void setIdPermissao(Long idPermissao) {
		this.idPermissao = idPermissao;
	}
	public boolean isAlterarBanco() {
		return alterarBanco;
	}
	public void setAlterarBanco(boolean alterarBanco) {
		this.alterarBanco = alterarBanco;
	}
	public boolean isAlterarConta() {
		return alterarConta;
	}
	public void setAlterarConta(boolean alterarConta) {
		this.alterarConta = alterarConta;
	}
	public boolean isAlterarPermissoes() {
		return alterarPermissoes;
	}
	public void setAlterarPermissoes(boolean alterarPermissoes) {
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
