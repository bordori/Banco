package br.com.unika.util;
import java.io.Serializable;
import java.util.ArrayList;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.feedback.FeedbackMessage;
import org.apache.wicket.markup.html.panel.FeedbackPanel;
import org.apache.wicket.model.Model;

public class NotificationPanel extends FeedbackPanel implements Serializable{

		private static final long serialVersionUID = 1L;

		public NotificationPanel(String id) {
			super(id);
			
			

		}
		
		public void montarFeedBack() {
			this.add(new AttributeModifier("style",
					"position: absolute; top: 40px;height:auto; list-style-type: none; text-align: center;padding-top:20px; right: 0px; z-index: 999999; width: auto; left:70%;margin-right: 10px"));
			this.add(new AttributeModifier("class", "  alert-danger shadow-lg hideMe "));
		} 

		public void mensagem(String msg, String tipoMensagem) {
			
			this.add(new AttributeModifier("style",
					"position: absolute; top: 40px;height:auto; list-style-type: none; text-align: center;padding-top:20px; right: 0px; z-index: 999999; width: auto; left:70%;margin-right: 10px"));
			if (tipoMensagem.equalsIgnoreCase("sucesso")) {
				this.info(msg);
				this.add(new AttributeModifier("class", "  alert-success  shadow-lg hideMe "));
			} else if (tipoMensagem.equalsIgnoreCase("erro")) {
				this.error(msg);
				this.add(new AttributeModifier("class", "  alert-danger  shadow-lg hideMe "));
			} else {
				this.info(msg);
				this.add(new AttributeModifier("class", "  alert-success  shadow-lg hideMe "));
			}

		}
		
		public void mensagem(ArrayList<String> msg,String tipoMensagem) {
			
			this.add(new AttributeModifier("style",
					"position: absolute; top: 40px;height:auto; list-style-type: none; text-align: center;padding-top:20px; right: 0px; z-index: 999999; width: auto; left:70%;margin-right: 10px"));
			if (tipoMensagem.equalsIgnoreCase("sucesso")) {
				for (String string : msg) {
					this.info(string);
				}
				this.add(new AttributeModifier("class", "  alert-success  shadow-lg hideMe "));
				
			} else if (tipoMensagem.equalsIgnoreCase("erro")) {
				for (String string : msg) {
					this.error(string);
				}
				this.add(new AttributeModifier("class", "  alert-danger  shadow-lg hideMe "));
				
			} else {
				for (String string : msg) {
					this.info(string);
				}
				this.add(new AttributeModifier("class", "  alert-success  shadow-lg hideMe "));
			}

		}

	}