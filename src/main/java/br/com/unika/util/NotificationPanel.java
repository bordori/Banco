package br.com.unika.util;

import java.io.Serializable;
import java.util.ArrayList;

import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;

public class NotificationPanel extends FeedbackPanel implements Serializable {

	private static final long serialVersionUID = 1L;

	public NotificationPanel(String id) {
		super(id);

	}

	/*
	 * public void montarFeedBack() { this.add(new AttributeModifier("style",
	 * "position: absolute; top: 40px;height:auto; list-style-type: none; text-align: center;padding-top:20px; right: 0px; z-index: 999999; width: auto; left:70%;margin-right: 10px"
	 * )); this.add(new AttributeModifier("class",
	 * "  alert-danger shadow-lg hideMe ")); }
	 */

	public void mensagem(String msg, String tipoMensagem) {

		if (tipoMensagem.equalsIgnoreCase("sucesso")) {
			this.success(msg);
			// this.add(new AttributeModifier("class", " alert-success shadow-lg hideMe "));
		} else if (tipoMensagem.equalsIgnoreCase("erro")) {
			this.error(msg);
			// this.add(new AttributeModifier("class", " alert-danger shadow-lg hideMe "));
		} else {
			this.success(msg);
			// this.add(new AttributeModifier("class", " alert-success shadow-lg hideMe "));
		}

	}

	public void mensagem(ArrayList<String> msg, String tipoMensagem) {

		if (tipoMensagem.equalsIgnoreCase("sucesso")) {
			for (String string : msg) {
				this.success(string);
			}
			// this.add(new AttributeModifier("class", " alert-success shadow-lg hideMe
			// alertStyle"));

		} else if (tipoMensagem.equalsIgnoreCase("erro")) {
			for (String string : msg) {
				this.error(string);
			}
			// this.add(new AttributeModifier("class", " alert-danger shadow-lg hideMe "));

		} else {
			for (String string : msg) {
				this.success(string);
			}
			// this.add(new AttributeModifier("class", " alert-success shadow-lg hideMe "));
		}

	}

	@Override
	protected String getCSSClass(FeedbackMessage message) {
		String css;
		switch (message.getLevel()) {
		case FeedbackMessage.SUCCESS:
			css = "alert-success  shadow-lg hideMe alertStyle";
			break;
		case FeedbackMessage.ERROR:
			css = " alert-danger  shadow-lg hideMe alertStyle";
			break;

		default:
			css = "alert-success  shadow-lg hideMe alertStyle";
		}
		return css;
	}

}