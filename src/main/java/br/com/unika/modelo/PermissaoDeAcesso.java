package br.com.unika.modelo;

public class PermissaoDeAcesso {
	
	private Long idPermissao;
	private boolean alterarBanco;
	private boolean alterarConta;
	
	
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
	
}
