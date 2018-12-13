package br.com.unika.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.ContatoDAO;
import br.com.unika.dao.UsuarioDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Contato;
import br.com.unika.modelo.Usuario;
import br.com.unika.util.Retorno;

public class ServicoContato implements IServico<Contato, Long>,Serializable {

	private static final long serialVersionUID = 1L;

	@SpringBean(name="contatoDAO")
	private ContatoDAO contatoDAO;
	

	

	@Override
	public Retorno incluir(Contato contato) {

		Retorno retorno = new Retorno(true, null);
		
		
		if (!retorno.isSucesso()) {
			return retorno;
		} 
	
		retorno = contatoDAO.salvarDAO(contato);
	
		return retorno;
	}

	@Override
	public Retorno alterar(Contato contato) {

		Retorno retorno = new Retorno(true, null);
		
		
		if (!retorno.isSucesso()) {
			return retorno;
		} 
		
		retorno = contatoDAO.alterarDAO(contato);

		return retorno;

	}

	@Override
	public Contato procurar(Contato contato) {
		if (contato != null && contato.getIdContato() != null) {
			
			return contatoDAO.procurarDAO(contato.getIdContato());
			
		} 
		return null;
	}

	@Override
	public List<Contato> listar() {
		return contatoDAO.listarDAO();
		

	}

	@Override
	public Retorno remover(Contato contato) {
		
		Retorno retorno = new Retorno(true, null);
	
		
		if (contato != null && contato.getIdContato() != null) {
				
				retorno = contatoDAO.removerDAO(contato);
			
		} else {
			System.out.println("Nenhuma PK inserida ou com o valor null");
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhuma PK inserida ou com o valor null");
		}
		return retorno;
	}

	@Override
	public List<Contato> search(Search search) {
		ArrayList<Contato> lista = new ArrayList<>();
		lista = (ArrayList<Contato>) contatoDAO.searchDAO(search);
		
		return lista;
	}
	
	public void setContatoDAO(ContatoDAO contatoDAO) {
		this.contatoDAO = contatoDAO;
	}
}
