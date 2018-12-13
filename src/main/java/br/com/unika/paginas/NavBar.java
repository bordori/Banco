package br.com.unika.paginas;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.model.CompoundPropertyModel;

import br.com.unika.modelo.Usuario;

public class NavBar extends WebPage{

	Form<Usuario> formCriarUsuario;
	Usuario usuario;
	TextField<String> login;
	PasswordTextField senha;
	EmailTextField email;
	TextField<String> telefone;
	
	
	public NavBar() {
		usuario = new Usuario();
		add(formCriarUsuario());
		}
	

	private TextField<String> campoLogin() {
		login = new TextField<>("login");
		login.setRequired(true);
		return login;		
	}
	
	private PasswordTextField campoSenha() {
		senha = new PasswordTextField("senha");
		senha.setRequired(true);
		return senha;		
	}

	private TextField<String> campoTelefone() {
		telefone = new TextField<>("telefone");
		telefone.add((new AttributeModifier("onfocus", "$(this).mask('(99)99999-9999');")));
		telefone.setRequired(true);
		return telefone;		
	}

	private EmailTextField campoEmail() {
		email = new EmailTextField("email");
		email.setRequired(true);
		return email;		
	}


	private Form<Usuario> formCriarUsuario() {
		formCriarUsuario = new Form<Usuario>("formCriarUsuario", new CompoundPropertyModel<Usuario>(usuario));
		formCriarUsuario.setOutputMarkupId(true);
		formCriarUsuario.add(new AttributeModifier("autocomplete", "off"));
		
		formCriarUsuario.add(campoLogin());
		formCriarUsuario.add(campoSenha());
		formCriarUsuario.add(campoTelefone());
		formCriarUsuario.add(campoEmail());
		
		return formCriarUsuario;
		
	}
}
