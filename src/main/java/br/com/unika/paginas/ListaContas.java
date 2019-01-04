package br.com.unika.paginas;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoBanco;
import br.com.unika.servicos.ServicoConta;
import br.com.unika.util.Confirmacao;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ListaContas extends NavBar {

	private static final long serialVersionUID = 1L;

	private ListView<Conta> listaConta;
	private ModalWindow janela;
	private NotificationPanel notificationPanel;
	private WebMarkupContainer containerListView;
	private List<Conta> contasList;
	private Conta contaProcurar;
	private Form<Conta> formFiltro;

	@SpringBean(name = "servicoConta")
	private ServicoConta servicoConta;

	@SpringBean(name = "servicoBanco")
	private ServicoBanco servicoBanco;

	public ListaContas() {
		verificarPermissaoConta();
		preencherListView();
		add(formFiltro());
		add(containerListView());
		add(initModal());
	}

	private void preencherListView() {
		contasList = servicoConta.listar();

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

				item.add(new Label("nomeUsuario", conta.getUsuario().getNome()).setOutputMarkupId(true));
				item.add(new Label("conta", conta.getConta()).setOutputMarkupId(true));
				item.add(new Label("agencia", conta.getAgencia().getNome()).setOutputMarkupId(true));
				item.add(new Label("nomeBanco", conta.getAgencia().getBanco().getNome()).setOutputMarkupId(true));
				item.add(new Label("tipoConta", Validacao.tipoConta(conta.getTipoConta())).setOutputMarkupId(true));
				item.add(AtivarDesativarConta(conta).setOutputMarkupId(true));

				item.add(acaoDeletar(conta));
			}
		};
		listaConta.setOutputMarkupId(true);
		return listaConta;
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

	private Form<Conta> formFiltro() {
		contaProcurar = new Conta();
		formFiltro = new Form<Conta>("formFiltro", new CompoundPropertyModel<>(contaProcurar));
		formFiltro.setOutputMarkupId(true);
		formFiltro.add(filtroNomeCliente());
		formFiltro.add(filtroNumeroConta());
		formFiltro.add(filtroNomeAgencia());
		formFiltro.add(campoBancoFiltro());
		formFiltro.add(acaoProcurar());
		return formFiltro;
	}

	private TextField<String> filtroNomeAgencia() {
		TextField<String> nomeAgencia = new TextField<>("agencia.nome");
		return nomeAgencia;
	}

	private TextField<String> filtroNumeroConta() {
		TextField<String> numeroConta = new TextField<>("conta");

		return numeroConta;
	}

	private TextField<String> filtroNomeCliente() {
		TextField<String> nomecliente = new TextField<>("usuario.nome");

		return nomecliente;
	}

	private AjaxSubmitLink acaoProcurar() {
		AjaxSubmitLink acaoProcurar = new AjaxSubmitLink("acaoProcurar", formFiltro) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Search search = new Search(Conta.class);
				System.out.println(contaProcurar.toString());
				if (contaProcurar.getConta() != null && !contaProcurar.getConta().equals("")) {
					search.addFilterEqual("conta", contaProcurar.getConta());
				} else {
					if (contaProcurar.getUsuario() != null && contaProcurar.getUsuario().getNome() != null) {
						search.addFilterILike("usuario.nome", "%" + contaProcurar.getUsuario().getNome() + "%");
					}
					if (contaProcurar.getAgencia() != null && contaProcurar.getAgencia().getBanco() != null) {
						search.addFilterEqual("agencia.banco", contaProcurar.getAgencia().getBanco());
					}
					if (contaProcurar.getAgencia() != null && contaProcurar.getAgencia().getNome() != null) {
						search.addFilterILike("agencia.nome", "%" + contaProcurar.getAgencia().getNome() + "%");
					}
				}
				contasList = servicoConta.search(search);
				target.add(containerListView);
				super.onSubmit(target, form);

			}
		};
		return acaoProcurar;
	}

	private DropDownChoice<Banco> campoBancoFiltro() {
		ChoiceRenderer<Banco> banco = new ChoiceRenderer<Banco>("nome", "idBanco");
		IModel<List<Banco>> model = new LoadableDetachableModel<List<Banco>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Banco> load() {

				return servicoBanco.listar();
			}

		};

		DropDownChoice<Banco> DropBanco = new DropDownChoice<>("agencia.banco", model, banco);
		DropBanco.setOutputMarkupId(false);
		return DropBanco;
	}
}
