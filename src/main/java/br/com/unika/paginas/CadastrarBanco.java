package br.com.unika.paginas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import br.com.unika.modelo.Banco;
import br.com.unika.servicos.ServicoBanco;
import br.com.unika.util.CustomFeedbackPanel;
import br.com.unika.util.Retorno;
import wicket.contrib.input.events.EventType;
import wicket.contrib.input.events.InputBehavior;
import wicket.contrib.input.events.key.KeyType;

public class CadastrarBanco extends Panel {

	private static final long serialVersionUID = 1L;
	
	private CustomFeedbackPanel feedbackPanel;
	private Form<Banco> formCriarBanco;
	private Banco banco;
	
	@SpringBean(name="servicoBanco")
	private ServicoBanco servicoBanco;

	public CadastrarBanco(String id) {
		super(id);
		banco = new Banco();
		montarTela();
	} 
	
	public CadastrarBanco(String id,Banco banco) {
		super(id);
		this.banco = banco;
		montarTela();
	}

	private void montarTela() {
		feedbackPanel = new CustomFeedbackPanel("feedBackPanel");
		feedbackPanel.setOutputMarkupId(true);
		add(feedbackPanel);
		add(formCriarBanco());
		
	}

	private Form<Banco> formCriarBanco() {
		formCriarBanco = new Form<Banco>("formCriarBanco", new CompoundPropertyModel<Banco>(banco));
		formCriarBanco.setOutputMarkupId(true);
		
		formCriarBanco.add(titulo());
		
		formCriarBanco.add(campoNumero());
		formCriarBanco.add(campoNome());
		
		formCriarBanco.add(botaoCancelar());
		formCriarBanco.add(botaoSalvar());
		
		return formCriarBanco;
	}

	private Label titulo() {
		Label titulo;
		if (banco.getIdBanco() == null) {
			titulo = new Label("titulo","Incluindo Novo Banco");
		}else {
			titulo = new Label("titulo","Editando Banco: "+banco.getNumeroNomeBanco());
		}
		return titulo;
	}

	private AjaxSubmitLink botaoSalvar() {
		AjaxSubmitLink acaoSubmit = new AjaxSubmitLink("acaoSubmit",formCriarBanco) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Retorno retorno = new Retorno(true, null);
				
				if(banco.getIdBanco() == null) {
					retorno = servicoBanco.incluir(banco);
				}else {
					retorno = servicoBanco.alterar(banco);
				}
				
				if (retorno.isSucesso()) {
					acaoSalvarCancelarBanco(target,true);
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
				acaoSalvarCancelarBanco(target, false);
			}
			
		};
		botaoCancelar.add(new InputBehavior(new KeyType[] {KeyType.Escape}, EventType.click));
		return botaoCancelar;
	}

	public void acaoSalvarCancelarBanco(AjaxRequestTarget target,boolean tecla) {
				
	}

	private TextField<String> campoNome() {
		TextField<String> nome = new TextField<>("nome");
		nome.setRequired(true);
		return nome;
	}

	private TextField<String> campoNumero() {
		TextField<String> numero = new TextField<>("numero");
		numero.setRequired(true);
		return numero;
	}

}
