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

import br.com.unika.modelo.Conta;
import br.com.unika.servicos.ServicoConta;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.Validacao;

public class ListaContas extends NavBar{

	private static final long serialVersionUID = 1L;
	
	private ListView<Conta> listaConta;
	private ModalWindow janela;
	private NotificationPanel notificationPanel;
	private WebMarkupContainer containerListView;
	private List<Conta> contasList;
	
	@SpringBean(name="servicoConta")
	private ServicoConta servicoConta;
	
	public ListaContas() {
		preencherListView();
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
		//containerListView.add(acaoNovaAgencia());
		
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
				Conta conta= item.getModelObject();
								
				item.add(new Label("nomeUsuario", conta.getUsuario().getNome()).setOutputMarkupId(true));
				item.add(new Label("conta", conta.getConta()).setOutputMarkupId(true));
				item.add(new Label("agencia", conta.getAgencia().getNumero()).setOutputMarkupId(true));
				item.add(new Label("nomeBanco", conta.getAgencia().getBanco().getNome()).setOutputMarkupId(true));
				item.add(new Label("tipoConta", Validacao.tipoConta(conta.getTipoConta())).setOutputMarkupId(true));
				item.add(new Label("ativo", Validacao.converterBoolean(conta.getAtivo())).setOutputMarkupId(true));
				
//				item.add(acaoAlterar(conta));
//				item.add(acaoDeletar(conta));
			}
		};
		listaConta.setOutputMarkupId(true);
		return listaConta;
	}
	
	private AjaxLink<Void> acaoNovaAgencia() {
		AjaxLink<Void> acaoNovaAgencia = new AjaxLink<Void>("novaAgencia") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				janela.setMinimalWidth(500);
				janela.setInitialWidth(600);
				janela.setMinimalHeight(350);
				janela.setInitialHeight(400);
				
				janela.setContent(cadastrarAgencia);
				janela.show(target);
			}

		};
		return acaoNovaAgencia;
	}
}
