package br.com.unika.paginas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;

public class HomePage extends NavBar {
	private static final long serialVersionUID = 1L;

	private ModalWindow janela;

	public HomePage() {
		janela = new ModalWindow("janela");
		this.add(janela);

	}

	@Override
	public void acaoRegistro(AjaxRequestTarget target) {
		super.acaoRegistro(target);
		janela.setMinimalWidth(800);
		janela.setInitialWidth(1200);
		janela.setMinimalHeight(400);
		janela.setInitialHeight(550);
		CadastrarUsuario cadastrarUsuario = new CadastrarUsuario(janela.getContentId());
		janela.setContent(cadastrarUsuario);
		janela.show(target);
	}

}
