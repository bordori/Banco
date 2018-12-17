package br.com.unika.testes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.Contato;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoContato;
import br.com.unika.util.Retorno;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")
public class ServicoContatoTeste {

	@Autowired
	private ServicoContato servicoContato; 
	
	@Test
	public void testIncluir() {
		Contato contato = new Contato();
		contato.setApelido("Jão");
		contato.setCpf("700.987.258-56");
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(new Long(3));
		contato.setUsuario(usuario);
		contato.setConta("56127486");
		contato.setAgencia("0025");
		contato.setNumeroBanco("104");
		contato.setNomeBanco("Caixa");
		
		Retorno retorno = servicoContato.incluir(contato);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testAlterar() {
		Contato contato = new Contato();
		contato.setIdContato(new Long(1));
		contato.setApelido("joão");
		contato.setCpf("700.987.258-56");
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(new Long(3));
		contato.setUsuario(usuario);
		contato.setConta("56127486");
		contato.setAgencia("0025");
		contato.setNumeroBanco("1");
		contato.setNomeBanco("Brasil");
		
		Retorno retorno = servicoContato.alterar(contato);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testProcurar() {
		Contato contato = new Contato();
		contato.setIdContato(new Long(1));
		
		
		Contato retorno = servicoContato.procurar(contato);
		
		assertNotNull(retorno);
	}

	@Test
	public void testListar() {
		ArrayList<Contato> retorno = (ArrayList<Contato>) servicoContato.listar();
		
		assertEquals(false, retorno.isEmpty());
	}

	@Test
	public void testRemover() {
		Contato contato = new Contato();
		contato.setIdContato(new Long(1));
		
		
		Retorno retorno = servicoContato.remover(contato);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testSearch() {
		Search search = new Search(Contato.class);
		search.addFilterILike("apelido", "%joao%");
		
		ArrayList<Contato> retorno = (ArrayList<Contato>) servicoContato.search(search);
		
		assertEquals(false, retorno.isEmpty());
	}

}
