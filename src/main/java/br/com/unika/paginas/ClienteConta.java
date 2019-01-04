package br.com.unika.paginas;

import java.math.BigDecimal;
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

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoConta;
import br.com.unika.util.Confirmacao;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;

public class ClienteConta extends NavBar{

	private static final long serialVersionUID = 1L;

	private ListView<Conta> listaConta;
	private ModalWindow janela;
	private NotificationPanel notificationPanel;
	private WebMarkupContainer containerListView;
	private List<Conta> contasList;
	
	@SpringBean(name = "servicoConta")
	private ServicoConta servicoConta;
	
	public ClienteConta() {
		preencherListView();
//		add(formFiltro());
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
		containerListView.add(acaoNovaAgencia());

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
				
				item.add(new Label("banco", conta.getAgencia().getBanco().getNumeroNomeBanco()).setOutputMarkupId(true));
				item.add(new Label("agencia", conta.getAgencia().getNumeroNomeAgencia()).setOutputMarkupId(true));
				item.add(new Label("conta", conta.getConta()).setOutputMarkupId(true));
				item.add(new Label("Tipoconta", Validacao.tipoConta(conta.getTipoConta())).setOutputMarkupId(true));
				item.add(new Label("saldo", Validacao.FormatarSaldo(conta.getSaldo())).setOutputMarkupId(true));
				item.add(AtivarDesativarConta(conta));

				item.add(acaoDeletar(conta));
			}
		};
		listaConta.setOutputMarkupId(true);
		return listaConta;
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
				janela.setMinimalHeight(400);
				janela.setInitialHeight(550);
				Usuario usuario = (Usuario) getSession().getAttribute("usuarioLogado");
				CadastrarConta cadastrarConta = new CadastrarConta(janela.getContentId(), usuario) {
					@Override
					protected void acaoSubmitCriarConta(AjaxRequestTarget target) {
						super.acaoSubmitCriarConta(target);
						janela.close(target);
						preencherListView();
						notificationPanel.mensagem("Conta Criada com Sucesso!", "sucesso");
						target.add(containerListView);
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
