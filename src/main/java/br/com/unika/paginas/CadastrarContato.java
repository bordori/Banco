package br.com.unika.paginas;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.enums.EnumTipoConta;
import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Contato;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoAgencia;
import br.com.unika.servicos.ServicoBanco;
import br.com.unika.servicos.ServicoContato;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;
import wicket.contrib.input.events.EventType;
import wicket.contrib.input.events.InputBehavior;
import wicket.contrib.input.events.key.KeyType;

public class CadastrarContato extends Panel {
	private static final long serialVersionUID = 1L;

	protected Contato contato;
	protected Form<Contato> formCriarContato;
	private NotificationPanel notificationPanel;
	protected DropDownChoice<Agencia> dropAgencia;
	private DropDownChoice<EnumTipoConta> dropTipoConta;
	protected Banco bancoSelecionado;
	private Agencia agenciaSelecionado;
	private EnumTipoConta tipoDeContaEnum;
	protected WebMarkupContainer container;

	@SpringBean(name = "servicoBanco")
	private ServicoBanco servicoBanco;

	@SpringBean(name = "servicoAgencia")
	private ServicoAgencia servicoAgencia;

	@SpringBean(name = "servicoContato")
	private ServicoContato servicoContato;

	public CadastrarContato(String id) {
		super(id);
		contato = new Contato();
		contato.setUsuario((Usuario) getSession().getAttribute("usuarioLogado"));
		montarTela();
	}

	public CadastrarContato(String id, Contato contatoAlterar) {
		super(id);
		contato = contatoAlterar;
		preencherDropDown();
		montarTela();
		dropAgencia.setEnabled(true);
	}

	protected void preencherDropDown() {
		bancoSelecionado = contato.getAgencia().getBanco();
		tipoDeContaEnum = Validacao.tipoContaEnum(contato.getTipoConta());
	}

	private void montarTela() {
		notificationPanel = new NotificationPanel("feedBackPanel");
		notificationPanel.setOutputMarkupId(true);
		add(montarContainer());

	}

	private WebMarkupContainer montarContainer() {
		container = new WebMarkupContainer("webContainer");
		container.setOutputMarkupId(true);

		container.add(notificationPanel);
		container.add(montarForm());
		return container;
	}

	private Form<Contato> montarForm() {
		formCriarContato = new Form<Contato>("formCriarContato", new CompoundPropertyModel<Contato>(contato));
		formCriarContato.setOutputMarkupId(true);

		formCriarContato.add(titulo());

		formCriarContato.add(campoApelido());
		formCriarContato.add(campoCpf());
		formCriarContato.add(campoConta());
		formCriarContato.add(campoDropBanco());
		formCriarContato.add(campoDropAgencia());
		formCriarContato.add(campoDropTipoConta());
		formCriarContato.add(botaoSalvar());
		formCriarContato.add(cancelar());

		return formCriarContato;
	}

	public Label titulo() {
		Label titulo;
		if (contato.getIdContato() == null) {
			titulo = new Label("titulo", "Incluindo Novo Contato");
		} else {
			titulo = new Label("titulo", "Editando Contato: " + contato.getApelido());
		}
		return titulo;
	}

	private TextField<String> campoConta() {
		TextField<String> conta = new TextField<>("conta");
		conta.setOutputMarkupId(true);
		conta.add((new AttributeModifier("onfocus", "$(this).mask('999999');")));
		conta.setRequired(true);
		return conta;
	}

	private TextField<String> campoCpf() {
		TextField<String> cpf = new TextField<>("cpf");
		cpf.add((new AttributeModifier("onfocus", "$(this).mask('999.999.999-99');")));
		cpf.setRequired(true);
		return cpf;
	}

	private TextField<String> campoApelido() {
		TextField<String> apelido = new TextField<String>("apelido");
		apelido.setRequired(true);

		return apelido;
	}

	private AjaxSubmitLink botaoSalvar() {
		AjaxSubmitLink acaoSubmit = new AjaxSubmitLink("sim", formCriarContato) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Retorno retorno = new Retorno(true, null);

				if (contato.getIdContato() == null) {
					retorno = servicoContato.incluir(contato);
				} else {
					retorno = servicoContato.alterar(contato);
				}

				if (retorno.isSucesso()) {
					acaoSalvarCancelarContato(target, true);
				} else {
					notificationPanel.mensagem(retorno.getRetorno(), "erro");
					target.add(notificationPanel);
				}
				super.onSubmit(target, form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				notificationPanel.montarFeedBack();
				target.add(notificationPanel);
				super.onError(target, form);
			}
		};
		return acaoSubmit;
	}

	private AjaxLink<Void> cancelar() {
		AjaxLink<Void> acaoSubmit = new AjaxLink<Void>("nao") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				acaoSalvarCancelarContato(target, false);

			}
		};
		acaoSubmit.add(new InputBehavior(new KeyType[] { KeyType.Escape }, EventType.click));
		return acaoSubmit;
	}

	protected void acaoSalvarCancelarContato(AjaxRequestTarget target, Boolean tecla) {
		// TODO Auto-generated method stub

	}

	private DropDownChoice<EnumTipoConta> campoDropTipoConta() {
		ChoiceRenderer<EnumTipoConta> tipoConta = new ChoiceRenderer<EnumTipoConta>("descricao", "valor") {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(EnumTipoConta object) {
				return object.getValor() + "-" + object.getDescricao();
			}
		};
		IModel<List<EnumTipoConta>> model = new LoadableDetachableModel<List<EnumTipoConta>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<EnumTipoConta> load() {

				return EnumTipoConta.listaDeTipoDeConta();
			}

		};

		dropTipoConta = new DropDownChoice<EnumTipoConta>("tipoConta", new PropertyModel<>(this, "tipoDeContaEnum"),
				model, tipoConta) {

			private static final long serialVersionUID = 1L;

			@Override
			protected String getNullValidDisplayValue() {
				return "Escolha";
			}
		};
		dropTipoConta.add(new AjaxFormComponentUpdatingBehavior("onchange") {

			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				if (tipoDeContaEnum != null) {
					contato.setTipoConta(tipoDeContaEnum.getValor());
				}

			}
		});
		dropTipoConta.setRequired(true);
		dropTipoConta.setOutputMarkupId(true);

		return dropTipoConta;
	}

	private DropDownChoice<Banco> campoDropBanco() {
		ChoiceRenderer<Banco> banco = new ChoiceRenderer<Banco>("nome", "idBanco") {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Banco object) {
				return object.getNumero() + "-" + object.getNome();
			}
		};
		IModel<List<Banco>> model = new LoadableDetachableModel<List<Banco>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Banco> load() {

				return servicoBanco.listar();
			}

		};

		DropDownChoice<Banco> dropBanco = new DropDownChoice<Banco>("banco",
				new PropertyModel<Banco>(this, "bancoSelecionado"), model, banco) {

			private static final long serialVersionUID = 1L;

			@Override
			protected String getNullValidDisplayValue() {
				return "Escolha";
			}
		};
		dropBanco.setOutputMarkupId(true);
		dropBanco.setNullValid(true);
		dropBanco.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				ChoiceRenderer<Agencia> agencia = new ChoiceRenderer<Agencia>("nome", "idAgencia") {
					private static final long serialVersionUID = 1L;

					@Override
					public Object getDisplayValue(Agencia object) {
						return object.getNumero() + "-" + object.getNome();
					}
				};
				IModel<List<Agencia>> model = new LoadableDetachableModel<List<Agencia>>() {
					private static final long serialVersionUID = 1L;

					@Override
					protected List<Agencia> load() {

						return servicoAgencia.getAgenciasDoBanco(bancoSelecionado);
					}

				};
				contato.setAgencia(null);
				dropAgencia.setChoiceRenderer(agencia);
				dropAgencia.setChoices(model);

				if (bancoSelecionado == null) {
					dropAgencia.setEnabled(false);
				} else {
					dropAgencia.setEnabled(true);
				}
				target.add(dropAgencia);
			}

		});
		return dropBanco;
	}

	private DropDownChoice<Agencia> campoDropAgencia() {
		ChoiceRenderer<Agencia> agencia = new ChoiceRenderer<Agencia>("nome", "idAgencia") {
			private static final long serialVersionUID = 1L;

			@Override
			public Object getDisplayValue(Agencia object) {
				return object.getNumero() + "-" + object.getNome();
			}
		};
		IModel<List<Agencia>> model = new LoadableDetachableModel<List<Agencia>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Agencia> load() {
				Search search = new Search(Agencia.class);
				if (bancoSelecionado != null) {
					search.addFilterEqual("banco", bancoSelecionado);
				}
				return servicoAgencia.search(search);
			}
		};

		dropAgencia = new DropDownChoice<Agencia>("agencia", model, agencia) {
			
			private static final long serialVersionUID = 1L;

			@Override
			protected String getNullValidDisplayValue() {
				return "Escolha";
			}
		};

		dropAgencia.setOutputMarkupId(true);
		dropAgencia.setNullValid(true);
		dropAgencia.setRequired(true);
		dropAgencia.setEnabled(false);

		return dropAgencia;
	}

	public Banco getBancoSelecionado() {
		return bancoSelecionado;
	}

	public void setBancoSelecionado(Banco bancoSelecionado) {
		this.bancoSelecionado = bancoSelecionado;
	}

	public Agencia getAgenciaSelecionado() {
		return agenciaSelecionado;
	}

	public void setAgenciaSelecionado(Agencia agenciaSelecionado) {
		this.agenciaSelecionado = agenciaSelecionado;
	}

}
