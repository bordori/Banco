package br.com.unika.paginas;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.Contato;
import br.com.unika.servicos.ServicoContato;
import wicket.contrib.input.events.EventType;
import wicket.contrib.input.events.InputBehavior;
import wicket.contrib.input.events.key.KeyType;

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
		add(new InputBehavior(new KeyType[] { KeyType.Escape }, EventType.click));
	}

	private void preencherLista() {
		listaContatos = new ArrayList<>();
		Search search = new Search(Contato.class);
		search.addFilterEqual("usuario", getSession().getAttribute("usuarioLogado"));
		listaContatos = servicoContato.search(search);
	}

	private WebMarkupContainer montarContainer() {
		container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		container.add(popularListView());

		return container;
	}

	private ListView<Contato> popularListView() {
		LoadableDetachableModel<List<Contato>> loadableDetachableModel = new LoadableDetachableModel<List<Contato>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Contato> load() {
				return listaContatos;
			}
		};

		listView = new ListView<Contato>("listaViewContatos", loadableDetachableModel) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Contato> item) {
				Contato contato = item.getModelObject();

				item.add(new Label("apelido", contato.getApelido()).setOutputMarkupId(true));
				item.add(new Label("banco", contato.getAgencia().getBanco().getNumeroNomeBanco())
						.setOutputMarkupId(true));
				item.add(new Label("conta", contato.getConta()).setOutputMarkupId(true));

				item.add(acaoSelecionar(contato));
			}
		};
		return listView;
	}

	private AjaxLink<Void> acaoSelecionar(Contato contato) {
		AjaxLink<Void> acaoSelecionar = new AjaxLink<Void>("acaoSelecionar") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				contatoSelecionado(target, contato);
			}
		};
		return acaoSelecionar;
	}

	public void contatoSelecionado(AjaxRequestTarget target, Contato contato) {

	}

}
