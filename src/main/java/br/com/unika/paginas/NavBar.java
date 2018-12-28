package br.com.unika.paginas;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.basic.Label;

import br.com.unika.modelo.Usuario;

public class NavBar extends WebPage {
	
	private Label usuarioLogado;
	private WebMarkupContainer alterarContas,alterarBancos;
	private Usuario usuario;
	
	public NavBar() {
		if(getSession().getAttribute("usuarioLogado") == null) {
			redirectToInterceptPage(new HomePage());
		}
		
		add(usuarioLogado());
		add(sairSession());
		add(alterarContas());
		add(alterarBancos());
	}

	private AjaxLink<Void> opcaoBancos() {
		AjaxLink<Void> opcaoBancos = new AjaxLink<Void>("opcaoBancos") {
			
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(ListaBancos.class);
			}
		};
		return opcaoBancos;
	}

	private WebMarkupContainer alterarBancos() {
		alterarBancos = new WebMarkupContainer("alterarBancos");
		alterarBancos.setOutputMarkupId(true);
		
		alterarBancos.add(opcaoBancos());
		
		if(usuario.getPermissaoDeAcesso().getAlterarBanco() == false) {
			alterarBancos.setVisible(false);
		}
		return alterarBancos;
	}

	private WebMarkupContainer alterarContas() {
		alterarContas = new WebMarkupContainer("alterarContas");
		alterarContas.setOutputMarkupId(true);
		if(usuario.getPermissaoDeAcesso().getAlterarConta() == false) {
			alterarContas.setVisible(false);
		}
		return alterarContas;
	}

	private AjaxLink<Void> sairSession() {
		AjaxLink<Void> sair = new AjaxLink<Void>("sair") {
			
			@Override
			public void onClick(AjaxRequestTarget target) {
				getSession().invalidate();
				setResponsePage(HomePage.class);				
			}
		};
		return sair;
	}

	private Label usuarioLogado() {
		usuario = (Usuario) getSession().getAttribute("usuarioLogado");
		usuarioLogado = new Label("usuarioLogado","Olá, "+usuario.getNome()+".");
		return usuarioLogado;
	}
	
	public void verificarPermissao() {
		Usuario usuario = (Usuario) getSession().getAttribute("usuarioLogado");
		if (usuario.getPermissaoDeAcesso().getAlterarBanco() != true) {
			setResponsePage(Menu.class);
		}
	}

}
