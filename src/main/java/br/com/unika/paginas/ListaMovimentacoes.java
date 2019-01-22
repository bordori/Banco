package br.com.unika.paginas;

import java.util.Calendar;
import java.util.List;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.markup.html.form.DateTextField;
import org.apache.wicket.extensions.yui.calendar.DatePicker;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.model.PropertyModel;

import br.com.unika.enums.EnumTipoMovimentacao;
import wicket.contrib.input.events.EventType;
import wicket.contrib.input.events.InputBehavior;
import wicket.contrib.input.events.key.KeyType;

public class ListaMovimentacoes extends Panel {

	private static final long serialVersionUID = 1L;

	private Form<String> formFiltro;
	private DateTextField dataInicial, dataFinal;
	private Calendar dateInicial, dateFinal;
	private EnumTipoMovimentacao valorTipoMovimentacao;

	public ListaMovimentacoes(String id) {
		super(id);
		add(formFiltro());
	}

	private Form<String> formFiltro() {
		formFiltro = new Form<String>("formFiltro");

		formFiltro.add(dataInicial());
		formFiltro.add(dataFinal());
		formFiltro.add(DropDownTipoDeMovimentacao());
		formFiltro.add(sim());
		formFiltro.add(nao());

		return formFiltro;
	}

	private AjaxSubmitLink sim() {
		AjaxSubmitLink sim = new AjaxSubmitLink("sim", formFiltro) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				dateInicial.set(Calendar.MILLISECOND, 0);
				dateInicial.set(Calendar.SECOND, 0);
				dateInicial.set(Calendar.MINUTE, 0);
				dateInicial.set(Calendar.HOUR, 0);

				dateFinal.set(Calendar.MILLISECOND, 59);
				dateFinal.set(Calendar.SECOND, 59);
				dateFinal.set(Calendar.MINUTE, 59);
				dateFinal.set(Calendar.HOUR, 23);
				execultarFechar(target, true, getDateInicial(), getDateFinal(), valorTipoMovimentacao);
				super.onSubmit(target, form);
				execultarFechar(target, false, null, null, null);
			}
		};

		return sim;

	}

	private AjaxLink<Void> nao() {
		AjaxLink<Void> nao = new AjaxLink<Void>("cancelar") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				execultarFechar(target, false, null, null, null);
			}
		};
		nao.add(new InputBehavior(new KeyType[] { KeyType.Escape }, EventType.click));
		return nao;

	}

	public void execultarFechar(AjaxRequestTarget target, boolean tecla, Calendar dataInicial, Calendar dataFinal,
			EnumTipoMovimentacao valorTipoMovimentacao) {
	}

	private DropDownChoice<EnumTipoMovimentacao> DropDownTipoDeMovimentacao() {

		ChoiceRenderer<EnumTipoMovimentacao> choiceRenderer = new ChoiceRenderer<EnumTipoMovimentacao>("descricao",
				"valor");

		IModel<List<EnumTipoMovimentacao>> model = new LoadableDetachableModel<List<EnumTipoMovimentacao>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<EnumTipoMovimentacao> load() {
				return EnumTipoMovimentacao.getTipoDeMovimentacao();
			}
		};

		DropDownChoice<EnumTipoMovimentacao> tipoMovimentacao = new DropDownChoice<>("tipoMovimentacao",
				new PropertyModel<>(this, "valorTipoMovimentacao"), model, choiceRenderer);

		return tipoMovimentacao;
	}

	private DateTextField dataInicial() {
		dataInicial = new DateTextField("dataInicial", new PropertyModel<>(this, "dateInicial"), "dd/MM/yyyy");
		DatePicker datePicker = new DatePicker() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean alignWithIcon() {

				return true;
			}

			@Override
			protected boolean enableMonthYearSelection() {
				return true;
			}

		};
		dataInicial.add(new AttributeModifier("onfocus", "$(this).mask('99/99/9999');"));
		datePicker.setAutoHide(true);
		datePicker.setShowOnFieldClick(true);
		dataInicial.add(datePicker);
		dataInicial.setOutputMarkupId(true);

		return dataInicial;
	}

	private DateTextField dataFinal() {
		dataFinal = new DateTextField("dataFinal", new PropertyModel<>(this, "dateFinal"), "dd/MM/yyyy");
		DatePicker datePicker = new DatePicker() {
			/**
			 * 
			 */
			private static final long serialVersionUID = 1L;

			@Override
			protected boolean alignWithIcon() {

				return true;
			}

			@Override
			protected boolean enableMonthYearSelection() {
				return true;
			}

		};
		dataFinal.add(new AttributeModifier("onfocus", "$(this).mask('99/99/9999');"));
		datePicker.setAutoHide(true);
		datePicker.setShowOnFieldClick(true);
		dataFinal.add(datePicker);
		dataFinal.setOutputMarkupId(true);

		return dataFinal;
	}

	public Calendar getDateInicial() {
		return dateInicial;
	}

	public void setDateInicial(Calendar dateInicial) {
		this.dateInicial = dateInicial;
	}

	public Calendar getDateFinal() {
		return dateFinal;
	}

	public void setDateFinal(Calendar dateFinal) {
		this.dateFinal = dateFinal;
	}

}
