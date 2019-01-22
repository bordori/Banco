package br.com.unika.paginas;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.PropertyModel;

import br.com.unika.enums.EnumTipoMovimentacao;
import br.com.unika.util.Validacao;
import wicket.contrib.input.events.EventType;
import wicket.contrib.input.events.InputBehavior;
import wicket.contrib.input.events.key.KeyType;

public class OperacaoSaqueDeposito extends Panel {
	private static final long serialVersionUID = 1L;

	private Double valor;
	private Form<Double> formDeposito;

	public OperacaoSaqueDeposito(String id, EnumTipoMovimentacao movimentacao, Double saldo) {
		super(id);
		if (movimentacao.getValor() == 1) /* Saque */ {
			add(new Label("titulo", "Saque"));
			add(formConfirmacao("100.000,00", "Saldo Disponivel:" + Validacao.FormatarSaldo(saldo),
					"Digite o Valor do Saque!"));
		} else if (movimentacao.getValor() == 2) /* Deposito */ {
			add(new Label("titulo", "Deposito"));
			add(formConfirmacao("1.000.000,00", "", "Digite o Valor do Deposito!"));
		}

	}

	private Form<Double> formConfirmacao(String max, String saldoDisponivel, String mensagem) {
		formDeposito = new Form<Double>("formDeposito");
		formDeposito.setOutputMarkupId(true);
		formDeposito.add(new Label("mensagem", mensagem));
		formDeposito.add(new Label("valorMinMax", "Mínimo:R$ 2,00   Máximo:R$ " + max));
		formDeposito.add(new Label("saldoDisponivel", saldoDisponivel));
		formDeposito.add(sim());
		formDeposito.add(nao());
		formDeposito.add(valor());
		return formDeposito;
	}

	private TextField<Double> valor() {
		TextField<Double> valor = new TextField<Double>("valor", new PropertyModel<>(this, "valor"));
		valor.setRequired(true);
		return valor;
	}

	public AjaxSubmitLink sim() {
		AjaxSubmitLink sim = new AjaxSubmitLink("sim") {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				execultarFechar(target, true, getValor());
				super.onSubmit(target, form);
			}
		};
		sim.add(new InputBehavior(new KeyType[] {KeyType.Enter} , EventType.click));
		return sim;

	}

	public AjaxLink<Void> nao() {
		AjaxLink<Void> nao = new AjaxLink<Void>("cancelar") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				execultarFechar(target, false, null);
			}
		};
		nao.add(new InputBehavior(new KeyType[] { KeyType.Escape }, EventType.click));
		return nao;

	}

	public void execultarFechar(AjaxRequestTarget target, boolean tecla, Double valor) {
	}

	public Double getValor() {
		return valor;
	}

	public void setValor(Double valor) {
		this.valor = valor;
	}
}
