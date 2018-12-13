package br.com.unika.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.MovimentacaoDAO;
import br.com.unika.dao.UsuarioDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Movimentacao;
import br.com.unika.modelo.Usuario;
import br.com.unika.util.Retorno;

public class ServicoMovimentacao implements IServico<Movimentacao, Long>,Serializable {

	private static final long serialVersionUID = 1L;

	@SpringBean(name="movimentacaoDAO")
	private MovimentacaoDAO movimentacaoDAO;
	

	

	@Override
	public Retorno incluir(Movimentacao movimentacao) {

		Retorno retorno = new Retorno(true, null);
		
		
		if (!retorno.isSucesso()) {
			return retorno;
		} 
	
		retorno = movimentacaoDAO.salvarDAO(movimentacao);
	
		return retorno;
	}

	@Override
	public Retorno alterar(Movimentacao movimentacao) {

		Retorno retorno = new Retorno(true, null);
		
		
		if (!retorno.isSucesso()) {
			return retorno;
		} 
		
		retorno = movimentacaoDAO.alterarDAO(movimentacao);

		return retorno;

	}

	@Override
	public Movimentacao procurar(Movimentacao movimentacao) {
		if (movimentacao != null && movimentacao.getIdMovimentacao() != null) {
			
			return movimentacaoDAO.procurarDAO(movimentacao.getIdMovimentacao());
			
		} 
		return null;
	}

	@Override
	public List<Movimentacao> listar() {
		return movimentacaoDAO.listarDAO();
		

	}

	@Override
	public Retorno remover(Movimentacao movimentacao) {
		
		Retorno retorno = new Retorno(true, null);
	
		
		if (movimentacao != null && movimentacao.getIdMovimentacao() != null) {
				
				retorno = movimentacaoDAO.removerDAO(movimentacao);
			
		} else {
			System.out.println("Nenhuma PK inserida ou com o valor null");
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhuma PK inserida ou com o valor null");
		}
		return retorno;
	}

	@Override
	public List<Movimentacao> search(Search search) {
		ArrayList<Movimentacao> lista = new ArrayList<>();
		lista = (ArrayList<Movimentacao>) movimentacaoDAO.searchDAO(search);
		
		return lista;
	}
	
	public void setMovimentacaoDAO(MovimentacaoDAO movimentacaoDAO) {
		this.movimentacaoDAO = movimentacaoDAO;
	}
}
