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
import br.com.unika.util.Validacao;

public class ServicoContato implements IServico<Contato, Long>, Serializable {

	private static final long serialVersionUID = 1L;

	@SpringBean(name = "contatoDAO")
	private ContatoDAO contatoDAO;

	@Override
	public Retorno incluir(Contato contato) {

		Retorno retorno = new Retorno(true, null);
		contato = (Contato) Validacao.retiraEspacoDesnecessarios(contato);
		retorno = validacaoDeNegocio(contato);

		if (!retorno.isSucesso()) {
			return retorno;
		}

		retorno = contatoDAO.salvarDAO(contato);

		return retorno;
	}

	@Override
	public Retorno alterar(Contato contato) {

		Retorno retorno = new Retorno(true, null);
		contato = (Contato) Validacao.retiraEspacoDesnecessarios(contato);
		retorno = validacaoDeNegocio(contato);

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

	@Override
	public int count(Search search) {
		return contatoDAO.countDAO(search);
	}

	private Retorno validacaoDeNegocio(Contato contato) {
		Retorno retorno = new Retorno(true, null);

		if (contato.getApelido() == null || contato.getApelido().length() < 3 || contato.getApelido().length() > 15) {
			retorno.setSucesso(false);
			retorno.addMensagem("Apelido Deve Ter Entre 3 e 15 Digitos!");
		}

		if (contato.getCpf() == null || contato.getCpf().length() != 14) {
			retorno.setSucesso(false);
			retorno.addMensagem("CPF Incorreto!");
		} else if (!Validacao.validarCPF(contato.getCpf())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Digito Verificador do CPF Invalido!");
		}

		if (contato.getConta() == null || contato.getConta().length() != 6) {
			retorno.setSucesso(false);
			retorno.addMensagem("Conta Deve Ter 6 Digitos!");
		}

		if (contato.getAgencia() == null || contato.getAgencia().length() != 4 ) {
			retorno.setSucesso(false);
			retorno.addMensagem("Agencia Deve Ser Preenchido");
		}
		
		if (contato.getNomeBanco() == null || contato.getNumeroBanco() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Banco Deve Ser Preenchido!");
		}
			return retorno;
	}

	public void setContatoDAO(ContatoDAO contatoDAO) {
		this.contatoDAO = contatoDAO;
	}
}
