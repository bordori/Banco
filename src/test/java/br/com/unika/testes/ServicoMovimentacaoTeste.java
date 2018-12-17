package br.com.unika.testes;

import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Calendar;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.Movimentacao;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoMovimentacao;
import br.com.unika.util.Retorno;


@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "file:src/main/webapp/WEB-INF/applicationContext.xml")
public class ServicoMovimentacaoTeste {

	@Autowired
	private ServicoMovimentacao servicoMovimentacao;
	
	@Test
	public void testIncluir() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setTipoMovimentacao(2);
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(new Long(3));
		movimentacao.setUsuario(usuario);
		movimentacao.setNomeFavorecido("Thor Filho de Odin");
		movimentacao.setCpfFavoracido("545.578.457-85");
		movimentacao.setNumeroBancoFavorecido("1");
		movimentacao.setNomeBancoFavorecido("Brasil");
		movimentacao.setAgenciaFavorecido("0015");
		movimentacao.setContaFavorecido("12345678");
		movimentacao.setValor(55.15);
		Calendar c = Calendar.getInstance();
		movimentacao.setData(c);
		
		Retorno retorno = servicoMovimentacao.incluir(movimentacao);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testAlterar() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setIdMovimentacao(new Long(1));
		movimentacao.setTipoMovimentacao(2);
		Usuario usuario = new Usuario();
		usuario.setIdUsuario(new Long(3));
		movimentacao.setUsuario(usuario);
		movimentacao.setNomeFavorecido("Thor Filho de Odin");
		movimentacao.setCpfFavoracido("545.578.457-85");
		movimentacao.setNumeroBancoFavorecido("1");
		movimentacao.setNomeBancoFavorecido("Brasil");
		movimentacao.setAgenciaFavorecido("0015");
		movimentacao.setContaFavorecido("12345678");
		movimentacao.setValor(100.15);
		Calendar c = Calendar.getInstance();
		movimentacao.setData(c);
		
		Retorno retorno = servicoMovimentacao.alterar(movimentacao);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testProcurar() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setIdMovimentacao(new Long(1));
		
		Movimentacao retorno = servicoMovimentacao.procurar(movimentacao);
		
		assertNotNull(retorno);
	}

	@Test
	public void testListar() {
		ArrayList<Movimentacao> retorno = (ArrayList<Movimentacao>) servicoMovimentacao.listar();
		
		assertEquals(false, retorno.isEmpty());
	}

	@Test
	public void testRemover() {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setIdMovimentacao(new Long(1));
		
		Retorno retorno = servicoMovimentacao.remover(movimentacao);
		
		assertEquals(true, retorno.isSucesso());
	}

	@Test
	public void testSearch() {
		Search search = new Search(Movimentacao.class);
		search.addFilterILike("nomeFavorecido", "%Thor%");
		
		ArrayList<Movimentacao> retorno = (ArrayList<Movimentacao>) servicoMovimentacao.search(search);
		
		assertEquals(false, retorno.isEmpty());
	}

}
