package br.com.unika.util;

import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.LoadableDetachableModel;

import com.sun.org.apache.regexp.internal.recompile;

public class Paginacao {

	private int itensPorPagina;
	private int quantidadeDeItens;

	public Paginacao(int itensPorPagina, int quantidadeDeitens) {
		this.itensPorPagina = itensPorPagina;
		this.quantidadeDeItens = quantidadeDeitens;
	}


	private ListView<Integer> montarPaginas() {
		float quantidadeDePaginas = (float) quantidadeDeItens / itensPorPagina;
		int aux = (int) quantidadeDePaginas;
		if (quantidadeDePaginas > aux) {
			aux = aux+1;
		}
		
		LoadableDetachableModel<List<Integer>> detachableModel = new LoadableDetachableModel<List<Integer>>() {
			
			@Override
			protected List<Integer> load() {
				float quantidadeDePaginas = (float) quantidadeDeItens / itensPorPagina;
				int aux = (int) quantidadeDePaginas;
				if (quantidadeDePaginas > aux) {
					aux = aux+1;
				}
				List<Integer> list = new ArrayList<>();
				for (int i = 1; i <+ aux; i++) {
					list.add(i);
				}
				return list;
			}
		};
	

		ListView<Integer> listView = new ListView<Integer>("",detachableModel) {

			@Override
			protected void populateItem(ListItem<Integer> item) {
				Integer count = item.getModelObject();
				
				item.add(new AjaxLink<Void>("indice") {
					
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						// TODO Auto-generated method stub
						
					}
				});
			}
		};

		return null;
	}

}
