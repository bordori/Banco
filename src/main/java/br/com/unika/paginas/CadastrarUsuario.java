package br.com.unika.paginas;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.FormComponent;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;

import br.com.unika.modelo.Usuario;

public class CadastrarUsuario extends Panel {

	private Form<Usuario> formCriarUsuario;
	private Usuario usuario;
	private TextField<String> login,nome,sobrenome,cpf,cep,endereco,numero,complemento,bairro;
	private PasswordTextField senha;
	private EmailTextField email;
	private TextField<String> telefone;
	private DateTextField dataNascimento;
	private Select<Boolean> sexo;
	

	public CadastrarUsuario(String id) {
		super(id);

		add(formCriarUsuario());

	}
	
	private Form<Usuario> formCriarUsuario() {
		usuario = new Usuario();
		formCriarUsuario = new Form<Usuario>("formCriarUsuario", new CompoundPropertyModel<Usuario>(usuario));
		formCriarUsuario.setOutputMarkupId(true);
		formCriarUsuario.add(new AttributeModifier("autocomplete", "off"));

		formCriarUsuario.add(campoNome());
		formCriarUsuario.add(campoSobrenome());
		formCriarUsuario.add(campoTelefone());
		formCriarUsuario.add(campoCpf());
		formCriarUsuario.add(campoEmail());
		formCriarUsuario.add(campoDataNascimento());
		formCriarUsuario.add(campoCep());
		formCriarUsuario.add(campoEndereco());
		formCriarUsuario.add(campoNumero());
		formCriarUsuario.add(campoComplemento());
		formCriarUsuario.add(campoSexo());
		formCriarUsuario.add(campoBairro());		
		formCriarUsuario.add(campoLogin());
		formCriarUsuario.add(campoSenha());
		
		formCriarUsuario.add(acaoSubmitCriarUsuario());

		return formCriarUsuario;

	}

	private TextField<String> campoBairro() {
		bairro = new TextField<>("bairro");
		bairro.setRequired(true);
		return bairro;
	}

	private Select<Boolean> campoSexo() {
		sexo = new Select<Boolean>("sexo");
		sexo.add(new SelectOption<Boolean>("escolhaSexo", new Model<Boolean>(null)));
		sexo.add(new SelectOption<Boolean>("masculino", new Model<Boolean>(true)));
		sexo.add(new SelectOption<Boolean>("feminino", new Model<Boolean>(false)));
		return sexo;
	}

	private TextField<String> campoComplemento() {
		complemento = new TextField<>("complemento");
		
		return complemento;
	}

	private TextField<String> campoNumero() {
		numero = new TextField<>("numero");
		numero.setRequired(true);
		return numero;
	}

	private TextField<String> campoEndereco() {
		endereco = new TextField<>("endereco");
		endereco.setRequired(true);
		return endereco;
	}

	private TextField<String> campoCep() {
		cep = new TextField<>("cep");
		cep.add((new AttributeModifier("onfocus", "$(this).mask('99999-999');")));
		cep.setRequired(true);
		return cep;
	}

	private DateTextField campoDataNascimento() {
		dataNascimento = new DateTextField("dataNascimento", "dd/MM/yyyy");
		DatePicker datePicker = new DatePicker() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean alignWithIcon() {

				return true;
			}

			@Override
			protected boolean enableMonthYearSelection() {
				// TODO Auto-generated method stub
				return false;
			}
		};
		dataNascimento.add(new AttributeModifier("onfocus", "$(this).mask('99/99/9999');"));
		datePicker.setAutoHide(true);
		datePicker.setShowOnFieldClick(true);
		dataNascimento.add(datePicker);
		dataNascimento.setOutputMarkupId(true);
		dataNascimento.setRequired(true);
		return dataNascimento;
	}

	private TextField<String> campoCpf() {
		cpf = new TextField<>("cpf");
		cpf.add((new AttributeModifier("onfocus", "$(this).mask('999.999.999-99');")));
		cpf.setRequired(true);
		return cpf;
	}

	private TextField<String> campoSobrenome() {
		sobrenome = new TextField<>("sobrenome");
		sobrenome.setRequired(true);
		return sobrenome;
	}

	private TextField<String> campoNome() {
		nome = new TextField<>("nome");
		nome.setRequired(true);
		return nome;
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

	

	private AjaxSubmitLink acaoSubmitCriarUsuario() {
		AjaxSubmitLink ajaxSubmitLink = new AjaxSubmitLink("acaoNovoUsuario") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {

				super.onSubmit(target, form);
			}
		};
		return ajaxSubmitLink;
	}

}
