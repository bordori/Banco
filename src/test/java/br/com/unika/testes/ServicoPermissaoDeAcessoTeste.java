package br.com.unika.testes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.apache.wicket.spring.injection.annot.SpringBean;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.PermissaoDeAcesso;
import br.com.unika.servicos.ServicoPermissaoDeAcesso;
import br.com.unika.util.Retorno;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")
public class ServicoPermissaoDeAcessoTeste {

	@Autowired
	private ServicoPermissaoDeAcesso servicoPermissaoDeAcesso;

	@Test
	public void testIncluir() {
		PermissaoDeAcesso permissao = new PermissaoDeAcesso();
		permissao.setAlterarPermissoes(true);
		permissao.setAlterarBanco(true);
		permissao.setAlterarConta(true);

		Retorno retorno = servicoPermissaoDeAcesso.incluir(permissao);

		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testAlterar() {
		PermissaoDeAcesso permissao = new PermissaoDeAcesso();
		permissao.setIdPermissao(new Long(1));
		permissao.setAlterarPermissoes(false);
		permissao.setAlterarBanco(false);
		permissao.setAlterarConta(false);

		Retorno retorno = servicoPermissaoDeAcesso.alterar(permissao);

		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testProcurar() {
		PermissaoDeAcesso permissao = new PermissaoDeAcesso();
		permissao.setIdPermissao(new Long(1));
		permissao.setAlterarPermissoes(false);
		permissao.setAlterarBanco(false);
		permissao.setAlterarConta(false);

		PermissaoDeAcesso retorno = servicoPermissaoDeAcesso.procurar(permissao);

		assertNotNull(retorno);
	}

	@Test
	public void testListar() {
		

		ArrayList<PermissaoDeAcesso> retorno = (ArrayList<PermissaoDeAcesso>) servicoPermissaoDeAcesso.listar();

		assertEquals(false,retorno.isEmpty() );
	}

	@Test
	public void testRemover() {
		PermissaoDeAcesso permissao = new PermissaoDeAcesso();
		permissao.setIdPermissao(new Long(2));
		

		Retorno retorno = servicoPermissaoDeAcesso.remover(permissao);

		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testSearch() {
		Search search = new Search(PermissaoDeAcesso.class);
		search.addFilterEqual("alterarBanco", false);

		ArrayList<PermissaoDeAcesso> retorno = (ArrayList<PermissaoDeAcesso>) servicoPermissaoDeAcesso.search(search);

		assertEquals(false,retorno.isEmpty() );
	}

	

}
