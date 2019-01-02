package br.com.unika.paginas;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormChoiceComponentUpdatingBehavior;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.enums.EnumTipoConta;
import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoAgencia;
import br.com.unika.servicos.ServicoBanco;
import br.com.unika.servicos.ServicoConta;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.Retorno;

public class CadastrarConta extends Panel {

	private static final long serialVersionUID = 1L;

	private Conta conta;
	private Banco bancoSelecionado;
	private NotificationPanel notificationPanel;
	private Form<Conta> formCriarConta;
	private DropDownChoice<Agencia> dropAgencia;
	private DropDownChoice<Banco> dropBanco;
	private TextField<String> numeroConta;
	private DropDownChoice<EnumTipoConta> dropTipoConta;
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
		notificationPanel = new NotificationPanel("feedBackPanel");
		notificationPanel.setOutputMarkupId(true);
		add(notificationPanel);
		add(formCriarConta());

	}

	private Form<Conta> formCriarConta() {
		formCriarConta = new Form<Conta>("formCriarConta", new CompoundPropertyModel<Conta>(conta));
		formCriarConta.setOutputMarkupId(true);

		formCriarConta.add(campoDropBanco());
		formCriarConta.add(campoDropAgencia());
		formCriarConta.add(campoConta());
		formCriarConta.add(campoDropTipoConta());

		formCriarConta.add(acaoSubmit());

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

		dropTipoConta = new DropDownChoice<EnumTipoConta>("tipoConta", new PropertyModel<>(this, "tipoDeContaEnum"),
				model, tipoConta);
		dropTipoConta.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			
			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				conta.setTipoConta(getTipoDeContaEnum().getValor());
				
			}
		});
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

		dropBanco = new DropDownChoice<>("banco", new PropertyModel<Banco>(this, "bancoSelecionado"), model, banco);
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
						Search search = new Search(Agencia.class);
						search.addFilterEqual("banco", bancoSelecionado);
						return servicoAgencia.search(search);
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

	private AjaxSubmitLink acaoSubmit() {
		AjaxSubmitLink acaoSubmit = new AjaxSubmitLink("acaoSubmit", formCriarConta) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Retorno retorno = new Retorno(true, null);
				
				if (conta.getIdConta() == null) {
					retorno = servicoConta.incluir(conta);
				} else {
					retorno = servicoConta.alterar(conta);
				}

				if (retorno.isSucesso()) {
					acaoSubmitCriarConta(target);
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

	protected void acaoSubmitCriarConta(AjaxRequestTarget target) {
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
