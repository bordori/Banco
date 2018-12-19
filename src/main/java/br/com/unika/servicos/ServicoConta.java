package br.com.unika.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.ContaDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ServicoConta implements IServico<Conta, Long>, Serializable {
	private static final long serialVersionUID = 1L;

	@SpringBean(name = "contaDAO")
	private ContaDAO contaDAO;

	@Override
	public Retorno incluir(Conta conta) {

		Retorno retorno = new Retorno(true, null);
		conta = (Conta) Validacao.retiraEspacoDesnecessarios(conta);
		retorno = validacaoDeNegocio(conta);

		if (!retorno.isSucesso()) {
			return retorno;
		}

		retorno = contaDAO.salvarDAO(conta);

		return retorno;
	}

	@Override
	public Retorno alterar(Conta conta) {

		Retorno retorno = new Retorno(true, null);
		conta = (Conta) Validacao.retiraEspacoDesnecessarios(conta);
		retorno = validacaoDeNegocio(conta);
		
		
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

	@Override
	public int count(Search search) {
		return contaDAO.countDAO(search);
	}

	private Retorno validacaoDeNegocio(Conta conta) {
		Retorno retorno = new Retorno(true, null);
			
		if (conta.getTipoConta() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Selecione um Tipo de Conta!");
		}
		
		if(conta.getAtivo() == null) {
			conta.setAtivo(true);
		}
		
		if(conta.getSaldo() == null) {
			conta.setSaldo(0.0);
		}
		
		if (conta.getBanco() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Selecione um Banco!");
		}
		
		if (conta.getUsuario() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Selecione um Usuario!");
		}
		
		return retorno;
	}

	public String gerarConta(Long idBanco) {
		String conta = geradorDeConta();

		while (!verificaSeContaExisteNoBanco(conta, idBanco)) {
			conta = geradorDeConta();
		}

		return conta;

	}

	private String geradorDeConta() {
		String conta = "";
		Random random = new Random();
		for (int i = 0; i < 6; i++) {
			conta = conta + "" + random.nextInt(10);
		}

		return conta;
	}

	private boolean verificaSeContaExisteNoBanco(String conta, Long idBanco) {
		Banco banco = new Banco();
		banco.setIdBanco(idBanco);

		Search search = new Search(Conta.class);
		search.addFilterEqual("conta", conta);
		search.addFilterEqual("banco", banco);

		ArrayList<Conta> lista = (ArrayList<Conta>) this.search(search);

		if (lista.isEmpty()) {
			return true;
		}

		return false;
	}

	public void setContaDAO(ContaDAO contaDAO) {
		this.contaDAO = contaDAO;
	}

}
