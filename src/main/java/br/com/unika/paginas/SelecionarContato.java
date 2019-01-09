package br.com.unika.paginas;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import br.com.unika.modelo.Contato;
import br.com.unika.servicos.ServicoContato;

public class SelecionarContato extends Panel {

	private static final long serialVersionUID = 1L;

	private List<Contato> listaContatos;
	private WebMarkupContainer container;
	private ListView<Contato> listView;

	@SpringBean(name = "servicoContato")
	private ServicoContato servicoContato;

	public SelecionarContato(String id) {
		super(id);
		preencherLista();
		add(montarContainer());
	}
	
	private void preencherLista() {
		listaContatos = new ArrayList<>();
		listaContatos = servicoContato.listar();
	}

	private WebMarkupContainer montarContainer() {
		container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		container.add(popularListView());

		return container;
	}

	private ListView<Contato> popularListView() {
		LoadableDetachableModel<List<Contato>> loadableDetachableModel = new LoadableDetachableModel<List<Contato>>() {

			@Override
			protected List<Contato> load() {
				// TODO Auto-generated method stub
				return listaContatos;
			}
		};

		listView = new ListView<Contato>("listaViewContatos", loadableDetachableModel) {

			@Override
			protected void populateItem(ListItem<Contato> item) {
				Contato contato = item.getModelObject();

				item.add(new Label("apelido", contato.getApelido()).setOutputMarkupId(true));
				item.add(new Label("banco", contato.getNumeroBanco() + "-" + contato.getNomeBanco())
						.setOutputMarkupId(true));
				item.add(new Label("conta", contato.getConta()).setOutputMarkupId(true));

				item.add(acaoSelecionar(contato));
			}
		};
		return listView;
	}

	private AjaxLink<Void> acaoSelecionar(Contato contato) {
		AjaxLink<Void> acaoSelecionar = new AjaxLink<Void>("acaoSelecionar") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				contatoSelecionado(target,contato);
			}
		};
		return acaoSelecionar;
	}

	public void contatoSelecionado(AjaxRequestTarget target, Contato contato) {
		// TODO Auto-generated method stub
		
	}

}
