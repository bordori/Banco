package br.com.unika.paginas;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import br.com.unika.modelo.Agencia;
import br.com.unika.servicos.ServicoAgencia;
import br.com.unika.util.Retorno;

public class ErrosImportacaoDeAgenciasPanel extends Panel {

	private static final long serialVersionUID = 1L;

	private List<Agencia> listaDeAgencias;

	@SpringBean(name = "servicoAgencia")
	private ServicoAgencia servicoAgencia;

	public ErrosImportacaoDeAgenciasPanel(String id, List<Agencia> listaAgenciasErro) {
		super(id);
		validarLista(listaAgenciasErro);
		add(montarContainer());
		
	}

	private void validarLista(List<Agencia> listaAgenciasErro) {
		listaDeAgencias = new ArrayList<>();
		for (Agencia agencia : listaAgenciasErro) {
			Retorno retorno = servicoAgencia.validacaoDeNegocio(agencia);
			String validacaoNumero = servicoAgencia.validaSeTemNumeroIgual(listaAgenciasErro, agencia);

			if (!validacaoNumero.equals("") || !retorno.isSucesso()) {
				listaDeAgencias.add(agencia);
			}
		}

	}

	private WebMarkupContainer montarContainer() {
		WebMarkupContainer container = new WebMarkupContainer("container");
		container.setOutputMarkupId(true);
		container.add(popularListView());

		return container;
	}

	private ListView<Agencia> popularListView() {
		LoadableDetachableModel<List<Agencia>> loadableDetachableModel = new LoadableDetachableModel<List<Agencia>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Agencia> load() {

				return listaDeAgencias;
			}
		};

		ListView<Agencia> listView = new ListView<Agencia>("listViewAgencia", loadableDetachableModel) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Agencia> item) {
				Agencia agencia = item.getModelObject();
				Retorno retorno = servicoAgencia.validacaoDeNegocio(agencia);
				String validacaoNumero = servicoAgencia.validaSeTemNumeroIgual(listaDeAgencias, agencia);

				String erros = "";
				item.add(new Label("linha", agencia.getIdAgencia()).setOutputMarkupId(true));
				item.add(new Label("numero", agencia.getNumero()).setOutputMarkupId(true));
				item.add(new Label("nome", agencia.getNome()).setOutputMarkupId(true));

				if (!retorno.isSucesso()) {
					for (String string : retorno.getRetorno()) {
						erros = erros + ", " + string;
					}
					if (validacaoNumero.length() < 3) {
						erros = erros.substring(2, erros.length());
					}
				}

				item.add(new Label("erro", validacaoNumero + erros).setOutputMarkupId(true));
			}

		};
		return listView;
	}

}
