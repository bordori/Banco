package br.com.unika.testes;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.Banco;
import br.com.unika.servicos.ServicoBanco;
import br.com.unika.util.Retorno;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")
public class ServicoBancoTeste {

	@Autowired
	private ServicoBanco servicoBanco;
	
	@Test
	public void testIncluir() {
		Banco banco = new Banco();
		banco.setNome("Brasil");
		banco.setNumero("1");
		
		Banco banco2 = new Banco();
		banco2.setNome("Caixa");
		banco2.setNumero("104");
		
		Retorno retorno = servicoBanco.incluir(banco);
		Retorno retorno2 = servicoBanco.incluir(banco2);
		assertEquals(true, retorno.isSucesso());
		assertEquals(true, retorno2.isSucesso());
		
	}

	@Test
	public void testAlterar() {
		Banco banco = new Banco();
		banco.setIdBanco(new Long(1));
		banco.setNome("Caixa");
		banco.setNumero("104");
		
		Retorno retorno = servicoBanco.alterar(banco);
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testProcurar() {
		Banco banco = new Banco();
		banco.setIdBanco(new Long(1));
		
		Banco retorno = servicoBanco.procurar(banco);
		assertNotNull(retorno);
	}

	@Test
	public void testListar() {
		Banco banco = new Banco();
		banco.setNome("Brasil");
		banco.setNumero("1");
		
		ArrayList<Banco> retorno = (ArrayList<Banco>) servicoBanco.listar();
		assertEquals(false, retorno.isEmpty());
	}

	@Test
	public void testRemover() {
		Banco banco = new Banco();
		banco.setIdBanco(new Long(2));
		
		
		Retorno retorno = servicoBanco.remover(banco);
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testSearch() {
		Search search = new Search(Banco.class);
		search.addFilterEqual("nome", "Caixa");

		ArrayList<Banco> retorno = (ArrayList<Banco>) servicoBanco.search(search);

		assertEquals(false,retorno.isEmpty() );
	}

	

}
