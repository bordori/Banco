package br.com.unika.paginas;

import java.util.List;

import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoUsuario;
import br.com.unika.util.CustomFeedbackPanel;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	private ModalWindow janela;
	private CustomFeedbackPanel feedbackPanel;

	private TextField<String> login;
	private PasswordTextField senha;
	private String loginUsuario, senhaUsuario;
	private Form<String> formLogin;
	private AjaxSubmitLink acaoLogin;

	@SpringBean(name = "servicoUsuario")
	private ServicoUsuario servicoUsuario;

	public HomePage() {
		if (getSession().getAttribute("usuarioLogado") != null) {
			setResponsePage(ClienteConta.class);
		}
		feedbackPanel = new CustomFeedbackPanel("feedBack");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);

		add(formLogin());
		add(initModal());
		add(acaoResgistrar());
	}

	private Form<String> formLogin() {
		formLogin = new Form<>("formLogin");

		formLogin.add(campoLogin());
		formLogin.add(campoSenha());
		formLogin.add(acaoLogin());

		return formLogin;
	}

	private AjaxSubmitLink acaoLogin() {
		acaoLogin = new AjaxSubmitLink("acaoLogin", formLogin) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				logar(getSession(), target);
			}
		};
		return acaoLogin;
	}

	private void logar(Session session, AjaxRequestTarget target) {
		Search search = new Search(Usuario.class);
		search.addFilterEqual("login", getLoginUsuario());
		search.addFilterEqual("senha", getSenhaUsuario());
		List<Usuario> lista = servicoUsuario.search(search);
		if (!lista.isEmpty()) {
			Usuario usuario = lista.get(0);
			if (usuario.getAtivo() == false) {
				feedbackPanel.error("Usuario Desativado!");
				target.add(feedbackPanel);
			} else {
				session.setAttribute("usuarioLogado", usuario);
				setResponsePage(ClienteConta.class);
			}

		} else {
			feedbackPanel.error("Login ou senha inválido");
			target.add(feedbackPanel);
		}

	}

	private PasswordTextField campoSenha() {
		senha = new PasswordTextField("senhaUsuario", new PropertyModel<>(this, "senhaUsuario"));

		return senha;
	}

	private TextField<String> campoLogin() {
		login = new TextField<>("loginUsuario", new PropertyModel<>(this, "loginUsuario"));

		return login;
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

	private AjaxLink<Void> acaoResgistrar() {
		AjaxLink<Void> ajaxSubmitLink = new AjaxLink<Void>("acaoRegistrar") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setMinimalWidth(800);
				janela.setInitialWidth(1200);
				janela.setMinimalHeight(400);
				janela.setInitialHeight(550);
				CadastrarUsuario cadastrarUsuario = new CadastrarUsuario(janela.getContentId()) {

					private static final long serialVersionUID = 1L;

					@Override
					public void acaoSalvarCancelarUsuario(AjaxRequestTarget target, boolean tecla) {
						if (tecla) {
							feedbackPanel.error("O Cliente Foi Adicionado com Sucesso");

						}
						janela.close(target);
						target.add(feedbackPanel);
						super.acaoSalvarCancelarUsuario(target, tecla);
					}
				};
				janela.setContent(cadastrarUsuario);
				janela.show(target);
			}
		};

		return ajaxSubmitLink;
	}

	public String getLoginUsuario() {
		return loginUsuario;
	}

	public void setLoginUsuario(String loginUsuario) {
		this.loginUsuario = loginUsuario;
	}

	public String getSenhaUsuario() {
		return senhaUsuario;
	}

	public void setSenhaUsuario(String senhaUsuario) {
		this.senhaUsuario = senhaUsuario;
	}

}
