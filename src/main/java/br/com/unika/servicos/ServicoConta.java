package br.com.unika.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.ContaDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Conta;
import br.com.unika.util.Retorno;

public class ServicoConta implements IServico<Conta, Long>,Serializable{
private static final long serialVersionUID = 1L;
	
	@SpringBean(name="contaDAO")
	ContaDAO contaDAO;
	

	

	@Override
	public Retorno incluir(Conta conta) {

		Retorno retorno = new Retorno(true, null);
		
		
		if (!retorno.isSucesso()) {
			return retorno;
		} 
	
		retorno = contaDAO.salvarDAO(conta);
	
		return retorno;
	}

	@Override
	public Retorno alterar(Conta conta) {

		Retorno retorno = new Retorno(true, null);
		
		
		if (!retorno.isSucesso()) {
			return retorno;
		} 
		
		retorno = contaDAO.alterarDAO(conta);

		return retorno;

	}

	@Override
	public Conta procurar(Conta conta) {
		if (conta != null && conta.getIdConta() != null) {
			
			return contaDAO.procurarDAO(conta.getIdConta());
			
		} 
		return null;
	}

	@Override
	public List<Conta> listar() {
		return contaDAO.listarDAO();
		

	}

	@Override
	public Retorno remover(Conta conta) {
		
		Retorno retorno = new Retorno(true, null);
	
		
		if (conta != null && conta.getIdConta() != null) {
				
				retorno = contaDAO.removerDAO(conta);
			
		} else {
			System.out.println("Nenhuma PK inserida ou com o valor null");
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhuma PK inserida ou com o valor null");
		}
		return retorno;
	}

	@Override
	public List<Conta> search(Search search) {
		ArrayList<Conta> lista = new ArrayList<>();
		lista = (ArrayList<Conta>) contaDAO.searchDAO(search);
		
		return lista;
	}
	
	public void setContaDAO(ContaDAO contaDAO) {
		this.contaDAO = contaDAO;
	}

}
