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

import br.com.unika.modelo.Contato;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoContato;
import br.com.unika.util.Confirmacao;
import br.com.unika.util.CustomFeedbackPanel;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ListaContatos extends NavBar {
	private static final long serialVersionUID = 1L;

	private ListView<Contato> listaContatos;
	private ModalWindow janela;
	private CustomFeedbackPanel feedbackPanel;
	private WebMarkupContainer containerListView;
	private List<Contato> contatosLista;

	@SpringBean(name = "servicoContato")
	private ServicoContato servicoContato;

	public ListaContatos() {
		preencherListView();
		add(acaoNovoContato());

		add(containerListView());
		add(initModal());
	}

	private void preencherListView() {
		Search search = new Search(Contato.class);
		search.addFilterEqual("usuario", (Usuario) getSession().getAttribute("usuarioLogado"));
		contatosLista = servicoContato.search(search);
	}

	private ModalWindow initModal() {
		janela = new ModalWindow("janela");
		janela.setWindowClosedCallback(new WindowClosedCallback() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target) {

			}
		});
		return janela;
	}

	private WebMarkupContainer containerListView() {
		containerListView = new WebMarkupContainer("containerListView");
		containerListView.setOutputMarkupId(true);
		feedbackPanel = new CustomFeedbackPanel("feedBack");
		feedbackPanel.setOutputMarkupId(true);
		containerListView.add(feedbackPanel);
		containerListView.add(polularTabelaContatos());

		return containerListView;
	}

	private ListView<Contato> polularTabelaContatos() {
		LoadableDetachableModel<List<Contato>> detachableModel = new LoadableDetachableModel<List<Contato>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Contato> load() {
				return contatosLista;
			}
		};
		listaContatos = new ListView<Contato>("listaContatos", detachableModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Contato> item) {
				Contato contatos = item.getModelObject();

				item.add(new Label("apelido", contatos.getApelido()).setOutputMarkupId(true));
				item.add(new Label("cpf", contatos.getCpf()).setOutputMarkupId(true));
				item.add(new Label("banco", contatos.getAgencia().getBanco().getNumeroNomeBanco())
						.setOutputMarkupId(true));
				item.add(new Label("agencia", contatos.getAgencia().getNumeroNomeAgencia()).setOutputMarkupId(true));
				item.add(new Label("conta", contatos.getConta()).setOutputMarkupId(true));
				item.add(new Label("tipoConta", Validacao.tipoConta(contatos.getTipoConta())).setOutputMarkupId(true));

				item.add(acaoAlterar(contatos));
				item.add(acaoDeletar(contatos));
			}
		};
		listaContatos.setOutputMarkupId(true);
		return listaContatos;
	}

	protected AjaxLink<Void> acaoAlterar(Contato contatos) {
		AjaxLink<Void> acaoAlterar = new AjaxLink<Void>("alterarContato") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				janela.setMinimalWidth(600);
				janela.setInitialWidth(700);
				janela.setMinimalHeight(350);
				janela.setInitialHeight(400);
				CadastrarContato cadastrarContato = new CadastrarContato(janela.getContentId(), contatos) {
					
					private static final long serialVersionUID = 1L;

					@Override
					protected void acaoSalvarCancelarContato(AjaxRequestTarget target, Boolean tecla) {
						if (tecla) {
							janela.close(target);
							preencherListView();
							feedbackPanel.success("Contato alterado com sucesso!");
						} else {
							janela.close(target);
						}

						target.add(containerListView);
						super.acaoSalvarCancelarContato(target, tecla);
					}
				};

				janela.setContent(cadastrarContato);
				janela.show(target);
			}

		};
		return acaoAlterar;
	}

	protected AjaxLink<Void> acaoDeletar(Contato contatos) {
		AjaxLink<Void> acaoDeletar = new AjaxLink<Void>("deletarContato") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setInitialWidth(550);
				janela.setInitialHeight(300);
				janela.setResizable(false);
				Confirmacao confirmacao = new Confirmacao(janela.getContentId(), "do Contato") {
					private static final long serialVersionUID = 1L;

					@Override
					public void execultarFechar(AjaxRequestTarget target, boolean tecla, String senha) {
						super.execultarFechar(target, tecla, senha);
						if (tecla == true) {
							Usuario usuarioLogado = (Usuario) getSession().getAttribute("usuarioLogado");
							if (usuarioLogado.getSenha().equals(senha)) {
								Retorno retorno = servicoContato.remover(contatos);
								if (retorno.isSucesso()) {
									feedbackPanel.success("Contato deletado com sucesso!");
									janela.close(target);
									preencherListView();
									target.add(containerListView);
								} else {
									feedbackPanel = retorno.getMensagens(feedbackPanel);
									janela.close(target);
									target.add(feedbackPanel);
								}
							} else {
								feedbackPanel.error("Senha incorreta!");
								target.add(feedbackPanel);
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

	private AjaxLink<Void> acaoNovoContato() {
		AjaxLink<Void> acaoNovaAgencia = new AjaxLink<Void>("novoContato") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				janela.setMinimalWidth(600);
				janela.setInitialWidth(700);
				janela.setMinimalHeight(350);
				janela.setInitialHeight(400);
				CadastrarContato cadastrarContato = new CadastrarContato(janela.getContentId()) {
					
					private static final long serialVersionUID = 1L;

					@Override
					protected void acaoSalvarCancelarContato(AjaxRequestTarget target, Boolean tecla) {
						if (tecla) {
							janela.close(target);
							preencherListView();
							feedbackPanel.success("Contato adicionado com sucesso!");

						} else {
							janela.close(target);

						}

						target.add(containerListView);
						super.acaoSalvarCancelarContato(target, tecla);
					}
				};

				janela.setContent(cadastrarContato);
				janela.show(target);
			}

		};
		return acaoNovaAgencia;
	}
}
