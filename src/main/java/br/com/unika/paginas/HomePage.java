package br.com.unika.paginas;

import org.apache.wicket.spring.injection.annot.SpringBean;

import br.com.unika.modelo.PermissaoDeAcesso;
import br.com.unika.servicos.ServicoPermissaoDeAcesso;
import br.com.unika.util.Retorno;

public class HomePage extends NavBar {
	private static final long serialVersionUID = 1L;
	
	@SpringBean(name="servicoPermissaoDeAcesso")
	private ServicoPermissaoDeAcesso servicoPermissaoDeAcesso;
	
	public HomePage() {
		PermissaoDeAcesso permissao = new PermissaoDeAcesso();
		permissao.setAlterarPermissoes(true);
		permissao.setAlterarBanco(true);
		permissao.setAlterarConta(true);
		
		Retorno retorno = servicoPermissaoDeAcesso.incluir(permissao);
		
	}

}
