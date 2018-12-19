package br.com.unika.paginas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.markup.html.WebPage;

public class NavBar extends WebPage{

	private static final long serialVersionUID = 1L;

	public NavBar() {
		add(acaoRegistrar());
		}
	private AjaxLink<Void> acaoRegistrar() {
		AjaxLink<Void> acaoRegistrar = new AjaxLink<Void>("acaoRegistrar") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				acaoRegistro(target);
				
			}
		};
		return acaoRegistrar;
	}
	public void acaoRegistro(AjaxRequestTarget target) {
		// TODO Auto-generated method stub
		
	}

	
}
