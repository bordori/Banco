package br.com.unika.paginas;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.apache.wicket.Component;
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
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoUsuario;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.Retorno;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	private ModalWindow janela;
	private NotificationPanel notificationPanel;

	private TextField<String> login;
	private PasswordTextField senha;
	private String loginUsuario, senhaUsuario;
	private Form<String> formLogin;
	private AjaxSubmitLink acaoLogin;

	@SpringBean(name = "servicoUsuario")
	private ServicoUsuario servicoUsuario;

	public HomePage() {
		if (getSession().getAttribute("usuarioLogado") != null) {
			setResponsePage(Menu.class);
		}
		notificationPanel = new NotificationPanel("feedBack");
		notificationPanel.setOutputMarkupId(true);
		add(notificationPanel);
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
				notificationPanel.mensagem("Usuario Desativado!", "erro");
				target.add(notificationPanel);
			} else {
				session.setAttribute("usuarioLogado", usuario);
				setResponsePage(Menu.class);
			}

		} else {
			notificationPanel.mensagem("Login ou Senha Esta Incorreto!", "erro");
			target.add(notificationPanel);
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
				// TODO Auto-generated method stub

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
					@Override
					public void acaoSalvarCancelarUsuario(AjaxRequestTarget target,boolean tecla) {
						if (tecla) {
							notificationPanel.mensagem("O Usuario Foi Adicionado com sucesso", "sucesso");
							
						}
						janela.close(target);
						target.add(notificationPanel);
						super.acaoSalvarCancelarUsuario(target,tecla);
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
