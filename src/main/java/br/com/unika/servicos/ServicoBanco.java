package br.com.unika.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.BancoDAO;
import br.com.unika.dao.ContaDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Usuario;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ServicoBanco implements IServico<Banco, Long>, Serializable {

	private static final long serialVersionUID = 1L;

	@SpringBean(name = "bancoDAO")
	private BancoDAO bancoDAO;

	@SpringBean(name = "servicoAgencia")
	private ServicoAgencia servicoAgencia;

	@Override
	public Retorno incluir(Banco banco) {

		Retorno retorno = new Retorno(true, null);
		banco = (Banco) Validacao.retiraEspacoDesnecessarios(banco);
		retorno = validacaoDeNegocio(banco);

		if (!retorno.isSucesso()) {
			return retorno;
		}

		retorno = bancoDAO.salvarDAO(banco);

		return retorno;
	}

	@Override
	public Retorno alterar(Banco banco) {

		Retorno retorno = new Retorno(true, null);
		banco = (Banco) Validacao.retiraEspacoDesnecessarios(banco);
		retorno = validacaoDeNegocio(banco);

		if (!retorno.isSucesso()) {
			return retorno;
		}
		if (banco.getIdBanco() != null) {
			retorno = bancoDAO.alterarDAO(banco);
		} else {
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhum Id Informado");
		}

		return retorno;

	}

	@Override
	public Banco procurar(Banco banco) {
		if (banco != null && banco.getIdBanco() != null) {

			return bancoDAO.procurarDAO(banco.getIdBanco());

		}
		return null;
	}

	@Override
	public List<Banco> listar() {
		return bancoDAO.listarDAO();

	}

	@Override
	public Retorno remover(Banco banco) {

		Retorno retorno = new Retorno(true, null);

		if (banco != null && banco.getIdBanco() != null) {
			if (verificaSeTemAgencias(banco) == 0) {
				retorno = bancoDAO.removerDAO(banco);
			} else {
				retorno.setSucesso(false);
				retorno.addMensagem("O Banco Não Deve Ter Agências Para a Exclusão!");
			}
		} else {
			System.out.println("Nenhuma PK inserida ou com o valor null");
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhuma PK inserida ou com o valor null");
		}
		return retorno;
	}

	public int verificaSeTemAgencias(Banco banco) {
		Search search = new Search(Agencia.class);
		search.addFilterEqual("banco", banco);
		return servicoAgencia.count(search);
	}

	@Override
	public List<Banco> search(Search search) {
		ArrayList<Banco> lista = new ArrayList<>();
		lista = (ArrayList<Banco>) bancoDAO.searchDAO(search);

		return lista;
	}

	@Override
	public int count(Search search) {
		return bancoDAO.countDAO(search);
	}

	private Retorno validacaoDeNegocio(Banco banco) {
		Retorno retorno = new Retorno(true, null);

		if (banco.getNome() == null || banco.getNome().length() < 3 || banco.getNome().length() > 20) {
			retorno.setSucesso(false);
			retorno.addMensagem("Nome do Banco Deve Ter Entre 3 e 20 Digitos");
		} else if (!verificaSeCampoExiste("nome", banco.getNome(), banco.getIdBanco())) {
			retorno.setSucesso(false);
			retorno.addMensagem("O Banco " + banco.getNome() + " Ja Esta Cadastrado!");
		}

		if (banco.getNumero() == null || banco.getNumero().length() < 1 || banco.getNumero().length() > 4) {
			retorno.setSucesso(false);
			retorno.addMensagem("Número do Banco Deve Ter Entre 1 e 4 Digitos");

		} else if (!Validacao.validaSeTemSoNumeros(banco.getNumero())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Numero do Banco Deve Ter Apenas Numeros!");
		} else if (!verificaSeCampoExiste("numero", banco.getNumero(), banco.getIdBanco())) {
			retorno.setSucesso(false);
			retorno.addMensagem("Existe um Banco com o Numero " + banco.getNumero() + " Cadastrado!");
		}

		return retorno;
	}

	private boolean verificaSeCampoExiste(String campo, String pesquisa, Long id) {
		Search search = new Search(Banco.class);
		search.addFilterEqual(campo, pesquisa);
		if (id != null) {
			search.addFilterNotEqual("idBanco", id);
		}
		int count = count(search);

		if (count == 0) {
			return true;
		}
		return false;
	}

	public void setBancoDAO(BancoDAO bancoDAO) {
		this.bancoDAO = bancoDAO;
	}
	
	public void setServicoAgencia(ServicoAgencia servicoAgencia) {
		this.servicoAgencia = servicoAgencia;
	}
}
