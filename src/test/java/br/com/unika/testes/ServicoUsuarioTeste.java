package br.com.unika.testes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate3.support.OpenSessionInViewInterceptor;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.PermissaoDeAcesso;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoUsuario;
import br.com.unika.util.Retorno;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")
public class ServicoUsuarioTeste {

	@Autowired
	private ServicoUsuario servicoUsuario;
	
	
	OpenSessionInViewInterceptor open;

	@Test public void testIncluir() {
		Usuario usuario = new Usuario();
		usuario.setNome("  Jean   Carlo");
		usuario.setSobrenome("         jjkj");
		usuario.setTelefone("(62)99115-3602");
		usuario.setCpf("700.794.811-16");
		usuario.setEmail("jeanbordori@gmail.com");
		Calendar c = Calendar.getInstance();
		c.set(1996, 01, 26);
		usuario.setDataNascimento(c);
		usuario.setCep("75144-610");
		usuario.setEndereco("Rua Matadouro Industrial");
		usuario.setNumero("15");
		usuario.setComplemento("final da rua ");
		usuario.setBairro("Vila Fabril");
		usuario.setSexo(true);
		usuario.setLogin("bordori22");
		usuario.setSenha("abrenhosa1");
		usuario.setAtivo(true);
		PermissaoDeAcesso permissaoDeAcesso = new PermissaoDeAcesso();
		permissaoDeAcesso.setIdPermissao(new Long(1));
		usuario.setPermissaoDeAcesso(permissaoDeAcesso);
		
		Retorno retorno = servicoUsuario.incluir(usuario);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testAlterar() {
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(new Long(2));
		usuario.setNome("Jean Carlo");
		usuario.setSobrenome("abrenhosa bordori");
		usuario.setTelefone("(62)99115-3602");
		usuario.setCpf("700.794.811-16");
		usuario.setEmail("jeanbordori@gmail.com");
		Calendar c = Calendar.getInstance();
		c.set(1996, 0, 27);
		usuario.setDataNascimento(c);
		usuario.setCep("75144-610");
		usuario.setEndereco("Rua Matadouro Industrial");
		usuario.setNumero("15");
		usuario.setComplemento("final da rua ");
		usuario.setBairro("Vila Fabril");
		usuario.setSexo(true);
		usuario.setLogin("bordori");
		usuario.setSenha("abrenhosa");
		PermissaoDeAcesso permissaoDeAcesso = new PermissaoDeAcesso();
		permissaoDeAcesso.setIdPermissao(new Long(3));
		usuario.setPermissaoDeAcesso(permissaoDeAcesso);
		
		Retorno retorno = servicoUsuario.alterar(usuario);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testProcurar() {
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(new Long(1));
		
		Usuario retorno = servicoUsuario.procurar(usuario);
		
		assertNotNull(retorno);
	}

	@Test
	public void testListar() {
		ArrayList<Usuario> retorno = new ArrayList<>();
		retorno = (ArrayList<Usuario>) servicoUsuario.listar();
		assertEquals(false, retorno.isEmpty());
	}

	

	@Test
	public void testSearch() {
		Search search = new Search(Usuario.class);
		search.addFilterILike("nome", "%jean%");

		ArrayList<Usuario> retorno = (ArrayList<Usuario>) servicoUsuario.search(search);

		assertEquals(false,retorno.isEmpty() );
	}
	
	@Test
	public void testRemover() {
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(new Long(1));
		
		Retorno retorno = servicoUsuario.remover(usuario);
		
		assertEquals(true, retorno.isSucesso());
		
	}

}
