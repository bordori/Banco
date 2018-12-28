package br.com.unika.servicos;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.AgenciaDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ServicoAgencia implements IServico<Agencia, Long> {

	@SpringBean(name = "agenciaDAO")
	private AgenciaDAO agenciaDAO;
	
	@SpringBean(name="servicoConta")
	private ServicoConta ServicoConta;

	@Override
	public Retorno incluir(Agencia agencia) {

		Retorno retorno = new Retorno(true, null);
		agencia = (Agencia) Validacao.retiraEspacoDesnecessarios(agencia);
		retorno =  validacaoDeNegocio(agencia);

		if (!retorno.isSucesso()) {
			return retorno;
		}

		retorno = agenciaDAO.salvarDAO(agencia);

		return retorno;
	}

	@Override
	public Retorno alterar(Agencia agencia) {

		Retorno retorno = new Retorno(true, null);
		agencia = (Agencia) Validacao.retiraEspacoDesnecessarios(agencia);
		retorno =  validacaoDeNegocio(agencia);
		
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
			if(verificaSeTemContas(agencia) == 0) {
				retorno = agenciaDAO.removerDAO(agencia);

			}else {
				retorno.setSucesso(false);
				retorno.addMensagem("A Agência Não Deve Ter Contas Para Exclusão");	
			}

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
	
	
	@Override
	public int count(Search search) {
		return agenciaDAO.countDAO(search);
	}

	
	private Retorno validacaoDeNegocio(Agencia agencia) {
		Retorno retorno = new Retorno(true, null);
		
		if (agencia.getNumero() == null || agencia.getNumero().length() < 2 || agencia.getNumero().length() > 4) {
			retorno.setSucesso(false);
			retorno.addMensagem("Numero da Agencia Deve Ter Entre 2 e 4 Digitos!");
		}else if (!Validacao.validaSeTemSoNumeros(agencia.getNumero())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Numero da Agecia Deve Ter Apenas Numeros!");
		}else if (!verificaSeCampoExiste("numero", agencia.getNumero(),agencia.getBanco(),agencia.getIdAgencia())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Agencia Ja Existe!");
		}
		
		if(agencia.getNome() == null || agencia.getNome().length() < 4 || agencia.getNome().length() > 30) {
			retorno.setSucesso(false);
			retorno.addMensagem("Nome da Agencia Deve Ter Entre 4 e 30 Digitos!");
		}
		if (agencia.getBanco() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhum Banco Selecionado!");
		}
		
		return retorno;
	}
	
	public int verificaSeTemContas(Agencia agencia) {
		Search search = new Search(Conta.class);
		search.addFetch("agencia");
		search.addFilterEqual("agencia", agencia);
		return ServicoConta.count(search);
	}
	
	private boolean verificaSeCampoExiste(String campo, String pesquisa, Banco banco, Long id) {
		Search search = new Search(Agencia.class);
		search.addFilterEqual(campo, pesquisa);
		search.addFilterEqual("banco", banco);
		if (id != null) {
			search.addFilterNotEqual("idAgencia", id);
		}
		int count = count(search);
		
		if (count == 0) {
			return true;
		}
		return false;
	}
	
	public void setAgenciaDAO(AgenciaDAO agenciaDAO) {
		this.agenciaDAO = agenciaDAO;
	}
	
	public void setServicoConta(ServicoConta servicoConta) {
		ServicoConta = servicoConta;
	}
}
