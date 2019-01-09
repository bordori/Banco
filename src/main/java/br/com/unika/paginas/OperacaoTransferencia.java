package br.com.unika.paginas;

import org.apache.wicket.AttributeModifier;
import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.form.AjaxFormComponentUpdatingBehavior;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.form.validation.IFormValidator;
import org.apache.wicket.markup.html.panel.Panel;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.model.PropertyModel;

import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Contato;
import br.com.unika.util.Validacao;

public class OperacaoTransferencia extends CadastrarContato {

	private static final long serialVersionUID = 1L;

	private ModalWindow janela;
	private TextField<Double> valor;
	private Double valorTransferencia;
	private Conta conta;
	private Double taxa = 9.4;
	private Label labelTaxa;

	public OperacaoTransferencia(String id, Conta conta) {
		super(id);
		this.conta = conta;
		form();
	}

	private void form() {
		add(initModal());
		formCriarContato.add(acaoLocalizarContato());
		formCriarContato.add(campoValor());
		formCriarContato.add(new Label("saldoDisponivel", Validacao.FormatarSaldo(conta.getSaldo())));
		formCriarContato.add(compoLabelTaxa());
	}

	private Label compoLabelTaxa() {
		labelTaxa = new Label("taxa", "Taxa: " + "R$ 0,00");
		labelTaxa.setOutputMarkupId(true);
		return labelTaxa;
	}

	private TextField<Double> campoValor() {
		valor = new TextField<Double>("valor", new PropertyModel<Double>(this, "valorTransferencia"));
		valor.setRequired(true);
		valor.add(new AjaxFormComponentUpdatingBehavior("onkeyup") {

			@Override
			protected void onUpdate(AjaxRequestTarget target) {		
				if (bancoSelecionado != null) {
					if (!bancoSelecionado.getNumero().equals(conta.getAgencia().getBanco().getNumero())) {
						taxa = 9.4;
						labelTaxa.setDefaultModel(new Model<>("Taxa: " + Validacao.FormatarSaldo(taxa)));
					}else {
						taxa = 0.0;
						labelTaxa.setDefaultModel(new Model<>("Taxa: " + "R$ 0,00"));
					}

				}
				target.add(labelTaxa);
			}
		});
		return valor;
	}

	private AjaxLink<Void> acaoLocalizarContato() {
		AjaxLink<Void> acaoLocalizarContato = new AjaxLink<Void>("acaoLocalizarContato") {

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setInitialWidth(450);
				janela.setInitialHeight(300);
				janela.setResizable(false);
				SelecionarContato selecionarContato = new SelecionarContato(janela.getContentId()) {
					@Override
					public void contatoSelecionado(AjaxRequestTarget target, Contato contatoSelecionado) {
						super.contatoSelecionado(target, contatoSelecionado);
						contato = contatoSelecionado;
						janela.close(target);
						formCriarContato.setModel(new CompoundPropertyModel<Contato>(contato));
						preencherDropDown();
						dropAgencia.setEnabled(true);
						valor.add(new AttributeModifier("class", "form-control monetario"));
						valorTransferencia = 0.0;

						target.add(container);
					}
				};
				janela.setContent(selecionarContato);
				janela.show(target);
			}
		};
		return acaoLocalizarContato;
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

	public Double getValorTransferencia() {
		return valorTransferencia;
	}

	public void setValorTransferencia(Double valorTransferencia) {
		this.valorTransferencia = valorTransferencia;
	}

	@Override
	protected void acaoSubmitCriarConta(AjaxRequestTarget target, Boolean tecla) {
		super.acaoSubmitCriarConta(target, tecla);
		acaoSubmitTrasferencia(target, tecla, conta, valorTransferencia, contato, taxa);
	}

	public void acaoSubmitTrasferencia(AjaxRequestTarget target, Boolean tecla, Conta conta, Double valorTransferencia,
			Contato contato, Double taxa) {
		// TODO Auto-generated method stub

	}

}
