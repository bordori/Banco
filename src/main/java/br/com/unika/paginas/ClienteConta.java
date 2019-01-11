package br.com.unika.paginas;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import com.googlecode.genericdao.search.Search;

import br.com.unika.enums.EnumTipoMovimentacao;
import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Contato;
import br.com.unika.modelo.Movimentacao;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoConta;
import br.com.unika.servicos.ServicoMovimentacao;
import br.com.unika.util.AJAXDownload;
import br.com.unika.util.Confirmacao;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.RelatorioJasper;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ClienteConta extends NavBar {

	private static final long serialVersionUID = 1L;

	private ListView<Conta> listaConta;
	private ModalWindow janela;
	private NotificationPanel notificationPanel;
	private WebMarkupContainer containerListView;
	private List<Conta> contasList;

	@SpringBean(name = "servicoConta")
	private ServicoConta servicoConta;

	@SpringBean(name = "servicoMovimentacao")
	private ServicoMovimentacao servicoMovimentacao;

	public ClienteConta() {
		preencherListView();
//		add(formFiltro());
		add(acaoNovaAgencia());

		add(containerListView());
		add(initModal());
	}

	private void preencherListView() {
		Search search = new Search(Conta.class);
		search.addFilterEqual("usuario", (Usuario) getSession().getAttribute("usuarioLogado"));
		contasList = servicoConta.search(search);

	}

	private ModalWindow initModal() {
		janela = new ModalWindow("janela");
		janela.setWindowClosedCallback(new WindowClosedCallback() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target) {
				// TODO Auto-generated method stub

			}
		});
		return janela;
	}

	private WebMarkupContainer containerListView() {
		containerListView = new WebMarkupContainer("containerListView");
		containerListView.setOutputMarkupId(true);
		notificationPanel = new NotificationPanel("feedBack");
		notificationPanel.setOutputMarkupId(true);
		containerListView.add(notificationPanel);
		containerListView.add(polularTabelaContas());

		return containerListView;
	}

	private AjaxLink<Void> acaoDeposito(Conta conta) {
		AjaxLink<Void> acaoDeposito = new AjaxLink<Void>("acaoDeposito") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setInitialWidth(550);
				janela.setInitialHeight(380);
				janela.setResizable(false);
				OperacaoSaqueDeposito acaoMovimentacoes = new OperacaoSaqueDeposito(janela.getContentId(),
						EnumTipoMovimentacao.DEPOSITO, null) {
					private static final long serialVersionUID = 1L;

					@Override
					public void execultarFechar(AjaxRequestTarget target, boolean tecla, Double valor) {
						super.execultarFechar(target, tecla, valor);
						Retorno retorno = new Retorno(true, null);

						if (tecla) {
							retorno = servicoConta.Deposito(conta, valor);

							if (retorno.isSucesso()) {
								notificationPanel.mensagem("Deposito Efetuado com Sucesso!", "sucesso");
								janela.close(target);
								servicoMovimentacao.comprovanteDepositoSaque(conta, EnumTipoMovimentacao.DEPOSITO,
										valor);

							} else {
								notificationPanel.mensagem(retorno.getRetorno(), "erro");
							}

						} else {
							janela.close(target);
						}
						target.add(containerListView);
					}
				};
				janela.setContent(acaoMovimentacoes);
				janela.show(target);
			}
		};
		return acaoDeposito;
	}

	private AjaxLink<Void> acaoSaque(Conta conta) {
		AjaxLink<Void> acaoSaque = new AjaxLink<Void>("acaoSaque") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setInitialWidth(550);
				janela.setInitialHeight(380);
				janela.setResizable(false);
				OperacaoSaqueDeposito acaoMovimentacoes = new OperacaoSaqueDeposito(janela.getContentId(),
						EnumTipoMovimentacao.SAQUE, conta.getSaldo()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void execultarFechar(AjaxRequestTarget target, boolean tecla, Double valor) {
						super.execultarFechar(target, tecla, valor);
						Retorno retorno = new Retorno(true, null);

						if (tecla) {
							retorno = servicoConta.Saque(conta, valor);

							if (retorno.isSucesso()) {
								notificationPanel.mensagem("Saque Efetuado com Sucesso!", "sucesso");
								janela.close(target);
								servicoMovimentacao.comprovanteDepositoSaque(conta, EnumTipoMovimentacao.SAQUE, valor);
							} else {
								notificationPanel.mensagem(retorno.getRetorno(), "erro");
							}

						} else {
							janela.close(target);
						}
						target.add(containerListView);
					}
				};
				janela.setContent(acaoMovimentacoes);
				janela.show(target);
			}
		};
		return acaoSaque;
	}

	private ListView<Conta> polularTabelaContas() {
		LoadableDetachableModel<List<Conta>> detachableModel = new LoadableDetachableModel<List<Conta>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Conta> load() {
				return contasList;
			}
		};
		listaConta = new ListView<Conta>("listaContas", detachableModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Conta> item) {
				Conta conta = item.getModelObject();

				item.add(
						new Label("banco", conta.getAgencia().getBanco().getNumeroNomeBanco()).setOutputMarkupId(true));
				item.add(new Label("agencia", conta.getAgencia().getNumeroNomeAgencia()).setOutputMarkupId(true));
				item.add(new Label("conta", conta.getConta()).setOutputMarkupId(true));
				item.add(new Label("Tipoconta", Validacao.tipoConta(conta.getTipoConta())).setOutputMarkupId(true));
				item.add(new Label("saldo", Validacao.FormatarSaldo(conta.getSaldo())).setOutputMarkupId(true));
				item.add(AtivarDesativarConta(conta));
				item.add(acaoDeposito(conta));
				item.add(acaoSaque(conta));
				item.add(acaoTransferencia(conta));
				item.add(acaoMovimentacao(conta));

				item.add(acaoDeletar(conta));
			}
		};
		listaConta.setOutputMarkupId(true);
		return listaConta;
	}

	protected AjaxLink<Void> acaoTransferencia(Conta conta) {
		AjaxLink<Void> acaoTransferencia = new AjaxLink<Void>("acaoTransferencia") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setMinimalWidth(600);
				janela.setInitialWidth(700);
				janela.setMinimalHeight(500);
				janela.setInitialHeight(520);
				OperacaoTransferencia operacaoTransferencia = new OperacaoTransferencia(janela.getContentId(), conta) {
					@Override
					public void acaoSubmitTrasferencia(AjaxRequestTarget target, Boolean tecla, Conta conta,
							Double valorTransferencia, Contato contato, Double taxa) {
						Retorno retorno = new Retorno(true, null);
						if (tecla) {
							retorno = servicoConta.transferencia(contato, valorTransferencia, conta, taxa);

							if (!retorno.isSucesso()) {
								notificationPanel.mensagem(retorno.getRetorno(), "erro");
							} else {
								notificationPanel.mensagem("Transferencia Concluida com Sucesso!", "sucesso");
								janela.close(target);
								servicoMovimentacao.comprovanteTransferencia(conta, valorTransferencia, contato, taxa);
							}
						} else {
							janela.close(target);
						}
						target.add(containerListView);
						super.acaoSubmitTrasferencia(target, tecla, conta, valorTransferencia, contato, taxa);
					}
				};

				janela.setContent(operacaoTransferencia);
				janela.show(target);
			}
		};
		return acaoTransferencia;
	}

	private AjaxLink<Void> acaoMovimentacao(Conta conta) {
		AjaxLink<Void> acaoMovimentacao = new AjaxLink<Void>("acaoMovimentacao") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setMinimalWidth(500);
				janela.setInitialWidth(660);
				janela.setMinimalHeight(300);
				janela.setInitialHeight(350);
				ListaMovimentacoes listaMovimentacoes = new ListaMovimentacoes(janela.getContentId()) {
					@Override
					public void execultarFechar(AjaxRequestTarget target, boolean tecla, Calendar dataInicial,
							Calendar dataFinal, EnumTipoMovimentacao tipoMovimentacao) {
						super.execultarFechar(target, tecla, dataInicial, dataFinal, tipoMovimentacao);
						if (tecla) {
							Search search = new Search(Movimentacao.class);
							// DateFormat dateFormat = DateFormat.getDateInstance(DateFormat.DATE_FIELD,new
							// Locale("pt", "BR"));
							search.addFilterEqual("conta", conta);
							if (dataInicial != null) {
								search.addFilterGreaterOrEqual("data", dataInicial);
							}
							if (dataFinal != null) {
								search.addFilterLessOrEqual("data", dataFinal);
							}
							if (tipoMovimentacao != null) {
								search.addFilterEqual("tipoMovimentacao", tipoMovimentacao.getValor());
							}
							List<Movimentacao> listaMovimentacoes = servicoMovimentacao.search(search);

							if (!listaMovimentacoes.isEmpty()) {
								gerarPdfMovimentacoes(target, conta, listaMovimentacoes);

							} else {
								notificationPanel.mensagem("Nenhuma Movimentação foi Encontrada", "erro");
							}
						} else {
							janela.close(target);
						}
						target.add(containerListView);
					}
				};
				janela.setContent(listaMovimentacoes);
				janela.show(target);
			}
		};
		return acaoMovimentacao;
	}

	private void gerarPdfMovimentacoes(AjaxRequestTarget target, Conta conta, List<Movimentacao> listaMovimentacoes) {

		HashMap<String, Object> hash = new HashMap<>();
		hash.put("conta", conta);

		final byte[] bytes = RelatorioJasper.gerarRelatorioMovimentacoes(hash, "Movimentacoes", listaMovimentacoes);

		final AJAXDownload download = new AJAXDownload("Movimentacao.pdf") {
			private static final long serialVersionUID = 1L;

			@Override
			protected IResourceStream getResourceStream() {
				AbstractResourceStreamWriter stream = new AbstractResourceStreamWriter() {
					private static final long serialVersionUID = 1L;

					@Override
					public void write(OutputStream output) throws IOException {
						output.write(bytes);
//						output.close();
					}
				};
				return stream;
			}

		};
		add(download);
		download.initiate(target);
	}

	private AjaxLink<Void> AtivarDesativarConta(final Conta conta) {

		AjaxLink<Void> ativo = new AjaxLink<Void>("ativo") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Retorno retorno = servicoConta.ativarDesativarConta(conta);
				if (retorno.isSucesso()) {
					notificationPanel.mensagem("A Conta foi " + Validacao.converterBooleanAtivo(conta.getAtivo()),
							"sucesso");
				} else {
					notificationPanel.mensagem(retorno.getRetorno(), "erro");
				}
				target.add(containerListView);
			}
		};

		if (conta.getAtivo()) {
			ativo.add(new AttributeModifier("class", "botaoAtivoSim"));
			// ativo.add(new AttributeModifier("style", "color:green"));

		} else {
			ativo.add(new AttributeModifier("class", "botaoAtivoNao"));
			// ativo.add(new AttributeModifier("style", "color:red"));
		}
		return ativo;
	}

	private AjaxLink<Void> acaoNovaAgencia() {
		AjaxLink<Void> acaoNovaAgencia = new AjaxLink<Void>("novaConta") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setMinimalWidth(600);
				janela.setInitialWidth(700);
				janela.setMinimalHeight(350);
				janela.setInitialHeight(400);
				Usuario usuario = (Usuario) getSession().getAttribute("usuarioLogado");
				CadastrarConta cadastrarConta = new CadastrarConta(janela.getContentId(), usuario) {
					@Override
					protected void acaoSalvarCancelarConta(AjaxRequestTarget target, boolean tecla) {
						super.acaoSalvarCancelarConta(target, tecla);
						if (tecla) {
							preencherListView();
							notificationPanel.mensagem("Conta Criada com Sucesso!", "sucesso");
							target.add(containerListView);
						}
						janela.close(target);

					}
				};
				janela.setContent(cadastrarConta);
				janela.show(target);
			}

		};
		return acaoNovaAgencia;
	}

	private AjaxLink<Void> acaoDeletar(Conta conta) {
		AjaxLink<Void> acaoDeletar = new AjaxLink<Void>("acaoDeletar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setInitialWidth(550);
				janela.setInitialHeight(300);
				janela.setResizable(false);
				Confirmacao confirmacao = new Confirmacao(janela.getContentId(), "da Conta") {
					private static final long serialVersionUID = 1L;

					@Override
					public void execultarFechar(AjaxRequestTarget target, boolean tecla, String senha) {
						super.execultarFechar(target, tecla, senha);
						if (tecla == true) {
							Usuario usuarioLogado = (Usuario) getSession().getAttribute("usuarioLogado");
							if (usuarioLogado.getSenha().equals(senha)
									&& usuarioLogado.getPermissaoDeAcesso().getAlterarConta() == true) {
								Retorno retorno = servicoConta.remover(conta);
								if (retorno.isSucesso()) {
									notificationPanel.mensagem("Conta Deletada com Sucesso!", "sucesso");
									janela.close(target);
									preencherListView();
									target.add(containerListView);
								} else {
									notificationPanel.mensagem(retorno.getRetorno(), "erro");
									janela.close(target);
									target.add(notificationPanel);
								}
							} else {
								notificationPanel.mensagem("Senha Incorreta!", "erro");
								target.add(notificationPanel);
							}
						} else {
							janela.close(target);
						}
					}
				};
				janela.setContent(confirmacao);
				janela.show(target);

			}
		};
		return acaoDeletar;
	}

}
