package br.com.unika.paginas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.upload.FileUploadField;
import org.apache.wicket.markup.html.panel.Panel;

import wicket.contrib.input.events.EventType;
import wicket.contrib.input.events.InputBehavior;
import wicket.contrib.input.events.key.KeyType;

public class ImportarAgencias extends Panel {

	private static final long serialVersionUID = 1L;
	
	private Form<Void> formImportar;
	private FileUploadField fileUpload;

	public ImportarAgencias(String id) {
		super(id);
		add(formImportar());
	}

	private Form<Void> formImportar() {
		formImportar = new Form<Void>("formImportar");
		formImportar.setOutputMarkupId(true);
		formImportar.add(FileUpload());
		formImportar.add(sim());
		formImportar.add(nao());
		return formImportar;
	}

	private FileUploadField FileUpload() {
		fileUpload = new FileUploadField("fileUpload");
		fileUpload.setOutputMarkupId(true);
		return fileUpload;
	}

	public AjaxSubmitLink sim() {
		AjaxSubmitLink sim = new AjaxSubmitLink("sim", formImportar) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				execultarFechar(target, true,fileUpload.getFileUpload());
				super.onSubmit(target, form);
			}
		};

		return sim;

	}

	public AjaxLink<Void> nao() {
		AjaxLink<Void> nao = new AjaxLink<Void>("cancelar") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				execultarFechar(target, false,null);
			}
		};
		nao.add(new InputBehavior(new KeyType[] {KeyType.Escape}, EventType.click));
		return nao;

	}

	public void execultarFechar(AjaxRequestTarget target, boolean tecla, org.apache.wicket.markup.html.form.upload.FileUpload fileUpload) {
	}

}
