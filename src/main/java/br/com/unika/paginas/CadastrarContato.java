package br.com.unika.paginas;

import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
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

public class CadastrarContato extends Panel {
	private static final long serialVersionUID = 1L;

	private Contato contato;
	private Form<Contato> formCriarContato;
	private NotificationPanel notificationPanel;
	private TextField<String> apelido,cpf,conta;
	private DropDownChoice<Banco> dropBanco;
	private DropDownChoice<Agencia> dropAgencia;
	private DropDownChoice<EnumTipoConta> dropTipoConta;
	private Banco bancoSelecionado;
	private Agencia agenciaSelecionado;
	private EnumTipoConta tipoDeContaEnum;
	
	@SpringBean(name="servicoBanco")
	private ServicoBanco servicoBanco;
	
	@SpringBean(name="servicoAgencia")
	private ServicoAgencia servicoAgencia;
	
	@SpringBean(name="servicoContato")
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
		Search search = new Search(Banco.class);
		search.addFilterEqual("numero", contato.getNumeroBanco());
		bancoSelecionado = servicoBanco.search(search).get(0);
		
		search = new Search(Agencia.class);
		search.addFilterEqual("numero", contato.getNumeroAgencia());
		search.addFilterEqual("banco", bancoSelecionado);
		agenciaSelecionado = servicoAgencia.search(search).get(0);
		montarTela();
		dropAgencia.setEnabled(true);
	}
	
	private void montarTela() {
		notificationPanel = new NotificationPanel("feedBackPanel");
		notificationPanel.setOutputMarkupId(true);
		add(notificationPanel);
		add(montarForm());
	}

	private Form<Contato> montarForm() {
		formCriarContato = new Form<Contato>("formCriarContato",new CompoundPropertyModel<Contato>(contato));
		formCriarContato.setOutputMarkupId(true);
		
		formCriarContato.add(campoApelido());
		formCriarContato.add(campoCpf());
		formCriarContato.add(campoConta());
		formCriarContato.add(campoDropBanco());
		formCriarContato.add(campoDropAgencia());
		formCriarContato.add(campoDropTipoConta());
		formCriarContato.add(acaoSubmit());
		
		return formCriarContato;
	}

	private TextField<String> campoConta() {
		conta = new TextField<>("conta");
		conta.add((new AttributeModifier("onfocus", "$(this).mask('999999');")));
		conta.setRequired(true);
		return conta;
	}

	private TextField<String> campoCpf() {
		cpf = new TextField<>("cpf");
		cpf.add((new AttributeModifier("onfocus", "$(this).mask('999.999.999-99');")));
		cpf.setRequired(true);
		return cpf;
	}

	private TextField<String> campoApelido() {
		apelido = new TextField<String>("apelido");
		apelido.setRequired(true);		
		
		return apelido;
	}
	
	private AjaxSubmitLink acaoSubmit() {
		AjaxSubmitLink acaoSubmit = new AjaxSubmitLink("acaoNovoContato", formCriarContato) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Retorno retorno = new Retorno(true, null);
				contato.setNomeBanco(bancoSelecionado.getNome());
				contato.setNumeroBanco(bancoSelecionado.getNumero());
				if (contato.getIdContato() == null) {
					retorno = servicoContato.incluir(contato);
				} else {
					retorno = servicoContato.alterar(contato);
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
				contato.setTipoConta(tipoDeContaEnum.getValor());
				
			}
		});
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

		dropBanco = new DropDownChoice<>("banco", new PropertyModel<Banco>(this, "bancoSelecionado"), model, banco);
//		dropBanco.setOutputMarkupId(true);
		bancoSelecionado = null;
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
				agenciaSelecionado = null;
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
				
				return servicoAgencia.listar();
			}

		};
		dropAgencia = new DropDownChoice<Agencia>("agencia",new PropertyModel<Agencia>(this, "agenciaSelecionado"),model,agencia);
		dropAgencia.setOutputMarkupId(true);
		dropAgencia.add(new AjaxFormComponentUpdatingBehavior("onchange") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onUpdate(AjaxRequestTarget target) {
				contato.setNumeroAgencia(agenciaSelecionado.getNumero());
				contato.setNomeAgencia(agenciaSelecionado.getNome());
			}
		});
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
