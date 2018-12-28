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

import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoConta;
import br.com.unika.util.Retorno;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")
public class ServicoContaTeste {
	
	@Autowired
	private ServicoConta servicoConta;

	@Test
	public void testIncluir() {
		Conta conta = new Conta();
		conta.setTipoConta(1);
		conta.setAtivo(true);
		conta.setSaldo(25.00);
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(new Long(1));
		conta.setUsuario(usuario);
		Banco banco = new Banco();
		banco.setIdBanco(new Long(1));
		
		Retorno retorno = servicoConta.incluir(conta);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testAlterar() {
		Conta conta = new Conta();
		conta.setIdConta(new Long(1));
		conta.setConta("51248475");
		conta.setTipoConta(1);
		conta.setAtivo(true);
		conta.setSaldo(100.00);
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(new Long(3));
		conta.setUsuario(usuario);
		Banco banco = new Banco();
		banco.setIdBanco(new Long(2));
		
		Retorno retorno = servicoConta.alterar(conta);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testProcurar() {
		Conta conta = new Conta();
		conta.setIdConta(new Long(1));
		
		
		Conta retorno = servicoConta.procurar(conta);
		
		assertNotNull(retorno);
	}

	@Test
	public void testListar() {
		ArrayList<Conta> retorno = (ArrayList<Conta>) servicoConta.listar();
		
		assertEquals(false, retorno.isEmpty());
	}

	@Test
	public void testRemover() {
		Conta conta = new Conta();
		conta.setIdConta(new Long(1));
		
		
		Retorno retorno = servicoConta.remover(conta);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testSearch() {
		Search search = new Search(Conta.class);
		search.addFilterEqual("ativo", true);
		
		ArrayList<Conta> retorno = (ArrayList<Conta>) servicoConta.search(search);
		
		assertEquals(false, retorno.isEmpty());
		
	}
	
	@Test
	public void testSearchConta() {
		Search search = new Search(Conta.class);
		
		Banco banco = new Banco();
		banco.setIdBanco(new Long(1));
		Agencia agencia =  new Agencia();
		agencia.setIdAgencia(new Long(1));
		servicoConta.gerarConta(banco,agencia);
		
		//assertEquals(false, retorno.isEmpty());
	}

}
