package br.com.unika.util;

import org.apache.wicket.Component;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.PasswordTextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import wicket.contrib.input.events.EventType;
import wicket.contrib.input.events.InputBehavior;
import wicket.contrib.input.events.key.KeyType;

public class Confirmacao extends Panel {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String password;
	private Form<String> formConfirmacao;
	  
	
	public Confirmacao(String id, String string) {

		super(id);
		add(new Label("titulo", "Exclusão"));
		
		add(formConfirmacao(string));

	}

	private Form<String> formConfirmacao(String mensagem) {
		formConfirmacao = new Form<String>("formConfirmacao");
		formConfirmacao.setOutputMarkupId(true);
		formConfirmacao.add(new Label("mensagem", "Esta operação não pode ser desfeita. Confirmar exclusão "+mensagem+"?"));
		formConfirmacao.add(sim());
		formConfirmacao.add(nao());
		formConfirmacao.add(senha());
		return formConfirmacao;
	}

	private PasswordTextField senha() {
		PasswordTextField senha = new PasswordTextField("senha",new PropertyModel<>(this, "password"));
		return senha;
	}

	public AjaxSubmitLink sim() {
		AjaxSubmitLink sim = new AjaxSubmitLink("sim") {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				execultarFechar(target, true, getPassword());
				super.onSubmit(target, form);
			}
		};
			
		return sim;

	}

	public  AjaxLink  nao() {
		AjaxLink nao = new AjaxLink("cancelar") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				execultarFechar(target,false,null);
			}
		};
		nao.add(new InputBehavior(new KeyType[] {KeyType.Escape}, EventType.click));
		return nao;

	}
	
	public void execultarFechar(AjaxRequestTarget target,boolean tecla,String senha) {
		
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

}
