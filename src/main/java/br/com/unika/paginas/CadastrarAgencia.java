package br.com.unika.paginas;

import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.PermissaoDeAcesso;
import br.com.unika.servicos.ServicoAgencia;
import br.com.unika.servicos.ServicoBanco;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.Retorno;
import wicket.contrib.input.events.EventType;
import wicket.contrib.input.events.InputBehavior;
import wicket.contrib.input.events.key.KeyType;

public class CadastrarAgencia extends Panel {

	private static final long serialVersionUID = 1L;

	private NotificationPanel notificationPanel;
	private Agencia agencia;
	private Form<Agencia> formCriarAgencia;
	private TextField<String> nome, numero;
	private DropDownChoice<Banco> DropBanco;

	@SpringBean(name = "servicoAgencia")
	private ServicoAgencia servicoAgencia;
	
	@SpringBean(name="servicoBanco")
	private ServicoBanco servicoBanco;

	public CadastrarAgencia(String id) {
		super(id);
		agencia = new Agencia();
		montarTela();
	}

	public CadastrarAgencia(String id, Agencia agenciaAlterar) {
		super(id);
		agencia = agenciaAlterar;
		montarTela();
	}

	private void montarTela() {
		notificationPanel = new NotificationPanel("feedBackPanel");
		notificationPanel.setOutputMarkupId(true);
		add(notificationPanel);
		add(formCriarAgencia());

	}

	private Form<Agencia> formCriarAgencia() {
		formCriarAgencia = new Form<Agencia>("formCriarAgencia", new CompoundPropertyModel<>(agencia));
		formCriarAgencia.setOutputMarkupId(true);
		
		formCriarAgencia.add(titulo());
		
		formCriarAgencia.add(campoNome());
		formCriarAgencia.add(campoNumero());
		formCriarAgencia.add(campoBanco());
		
		formCriarAgencia.add(botaoCancelar());
		formCriarAgencia.add(botaoSalvar());

		return formCriarAgencia;
	}

	private Label titulo() {
		Label titulo;
		if (agencia.getIdAgencia() == null) {
			titulo = new Label("titulo","Incluindo Nova Agência");
		}else {
			titulo = new Label("titulo","Editando Agência: "+agencia.getNumeroNomeAgencia());
		}
		return titulo;
	}

	private TextField<String> campoNumero() {
		numero = new TextField<>("numero");
		numero.setRequired(true);
		return numero;
	}

	private TextField<String> campoNome() {
		nome = new TextField<>("nome");
		nome.setRequired(true);
		return nome;
	}
	
	private DropDownChoice<Banco> campoBanco() {
		ChoiceRenderer<Banco> banco = new ChoiceRenderer<Banco>("nome", "idBanco");
		IModel<List<Banco>> model = new LoadableDetachableModel<List<Banco>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Banco> load() {

				return servicoBanco.listar();
			}

		};

		DropBanco = new DropDownChoice<>("banco", model, banco);

		return DropBanco;
	}


	private AjaxSubmitLink botaoSalvar() {
		AjaxSubmitLink acaoSubmit = new AjaxSubmitLink("acaoSubmit", formCriarAgencia) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Retorno retorno = new Retorno(true, null);

				if (agencia.getIdAgencia() == null) {
					retorno = servicoAgencia.incluir(agencia);
				} else {
					retorno = servicoAgencia.alterar(agencia);
				}

				if (retorno.isSucesso()) {
					acaoSalvarCancelarAgencia(target,true);
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
	
	private AjaxLink<Void> botaoCancelar() {
		AjaxLink<Void> botaoCancelar = new AjaxLink<Void>("cancelar") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				acaoSalvarCancelarAgencia(target, false);
			}
			
		};
		botaoCancelar.add(new InputBehavior(new KeyType[] {KeyType.Escape}, EventType.click));
		return botaoCancelar;
	}

	protected void acaoSalvarCancelarAgencia(AjaxRequestTarget target,boolean tecla) {
		// TODO Auto-generated method stub

	}
}
