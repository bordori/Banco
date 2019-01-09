package br.com.unika.paginas;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.Session;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.markup.html.form.select.Select;
import org.apache.wicket.extensions.markup.html.form.select.SelectOption;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.EmailTextField;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import br.com.unika.modelo.PermissaoDeAcesso;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoPermissaoDeAcesso;
import br.com.unika.servicos.ServicoUsuario;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.Retorno;
import br.com.unika.util.ViaCepWs;

public class CadastrarUsuario extends Panel {

	private static final long serialVersionUID = 1L;

	private Form<Usuario> formCriarUsuario;
	private Usuario usuario;
	private TextField<String> login, nome, sobrenome, cpf, cep, endereco, numero, complemento, bairro, cidade, estado;
	private PasswordTextField senha, confirmarSenha;
	private EmailTextField email;
	private TextField<String> telefone;
	private DateTextField dataNascimento;
	private Select<Boolean> sexo;
	private DropDownChoice<PermissaoDeAcesso> permissaoDeAcesso;
	private WebMarkupContainer containerCep;
	private String confirmacaoSenha;
	private String alterarSenha;

	private NotificationPanel notificationPanel;

	@SpringBean(name = "servicoPermissaoDeAcesso")
	private ServicoPermissaoDeAcesso servicoPermissaoDeAcesso;

	@SpringBean(name = "servicoUsuario")
	private ServicoUsuario servicoUsuario;

	public CadastrarUsuario(String id) {
		super(id);
		usuario = new Usuario();
		montarTela();

	}

	public CadastrarUsuario(String id, Usuario usuarioAlterar) {
		super(id);
		this.usuario = usuarioAlterar;
		montarTela();
		cpf.setEnabled(false);
		login.setEnabled(false);
		senha.setRequired(false);
		confirmarSenha.setRequired(false);
		alterarSenha = usuario.getSenha();

		if (!alterarPermissaoDeAcesso()) {
			permissaoDeAcesso.setEnabled(false);
		}

	}

	private Boolean alterarPermissaoDeAcesso() {
		Usuario usuarioLogado = (Usuario) getSession().getAttribute("usuarioLogado");
		if (usuarioLogado.getPermissaoDeAcesso().getAlterarPermissoes()) {
			return true;
		}
		return false;
	}

	private void montarTela() {
		notificationPanel = new NotificationPanel("feedBackPanel");
		notificationPanel.setOutputMarkupId(true);
		add(notificationPanel);
		add(formCriarUsuario());
	}

	private Form<Usuario> formCriarUsuario() {

		formCriarUsuario = new Form<Usuario>("formCriarUsuario", new CompoundPropertyModel<Usuario>(usuario));
		formCriarUsuario.setOutputMarkupId(true);

		formCriarUsuario.add(new AttributeModifier("autocomplete", "off"));

		formCriarUsuario.add(campoNome());
		formCriarUsuario.add(campoSobrenome());
		formCriarUsuario.add(campoTelefone());
		formCriarUsuario.add(campoCpf());
		formCriarUsuario.add(campoEmail());
		formCriarUsuario.add(campoDataNascimento());
		formCriarUsuario.add(campoSexo());
		formCriarUsuario.add(campoLogin());
		formCriarUsuario.add(campoSenha());
		formCriarUsuario.add(campoConfirmarSenha());
		formCriarUsuario.add(campoPermissoesDeAcesso());
		formCriarUsuario.add(criarContainer());

		formCriarUsuario.add(acaoSubmit());

		if (usuario.getDataNascimento() != null) {
			Date date = usuario.getDataNascimento().getTime();
			dataNascimento.setModel(new Model<Date>(date));
			dataNascimento.setModelObject(date);
		}

		return formCriarUsuario;

	}

	private WebMarkupContainer criarContainer() {
		containerCep = new WebMarkupContainer("containerCep");
		containerCep.setOutputMarkupId(true);
		containerCep.add(acaoLocalizarCep());
		containerCep.add(campoCep());
		containerCep.add(campoEndereco());
		containerCep.add(campoComplemento());
		containerCep.add(campoNumero());
		containerCep.add(campoBairro());
		containerCep.add(campoCidade());
		containerCep.add(campoEstado());
		return containerCep;
	}

	private DropDownChoice<PermissaoDeAcesso> campoPermissoesDeAcesso() {
		ChoiceRenderer<PermissaoDeAcesso> permissao = new ChoiceRenderer<PermissaoDeAcesso>("descricao", "idPermissao");
		IModel<List<PermissaoDeAcesso>> model = new LoadableDetachableModel<List<PermissaoDeAcesso>>() {

			@Override
			protected List<PermissaoDeAcesso> load() {

				return servicoPermissaoDeAcesso.listar();
			}

		};

		permissaoDeAcesso = new DropDownChoice<>("permissaoDeAcesso", model, permissao);

		return permissaoDeAcesso;
	}

	private AjaxLink<Void> acaoLocalizarCep() {
		AjaxLink<Void> ajaxLink = new AjaxLink<Void>("acaoLocalizarCep") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				String cepConsulta = cep.getModelObject();
				if (cepConsulta != null && !cepConsulta.equals("") && cepConsulta.length() == 9) {
					Map<String, String> mapa = new HashMap<>();
					mapa = ViaCepWs.buscarCep(cepConsulta);
					if (mapa != null && !mapa.isEmpty()) {
						usuario.setComplemento(mapa.get("complemento"));
						usuario.setEndereco(mapa.get("logradouro"));
						usuario.setBairro(mapa.get("bairro"));
						usuario.setCidade(mapa.get("localidade"));
						usuario.setEstado(mapa.get("uf"));

						target.add(containerCep);
					} else {
						notificationPanel.mensagem("Não foi encontrado esse cep!", "erro");
						target.add(notificationPanel);
					}

				}
			}
		};
		return ajaxLink;
	}

	private TextField<String> campoEstado() {
		estado = new TextField<>("estado");
		estado.setRequired(true);
		estado.add(new AttributeModifier("readonly", "readonly"));
		return estado;
	}

	private TextField<String> campoCidade() {
		cidade = new TextField<>("cidade");
		cidade.setRequired(true);
		cidade.add(new AttributeModifier("readonly", "readonly"));
		return cidade;
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
		numero.add(new AttributeModifier("onfocus", "$(this).mask('999999');"));
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
		cep.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {

			}
		});
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
				return true;
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

	private PasswordTextField campoConfirmarSenha() {
		confirmarSenha = new PasswordTextField("confirmarSenha", new PropertyModel<String>(this, "confirmacaoSenha"));
		confirmarSenha.setRequired(true);
		return confirmarSenha;
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

	private AjaxSubmitLink acaoSubmit() {
		AjaxSubmitLink ajaxSubmitLink = new AjaxSubmitLink("acaoNovoUsuario", formCriarUsuario) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				super.onSubmit(target, form);
				if (alterarSenha != null && usuario.getSenha() == null) {
					usuario.setSenha(alterarSenha);
					setConfirmacaoSenha(alterarSenha);
				}
				
				if (!usuario.getSenha().equals(confirmacaoSenha)) {
					notificationPanel.mensagem("Senha e Confirmação da Senha Devem Ser Iguais!", "erro");
					target.add(notificationPanel);

				}else if (usuario.getIdUsuario() == null) {
					usuario.setAtivo(true);
					Retorno retorno = servicoUsuario.incluir(usuario);
					if (retorno.isSucesso()) {
						acaoSubmitCriarUsuario(target);
					} else {
						notificationPanel.mensagem(retorno.getRetorno(), "erro");
						target.add(notificationPanel);
					}
				} else {
					Retorno retorno = servicoUsuario.alterar(usuario);
					if (retorno.isSucesso()) {
						acaoSubmitCriarUsuario(target);
					} else {
						notificationPanel.mensagem(retorno.getRetorno(), "erro");
						target.add(notificationPanel);
					}
				}

			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				// TODO Auto-generated method stub
				super.onError(target, form);

				// Retorno retorno = servicoUsuario.validacaoDeNegocio(usuario);
				notificationPanel.montarFeedBack();
				target.add(notificationPanel);
			}

		};
		return ajaxSubmitLink;
	}

	public void acaoSubmitCriarUsuario(AjaxRequestTarget target) {

	}

	private String getConfirmacaoSenha() {
		return confirmacaoSenha;
	}

	private void setConfirmacaoSenha(String confirmacaoSenha) {
		this.confirmacaoSenha = confirmacaoSenha;
	}

}
