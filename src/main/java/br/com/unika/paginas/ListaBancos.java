package br.com.unika.paginas;

import java.util.List;

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

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoAgencia;
import br.com.unika.servicos.ServicoBanco;
import br.com.unika.servicos.ServicoConta;
import br.com.unika.util.Confirmacao;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.Retorno;

public class ListaBancos extends NavBar {

	private static final long serialVersionUID = 1L;

	private ListView<Banco> listaBancos;
	private ModalWindow janela;
	private NotificationPanel notificationPanel;
	private WebMarkupContainer containerListView;

	@SpringBean(name = "servicoBanco")
	private ServicoBanco servicoBanco;

	@SpringBean(name = "servicoConta")
	private ServicoConta servicoConta;

	public ListaBancos() {

		verificarPermissao();
		add(containerListView());
		add(initModal());

	}

	private WebMarkupContainer containerListView() {
		containerListView = new WebMarkupContainer("containerListView");
		containerListView.setOutputMarkupId(true);
		notificationPanel = new NotificationPanel("feedBack");
		notificationPanel.setOutputMarkupId(true);
		containerListView.add(notificationPanel);
		containerListView.add(polularTabelaBancos());
		containerListView.add(acaoNovoBanco());

		return containerListView;
	}

	private ListView<Banco> polularTabelaBancos() {
		LoadableDetachableModel<List<Banco>> detachableModel = new LoadableDetachableModel<List<Banco>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Banco> load() {
				return servicoBanco.listar();
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
				search.addFilterEqual("ativo", false);
				int ativos = servicoConta.count(search);
				item.add(new Label("contasAtivas", ativos).setOutputMarkupId(true));
				item.add(new Label("contasInativas", contas - ativos).setOutputMarkupId(true));
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
				Confirmacao confirmacao = new Confirmacao(janela.getContentId()) {
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
					public void acaoSubmitCriarBanco(AjaxRequestTarget target) {
						janela.close(target);
						notificationPanel.mensagem("Banco foi Alterado Com Sucesso!", "sucesso");
						target.add(containerListView);
						super.acaoSubmitCriarBanco(target);
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
					public void acaoSubmitCriarBanco(AjaxRequestTarget target) {
						janela.close(target);
						notificationPanel.mensagem("Banco Adicionado Com Sucesso!", "sucesso");
						target.add(containerListView);
						super.acaoSubmitCriarBanco(target);
					}
				};
				janela.setContent(cadastrarBanco);
				janela.show(target);
			}

		};
		return acaoNovoBanco;
	}

}
