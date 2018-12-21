package br.com.unika.paginas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.markup.html.form.Form;

import br.com.unika.util.NotificationPanel;

public class HomePage extends WebPage {
	private static final long serialVersionUID = 1L;

	private ModalWindow janela;

	private NotificationPanel notificationPanel;

	public HomePage() {
		notificationPanel = new NotificationPanel("feedBack");
		notificationPanel.setOutputMarkupId(true);
		add(notificationPanel);
		add(initModal());
		add(acaoResgistrar());
	}

	private ModalWindow initModal() {
		janela = new ModalWindow("janela");
		janela.setWindowClosedCallback(new WindowClosedCallback() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target) {
				// TODO Auto-generated method stub

			}
		});
		return janela;
	}
	
	
	

	private AjaxLink<Void> acaoResgistrar() {
		AjaxLink<Void> ajaxSubmitLink = new AjaxLink<Void>("acaoRegistrar") {

			private static final long serialVersionUID = 1L;


			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setMinimalWidth(800);
				janela.setInitialWidth(1200);
				janela.setMinimalHeight(400);
				janela.setInitialHeight(550);
				CadastrarUsuario cadastrarUsuario = new CadastrarUsuario(janela.getContentId()) {
					@Override
					public void acaoSubmit(AjaxRequestTarget target) {
						janela.close(target);
						notificationPanel.mensagem("O Usuario Foi Adicionado com sucesso", "sucesso");
						target.add(notificationPanel);;
						super.acaoSubmit(target);
					}
				};
				janela.setContent(cadastrarUsuario);
				janela.show(target);				
			}
		};

		return ajaxSubmitLink;
	}

}
