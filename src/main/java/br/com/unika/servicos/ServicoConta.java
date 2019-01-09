package br.com.unika.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.ContaDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Contato;
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
			if (conta.getSaldo() == 0) {
				retorno = contaDAO.removerDAO(conta);
			} else {
				retorno.setSucesso(false);
				retorno.addMensagem("A Conta não Deve Ter Saldo!");
			}

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

	public Retorno ativarDesativarConta(Conta conta) {
		Retorno retorno = new Retorno(true, null);

		if (conta.getSaldo() == 0) {
			if (conta.getAtivo()) {
				conta.setAtivo(false);
			} else {
				conta.setAtivo(true);
			}
			retorno = contaDAO.alterarDAO(conta);
		} else {
			retorno.setSucesso(false);
			retorno.addMensagem("O Conta não Deve ter Saldo Para Desativa-la!");
		}
		return retorno;
	}

	public Retorno Deposito(Conta conta, Double valorDeposito) {
		Retorno retorno = new Retorno(true, null);
		if (valorDeposito < 2) {
			retorno.setSucesso(false);
			retorno.addMensagem("Valor Mínimo para Deposito é R$ 2,00");
		} else if (valorDeposito > 1000000) {
			retorno.setSucesso(false);
			retorno.addMensagem("Valor Máximo para Deposito é R$ 1.000.000,00");
		}

		if (retorno.isSucesso()) {
			conta.setSaldo(conta.getSaldo() + valorDeposito);
			retorno = contaDAO.alterarDAO(conta);
		}

		return retorno;
	}

	public Retorno Saque(Conta conta, Double valorSaque) {
		Retorno retorno = new Retorno(true, null);

		if (conta.getSaldo() < valorSaque) {
			retorno.setSucesso(false);
			retorno.addMensagem("Saldo Insuficiente Para Realizar o Saque!");
		} else if (valorSaque < 2) {
			retorno.setSucesso(false);
			retorno.addMensagem("Saque Mínimo é de R$ 2,00!");
		} else if (valorSaque > 100000) {
			retorno.setSucesso(false);
			retorno.addMensagem("Saque Máximo é de R$ 100.000,00!");
		}

		if (retorno.isSucesso()) {
			conta.setSaldo(conta.getSaldo() - valorSaque);
			contaDAO.alterarDAO(conta);
		}

		return retorno;
	}

	public Retorno transferencia(Contato contato, Double valorTransferencia, Conta conta,Double taxa) {
		Retorno retorno = new Retorno(true, null);

		if (conta.getSaldo() < (valorTransferencia+taxa)) {
			retorno.setSucesso(false);
			retorno.addMensagem("Saldo Insuficiente Para Realizar a Transferencia!");
		} else if (valorTransferencia < 1) {
			retorno.setSucesso(false);
			retorno.addMensagem("Valor Mínimo é de R$ 1,00!");
		} else if (valorTransferencia > 1000000) {
			retorno.setSucesso(false);
			retorno.addMensagem("Valor Máximo é de R$ 1.000.000,00!");
		}
		if (retorno.isSucesso()) {
			Search search = new Search(Conta.class);
			search.addFilterEqual("conta", contato.getConta());
			search.addFilterEqual("agencia.numero", contato.getNumeroAgencia());
			search.addFilterEqual("agencia.banco.numero", contato.getNumeroBanco());
			search.addFilterEqual("usuario.cpf", contato.getCpf());
			List<Conta> contaLista = search(search); 
			if ( contaLista.size() == 1) {
				Conta contaFavorecido = contaLista.get(0);
				if (contaFavorecido.getAtivo() == true) {
					this.transferir(contaFavorecido,conta,valorTransferencia,taxa);
				}else {
					retorno.setSucesso(false);
					retorno.addMensagem("Essa Conta Está Desativada!");
				}
			}else {
				retorno.setSucesso(false);
				retorno.addMensagem("A Conta não Existe!");
			}
		}

		return retorno;
	}

	private Retorno transferir(Conta contaFavorecido, Conta conta,Double valorTrasferencia,Double taxa) {
		Retorno retorno = new Retorno(true, null);
		
		contaFavorecido.setSaldo(contaFavorecido.getSaldo()+valorTrasferencia);
		conta.setSaldo(conta.getSaldo()-valorTrasferencia);
		if (!conta.getAgencia().getBanco().getNumero().equals(contaFavorecido.getAgencia().getBanco().getNumero())){
			conta.setSaldo(conta.getSaldo()-taxa);
		}
		retorno = contaDAO.transferir(contaFavorecido,conta);
		
		return retorno;
	}

	private Retorno validacaoDeNegocio(Conta conta) {
		Retorno retorno = new Retorno(true, null);

		if (conta.getTipoConta() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Selecione um Tipo de Conta!");
		}

		if (conta.getAtivo() == null) {
			conta.setAtivo(true);
		}

		if (conta.getConta() == null || conta.getConta().length() != 6) {
			retorno.setSucesso(false);
			retorno.addMensagem("Selecione o Banco e Agência para Gerar uma Conta!");
		}

		if (conta.getSaldo() == null) {
			conta.setSaldo(0.0);
		} else if (conta.getSaldo() != 0 && conta.getAtivo() == false) {
			retorno.setSucesso(false);
			retorno.addMensagem("Para Desativar uma Conta ela não Deve ter Saldo!");
		}

		if (conta.getAgencia() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Selecione Uma Agencia!");
		}

		if (conta.getUsuario() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Selecione um Usuario!");
		} else if (conta.getUsuario().getAtivo() == false) {
			retorno.setSucesso(false);
			retorno.addMensagem("Essa Cliente Esta Desativado!");
		}

		return retorno;
	}

	public String gerarConta(Banco banco, Agencia agencia) {
		String conta = geradorDeConta();
		while (!verificaSeContaExisteNoBanco(conta, banco, agencia)) {
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

	private boolean verificaSeContaExisteNoBanco(String conta, Banco banco, Agencia agencia) {

		Search search = new Search(Conta.class);
		search.addFilterEqual("conta", conta);
		search.addFilterEqual("agencia", agencia);
		search.addFilterEqual("agencia.banco", banco);

		int count = this.count(search);

		if (count == 0) {
			return true;
		}

		return false;
	}

	public void setContaDAO(ContaDAO contaDAO) {
		this.contaDAO = contaDAO;
	}

}
