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
	private WebMarkupContainer alterarContas, alterarBancos;
	private Usuario usuario;

	public NavBar() {
		if (getSession().getAttribute("usuarioLogado") == null) {
			redirectToInterceptPage(new HomePage());
		}

		add(usuarioLogado());
		add(sairSession());
		add(alterarContas());
		add(alterarBancos());
		add(contas());

	}

	private AjaxLink<Void> contas() {
		AjaxLink<Void> contas = new AjaxLink<Void>("contas") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(ClienteConta.class);
			}
		};
		return contas;
	}

	private AjaxLink<Void> opcaoGerenciarBancos() {
		AjaxLink<Void> opcaoBancos = new AjaxLink<Void>("opcaoBancos") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(ListaBancos.class);
			}
		};
		return opcaoBancos;
	}

	private AjaxLink<Void> opcaoGerenciarAgencia() {
		AjaxLink<Void> opcaoAgencia = new AjaxLink<Void>("opcaoAgencia") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(ListaAgencia.class);

			}
		};
		return opcaoAgencia;
	}

	private WebMarkupContainer alterarBancos() {
		alterarBancos = new WebMarkupContainer("alterarBancos");
		alterarBancos.setOutputMarkupId(true);

		alterarBancos.add(opcaoGerenciarBancos());
		alterarBancos.add(opcaoGerenciarAgencia());

		if (usuario.getPermissaoDeAcesso().getAlterarBanco() == false) {
			alterarBancos.setVisible(false);
		}
		return alterarBancos;
	}

	private WebMarkupContainer alterarContas() {
		alterarContas = new WebMarkupContainer("alterarContas");
		alterarContas.setOutputMarkupId(true);

		alterarContas.add(opcaoGerenciarConta());
		alterarContas.add(opcaoGerenciarClientes());

		if (usuario.getPermissaoDeAcesso().getAlterarConta() == false) {
			alterarContas.setVisible(false);
		}
		return alterarContas;
	}

	private AjaxLink<Void> opcaoGerenciarClientes() {
		AjaxLink<Void> opcaoClientes = new AjaxLink<Void>("opcaoClientes") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(ListaClientes.class);

			}
		};
		return opcaoClientes;
	}

	private AjaxLink<Void> opcaoGerenciarConta() {
		AjaxLink<Void> opcaoConta = new AjaxLink<Void>("opcaoConta") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				setResponsePage(ListaContas.class);

			}
		};
		return opcaoConta;
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
		usuarioLogado = new Label("usuarioLogado", "Olá, " + usuario.getNome() + ".");
		return usuarioLogado;
	}

	public void verificarPermissaoBanco() {
		Usuario usuario = (Usuario) getSession().getAttribute("usuarioLogado");
		if (usuario.getPermissaoDeAcesso().getAlterarBanco() != true) {
			setResponsePage(Menu.class);
		}
	}

	public void verificarPermissaoConta() {
		Usuario usuario = (Usuario) getSession().getAttribute("usuarioLogado");
		if (usuario.getPermissaoDeAcesso().getAlterarConta() != true) {
			setResponsePage(Menu.class);
		}
	}

}
