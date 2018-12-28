package br.com.unika.paginas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.spring.injection.annot.SpringBean;

import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.servicos.ServicoConta;
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.Retorno;

public class CadastrarConta extends Panel {

	private static final long serialVersionUID = 1L;
	
	private Conta conta;
	private NotificationPanel notificationPanel;
	private Form<Conta> formCriarConta;
	
	@SpringBean(name="servicoConta")
	private ServicoConta servicoConta;

	public CadastrarConta(String id) {
		super(id);
		conta = new Conta();
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
		formCriarConta = new Form<Conta>("formCriarBanco", new CompoundPropertyModel<Conta>(conta));
		formCriarConta.setOutputMarkupId(true);
		
		formCriarConta.add(acaoSubmit());
		
		return formCriarConta;
	}
	
	private AjaxSubmitLink acaoSubmit() {
		AjaxSubmitLink acaoSubmit = new AjaxSubmitLink("acaoSubmit",formCriarConta) {
			private static final long serialVersionUID = 1L;
			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Retorno retorno = new Retorno(true, null);
				
				if(conta.getIdConta() == null) {
					retorno = servicoConta.incluir(conta);
				}else {
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

}
