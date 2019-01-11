package br.com.unika.paginas;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
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

public class ListaBancos extends NavBar {

	private static final long serialVersionUID = 1L;

	private ListView<Banco> listaBancos;
	private ModalWindow janela;
	private NotificationPanel notificationPanel;
	private WebMarkupContainer containerListView;
	private TextField<String> nomeFiltro, numeroFiltro;
	private Form<Banco> formFiltro;
	private List<Banco> bancos;
	private Banco bancoProcurar;

	@SpringBean(name = "servicoBanco")
	private ServicoBanco servicoBanco;

	@SpringBean(name = "servicoConta")
	private ServicoConta servicoConta;

	public ListaBancos() {

		verificarPermissaoBanco();
		preencherListaBancos();
		add(containerListView());
		add(initModal());
		add(formFiltro());

	}

	private void preencherListaBancos() {
		bancos = servicoBanco.listar();
	}

	private WebMarkupContainer containerListView() {
		containerListView = new WebMarkupContainer("containerListView");
		containerListView.setOutputMarkupId(true);
		notificationPanel = new NotificationPanel("feedBack");
		notificationPanel.setOutputMarkupId(true);
		containerListView.add(notificationPanel);
		containerListView.add(polularTabelaBancos());
		

		return containerListView;
	}

	private Form<Banco> formFiltro() {
		bancoProcurar = new Banco();
		formFiltro = new Form<Banco>("formFiltro", new CompoundPropertyModel<>(bancoProcurar));
		formFiltro.setOutputMarkupId(true);
		formFiltro.add(filtroNome());
		formFiltro.add(filtroNumero());
		formFiltro.add(acaoProcurar());
		formFiltro.add(acaoNovoBanco());
		return formFiltro;
	}

	private AjaxSubmitLink acaoProcurar() {
		AjaxSubmitLink acaoProcurar = new AjaxSubmitLink("acaoProcurar", formFiltro) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Search search = new Search(Banco.class);

				if (bancoProcurar.getNome() != null && !bancoProcurar.getNome().equals("")) {
					search.addFilterILike("nome", "%" + bancoProcurar.getNome() + "%");
				}
				if (bancoProcurar.getNumero() != null && !bancoProcurar.getNumero().equals("")) {
					search.addFilterILike("numero", "%" + bancoProcurar.getNumero() + "%");
				}
				bancos = servicoBanco.search(search);
				target.add(containerListView);
				super.onSubmit(target, form);
			}
		};
		return acaoProcurar;
	}

	private TextField<String> filtroNumero() {
		numeroFiltro = new TextField<>("numero");
		numeroFiltro.setOutputMarkupId(true);
		return numeroFiltro;
	}

	private TextField<String> filtroNome() {
		nomeFiltro = new TextField<>("nome");
		return nomeFiltro;
	}

	private ListView<Banco> polularTabelaBancos() {
		LoadableDetachableModel<List<Banco>> detachableModel = new LoadableDetachableModel<List<Banco>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Banco> load() {
				return bancos;
			}
		};
		listaBancos = new ListView<Banco>("listaBancos", detachableModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Banco> item) {
				Banco banco = item.getModelObject();

				Search search = new Search(Conta.class);
				search.addFilterEqual("agencia.banco", banco);

				item.add(new Label("numero", banco.getNumero()).setOutputMarkupId(true));
				item.add(new Label("nome", banco.getNome()).setOutputMarkupId(true));
				int contas = servicoConta.count(search);
				item.add(new Label("contas", contas).setOutputMarkupId(true));
				search.addFilterEqual("ativo", true);
				int ativos = servicoConta.count(search);
				item.add(new Label("contasAtivas", ativos).setOutputMarkupId(true));
				item.add(new Label("contasInativas", contas - ativos).setOutputMarkupId(true));
				item.add(new Label("agencias", servicoBanco.verificaSeTemAgencias(banco)).setOutputMarkupId(true));
				item.add(acaoAlterar(banco));
				item.add(acaoDeletar(banco));
			}
		};
		listaBancos.setOutputMarkupId(true);
		return listaBancos;
	}

	protected AjaxLink<Void> acaoDeletar(Banco banco) {
		AjaxLink<Void> acaoDeletar = new AjaxLink<Void>("acaoDeletar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setInitialWidth(550);
				janela.setInitialHeight(300);
				janela.setResizable(false);
				Confirmacao confirmacao = new Confirmacao(janela.getContentId(), "do Banco") {
					private static final long serialVersionUID = 1L;

					@Override
					public void execultarFechar(AjaxRequestTarget target, boolean tecla, String senha) {
						super.execultarFechar(target, tecla, senha);
						if (tecla == true) {
							Usuario usuarioLogado = (Usuario) getSession().getAttribute("usuarioLogado");
							if (usuarioLogado.getSenha().equals(senha)) {
								Retorno retorno = servicoBanco.remover(banco);
								if (retorno.isSucesso()) {
									notificationPanel.mensagem("Banco Deletado com Sucesso!", "sucesso");
									janela.close(target);
									preencherListaBancos();
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

	protected AjaxLink<Void> acaoAlterar(Banco bancoAlterar) {
		AjaxLink<Void> acaoAlterar = new AjaxLink<Void>("acaoAlterar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setMinimalWidth(500);
				janela.setInitialWidth(600);
				janela.setMinimalHeight(300);
				janela.setInitialHeight(350);
				CadastrarBanco cadastrarBanco = new CadastrarBanco(janela.getContentId(), bancoAlterar) {
					private static final long serialVersionUID = 1L;

					@Override
					public void acaoSalvarCancelarBanco(AjaxRequestTarget target,boolean tecla) {
						if (tecla) {
							notificationPanel.mensagem("Banco foi Alterado Com Sucesso!", "sucesso");
							preencherListaBancos();
							target.add(containerListView);

						}
						janela.close(target);
						super.acaoSalvarCancelarBanco(target,tecla);
					}
				};
				janela.setContent(cadastrarBanco);
				janela.show(target);
			}
		};
		return acaoAlterar;
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

	private AjaxLink<Void> acaoNovoBanco() {
		AjaxLink<Void> acaoNovoBanco = new AjaxLink<Void>("novoBanco") {

			@Override
			public void onClick(AjaxRequestTarget target) {

				janela.setMinimalWidth(500);
				janela.setInitialWidth(600);
				janela.setMinimalHeight(300);
				janela.setInitialHeight(350);
				CadastrarBanco cadastrarBanco = new CadastrarBanco(janela.getContentId()) {
					@Override
					public void acaoSalvarCancelarBanco(AjaxRequestTarget target,boolean tecla) {
						if (tecla) {
							notificationPanel.mensagem("Banco Adicionado Com Sucesso!", "sucesso");
							preencherListaBancos();
							target.add(containerListView);
						}
						janela.close(target);
						
						super.acaoSalvarCancelarBanco(target,tecla);
					}
				};
				janela.setContent(cadastrarBanco);
				janela.show(target);
			}

		};
		return acaoNovoBanco;
	}

}
