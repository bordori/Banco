package br.com.unika.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.MovimentacaoDAO;
import br.com.unika.dao.UsuarioDAO;
import br.com.unika.enums.EnumTipoMovimentacao;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Contato;
import br.com.unika.modelo.Movimentacao;
import br.com.unika.modelo.Usuario;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ServicoMovimentacao implements IServico<Movimentacao, Long>, Serializable {

	private static final long serialVersionUID = 1L;

	@SpringBean(name = "movimentacaoDAO")
	private MovimentacaoDAO movimentacaoDAO;

	@Override
	public Retorno incluir(Movimentacao movimentacao) {

		Retorno retorno = new Retorno(true, null);
		movimentacao = (Movimentacao) Validacao.retiraEspacoDesnecessarios(movimentacao);

		if (!retorno.isSucesso()) {
			return retorno;
		}

		retorno = movimentacaoDAO.salvarDAO(movimentacao);

		return retorno;
	}

	@Override
	public Retorno alterar(Movimentacao movimentacao) {

		Retorno retorno = new Retorno(true, null);
		movimentacao = (Movimentacao) Validacao.retiraEspacoDesnecessarios(movimentacao);

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

	@Override
	public int count(Search search) {
		return movimentacaoDAO.countDAO(search);
	}

	public void comprovanteDepositoSaque(Conta conta, EnumTipoMovimentacao tipoMovimentacao, Double valor) {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setTipoMovimentacao(tipoMovimentacao.getValor());
		movimentacao.setConta(conta);
		movimentacao.setValor(Validacao.FormatarSaldo(valor));
		Calendar data = Calendar.getInstance();
		movimentacao.setData(data);

		incluir(movimentacao);

	}

	public void comprovanteTransferencia(Conta conta, Double valorTransferencia, Contato contato,Double taxa) {
		Movimentacao movimentacao = new Movimentacao();
		movimentacao.setTipoMovimentacao(EnumTipoMovimentacao.TRANSFERENCIA.getValor());
		if (taxa != 0 ) {
			movimentacao.setValor(Validacao.FormatarSaldo(valorTransferencia)+"+taxa:"+Validacao.FormatarSaldo(taxa));
		}else {
			movimentacao.setValor(Validacao.FormatarSaldo(valorTransferencia));
		}
		movimentacao.setConta(conta);
	
		Calendar data = Calendar.getInstance();
		movimentacao.setNomeFavorecido(contato.getApelido());
		movimentacao.setCpfFavoracido(contato.getCpf());
		movimentacao.setNumeroBancoFavorecido(contato.getAgencia().getBanco().getNumero());
		movimentacao.setNomeBancoFavorecido(contato.getAgencia().getBanco().getNome());
		movimentacao.setNumeroAgenciaFavorecido(contato.getAgencia().getNumero());
		movimentacao.setNomeAgenciaFavorecido(contato.getAgencia().getNome());
		movimentacao.setContaFavorecido(contato.getConta());
		movimentacao.setData(data);

		incluir(movimentacao);
	}

	public void setMovimentacaoDAO(MovimentacaoDAO movimentacaoDAO) {
		this.movimentacaoDAO = movimentacaoDAO;
	}

}
