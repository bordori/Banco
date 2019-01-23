package br.com.unika.paginas;

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

import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoConta;
import br.com.unika.servicos.ServicoUsuario;
import br.com.unika.util.CustomFeedbackPanel;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ListaClientes extends NavBar {

	private static final long serialVersionUID = 1L;

	private ListView<Usuario> listaUsuario;
	private ModalWindow janela;
	private CustomFeedbackPanel feedbackPanel;
	private WebMarkupContainer containerListView;
	private List<Usuario> usuariosList;

	@SpringBean(name = "servicoUsuario")
	private ServicoUsuario servicoUsuario;

	@SpringBean(name = "servicoConta")
	private ServicoConta servicoConta;

	public ListaClientes() {
		verificarPermissaoConta();
		preencherListView();
		add(containerListView());
		add(initModal());
	}

	private void preencherListView() {
		usuariosList = servicoUsuario.listar();

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
		containerListView.add(polularTabelaUsuarios());

		return containerListView;
	}

	private ListView<Usuario> polularTabelaUsuarios() {
		LoadableDetachableModel<List<Usuario>> detachableModel = new LoadableDetachableModel<List<Usuario>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Usuario> load() {
				return usuariosList;
			}
		};
		listaUsuario = new ListView<Usuario>("listaUsuarios", detachableModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Usuario> item) {
				Usuario usuario = item.getModelObject();

				item.add(new Label("nomeCliente", usuario.getNomeCompleto()).setOutputMarkupId(true));
				item.add(new Label("telefone", usuario.getTelefone()).setOutputMarkupId(true));
				item.add(new Label("cpf", usuario.getCpf()).setOutputMarkupId(true));

				item.add(new Label("contas", servicoConta.numeroDeContasDoCliente(usuario)).setOutputMarkupId(true));

				item.add(new Label("contasAtivas", servicoConta.numeroDeContasAtivasDoCliente(usuario))
						.setOutputMarkupId(true));
				item.add(AtivarDesativarConta(usuario).setOutputMarkupId(true));

				item.add(acaoAlterar(usuario));
			}
		};
		listaUsuario.setOutputMarkupId(true);
		return listaUsuario;
	}

	private AjaxLink<Void> acaoAlterar(Usuario usuarioAlterar) {
		AjaxLink<Void> acaoAlterar = new AjaxLink<Void>("acaoAlterar") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setMinimalWidth(800);
				janela.setInitialWidth(1200);
				janela.setMinimalHeight(400);
				janela.setInitialHeight(550);
				CadastrarUsuario cadastrarUsuario = new CadastrarUsuario(janela.getContentId(), usuarioAlterar) {
					private static final long serialVersionUID = 1L;

					@Override
					public void acaoSalvarCancelarUsuario(AjaxRequestTarget target, boolean tecla) {
						if (tecla) {
							feedbackPanel.success("Cliente alterado com sucesso!");

						}
						janela.close(target);
						target.add(containerListView);
						super.acaoSalvarCancelarUsuario(target, tecla);
					}
				};
				janela.setContent(cadastrarUsuario);
				janela.show(target);
			}
		};

		return acaoAlterar;
	}

	private AjaxLink<Void> AtivarDesativarConta(final Usuario usuario) {

		AjaxLink<Void> ativo = new AjaxLink<Void>("ativo") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				Retorno retorno = servicoUsuario.ativarDesativarConta(usuario);
				if (retorno.isSucesso()) {
					feedbackPanel.success("Cliente foi " + Validacao.converterBooleanAtivo(usuario.getAtivo()));
				} else {
					feedbackPanel = retorno.getMensagens(feedbackPanel);
				}
				target.add(containerListView);
			}
		};

		if (usuario.getAtivo()) {
			ativo.add(new AttributeModifier("class", "botaoAtivoSim"));

		} else {
			ativo.add(new AttributeModifier("class", "botaoAtivoNao"));
		}
		return ativo;
	}

}
