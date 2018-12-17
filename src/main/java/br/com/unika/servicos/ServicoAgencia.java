package br.com.unika.servicos;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.AgenciaDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Agencia;
import br.com.unika.util.Retorno;

public class ServicoAgencia implements IServico<Agencia, Long> {

	@SpringBean(name = "agenciaDAO")
	private AgenciaDAO agenciaDAO;

	@Override
	public Retorno incluir(Agencia agencia) {

		Retorno retorno = new Retorno(true, null);

		if (!retorno.isSucesso()) {
			return retorno;
		}

		retorno = agenciaDAO.salvarDAO(agencia);

		return retorno;
	}

	@Override
	public Retorno alterar(Agencia agencia) {

		Retorno retorno = new Retorno(true, null);

		if (!retorno.isSucesso()) {
			return retorno;
		}

		retorno = agenciaDAO.alterarDAO(agencia);

		return retorno;

	}

	@Override
	public Agencia procurar(Agencia agencia) {
		if (agencia != null && agencia.getIdAgencia() != null) {

			return agenciaDAO.procurarDAO(agencia.getIdAgencia());

		}
		return null;
	}

	@Override
	public List<Agencia> listar() {
		return agenciaDAO.listarDAO();

	}

	@Override
	public Retorno remover(Agencia agencia) {

		Retorno retorno = new Retorno(true, null);

		if (agencia != null && agencia.getIdAgencia() != null) {

			retorno = agenciaDAO.removerDAO(agencia);

		} else {
			System.out.println("Nenhuma PK inserida ou com o valor null");
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhuma PK inserida ou com o valor null");
		}
		return retorno;
	}

	@Override
	public List<Agencia> search(Search search) {
		ArrayList<Agencia> lista = new ArrayList<>();
		lista = (ArrayList<Agencia>) agenciaDAO.searchDAO(search);

		return lista;
	}

	public void setAgenciaDAO(AgenciaDAO agenciaDAO) {
		this.agenciaDAO = agenciaDAO;
	}
}
