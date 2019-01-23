package br.com.unika.paginas;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
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

import br.com.unika.enums.EnumTipoConta;
import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoAgencia;
import br.com.unika.servicos.ServicoBanco;
import br.com.unika.servicos.ServicoConta;
import br.com.unika.util.CustomFeedbackPanel;
import br.com.unika.util.Retorno;
import wicket.contrib.input.events.EventType;
import wicket.contrib.input.events.InputBehavior;
import wicket.contrib.input.events.key.KeyType;

public class CadastrarConta extends Panel {

	private static final long serialVersionUID = 1L;

	private Conta conta;
	private Banco bancoSelecionado;
	private CustomFeedbackPanel feedbackPanel;
	private Form<Conta> formCriarConta;
	private DropDownChoice<Agencia> dropAgencia;
	private TextField<String> numeroConta;
	private String contaGerada;
	private EnumTipoConta tipoDeContaEnum;

	@SpringBean(name = "servicoConta")
	private ServicoConta servicoConta;

	@SpringBean(name = "servicoBanco")
	private ServicoBanco servicoBanco;

	@SpringBean(name = "servicoAgencia")
	private ServicoAgencia servicoAgencia;

	public CadastrarConta(String id, Usuario usuario) {
		super(id);
		conta = new Conta();
		conta.setUsuario(usuario);
		montarTela();
	}

	public CadastrarConta(String id, Conta contaAlterar) {
		super(id);
		conta = contaAlterar;
		montarTela();
	}

	private void montarTela() {
		feedbackPanel = new CustomFeedbackPanel("feedBackPanel");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		add(formCriarConta());

	}

	private Form<Conta> formCriarConta() {
		formCriarConta = new Form<Conta>("formCriarConta", new CompoundPropertyModel<Conta>(conta));
		formCriarConta.setOutputMarkupId(true);

		formCriarConta.add(campoDropBanco());
		formCriarConta.add(campoDropAgencia());
		formCriarConta.add(campoConta());
		formCriarConta.add(campoDropTipoConta());

		formCriarConta.add(botaoCancelar());
		formCriarConta.add(botaoSalvar());

		return formCriarConta;
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

		DropDownChoice<EnumTipoConta> dropTipoConta = new DropDownChoice<EnumTipoConta>("tipoConta",
				new PropertyModel<>(this, "tipoDeContaEnum"), model, tipoConta);

		dropTipoConta.setOutputMarkupId(true);

		return dropTipoConta;
	}

	private TextField<String> campoConta() {
		numeroConta = new TextField<>("conta");
		numeroConta.setRequired(true);
		numeroConta.setEnabled(false);
		numeroConta.setOutputMarkupId(true);
		return numeroConta;
	}

	private DropDownChoice<Agencia> campoDropAgencia() {

		dropAgencia = new DropDownChoice<>("agencia");
		dropAgencia.setOutputMarkupId(true);
		dropAgencia.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				contaGerada = servicoConta.gerarConta(bancoSelecionado, dropAgencia.getModelObject());
				conta.setConta(contaGerada);
				target.add(numeroConta);
			}
		});
		dropAgencia.setEnabled(false);

		return dropAgencia;
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

		DropDownChoice<Banco> dropBanco = new DropDownChoice<>("banco",
				new PropertyModel<Banco>(this, "bancoSelecionado"), model, banco);
		dropBanco.setOutputMarkupId(true);
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
				conta.setAgencia(null);
				conta.setConta(null);
				dropAgencia.setChoiceRenderer(agencia);
				dropAgencia.setChoices(model);
				if (bancoSelecionado == null) {
					dropAgencia.setEnabled(false);
				} else {
					dropAgencia.setEnabled(true);
				}
				target.add(dropAgencia);
				target.add(numeroConta);
			}

		});
		return dropBanco;
	}

	private AjaxSubmitLink botaoSalvar() {
		AjaxSubmitLink acaoSubmit = new AjaxSubmitLink("acaoSubmit", formCriarConta) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Retorno retorno = new Retorno(true, null);
				conta.setTipoConta(getTipoDeContaEnum().getValor());
				if (conta.getIdConta() == null) {
					retorno = servicoConta.incluir(conta);
				} else {
					retorno = servicoConta.alterar(conta);
				}

				if (retorno.isSucesso()) {
					acaoSalvarCancelarConta(target, true);
				} else {
					feedbackPanel = retorno.getMensagens(feedbackPanel);
					target.add(feedbackPanel);
				}
				super.onSubmit(target, form);
			}

			@Override
			protected void onError(AjaxRequestTarget target, Form<?> form) {
				target.add(feedbackPanel);
				super.onError(target, form);
			}
		};
		return acaoSubmit;
	}

	private AjaxLink<Void> botaoCancelar() {
		AjaxLink<Void> botaoCancelar = new AjaxLink<Void>("cancelar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				acaoSalvarCancelarConta(target, false);
			}

		};
		botaoCancelar.add(new InputBehavior(new KeyType[] { KeyType.Escape }, EventType.click));
		return botaoCancelar;
	}

	protected void acaoSalvarCancelarConta(AjaxRequestTarget target, boolean tecla) {
		// TODO Auto-generated method stub

	}

	public Banco getBancoSelecionado() {
		return bancoSelecionado;
	}

	public void setBancoSelecionado(Banco bancoSelecionado) {
		this.bancoSelecionado = bancoSelecionado;
	}

	public String getContaGerada() {
		return contaGerada;
	}

	public void setContaGerada(String contaGerada) {
		this.contaGerada = contaGerada;
	}

	public EnumTipoConta getTipoDeContaEnum() {
		return tipoDeContaEnum;
	}

	public void setTipoDeContaEnum(EnumTipoConta tipoDeContaEnum) {
		this.tipoDeContaEnum = tipoDeContaEnum;
	}

}
