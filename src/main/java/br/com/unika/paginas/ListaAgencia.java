package br.com.unika.paginas;

import java.io.IOException;
import java.io.OutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.ajax.AjaxRequestTarget;
import org.apache.wicket.ajax.markup.html.AjaxLink;
import org.apache.wicket.ajax.markup.html.form.AjaxSubmitLink;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow;
import org.apache.wicket.extensions.ajax.markup.html.modal.ModalWindow.WindowClosedCallback;
import org.apache.wicket.markup.html.WebMarkupContainer;
import org.apache.wicket.markup.html.basic.Label;
import org.apache.wicket.markup.html.form.ChoiceRenderer;
import org.apache.wicket.markup.html.form.DropDownChoice;
import org.apache.wicket.markup.html.form.Form;
import org.apache.wicket.markup.html.form.TextField;
import org.apache.wicket.markup.html.list.ListItem;
import org.apache.wicket.markup.html.list.ListView;
import org.apache.wicket.model.CompoundPropertyModel;
import org.apache.wicket.model.IModel;
import org.apache.wicket.model.LoadableDetachableModel;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.resource.AbstractResourceStreamWriter;
import org.apache.wicket.util.resource.IResourceStream;

import com.googlecode.genericdao.search.Search;

import br.com.unika.modelo.Agencia;
import br.com.unika.modelo.Banco;
import br.com.unika.modelo.Conta;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoAgencia;
import br.com.unika.servicos.ServicoBanco;
import br.com.unika.util.AJAXDownload;
import br.com.unika.util.Confirmacao;
import br.com.unika.util.CustomFeedbackPanel;
import br.com.unika.util.RelatorioExcel;
import br.com.unika.util.Retorno;

public class ListaAgencia extends NavBar implements Serializable {

	private static final long serialVersionUID = 1L;

	private ListView<Agencia> listaAgencia;
	private ModalWindow janela;
	private CustomFeedbackPanel feedbackPanel;
	private WebMarkupContainer containerListView;
	private Agencia agenciaProcurar;
	private Form<Agencia> formFiltro;
	private List<Agencia> agenciasList;
	private int count = 0;

	@SpringBean(name = "servicoAgencia")
	private ServicoAgencia servicoAgencia;

	@SpringBean(name = "servicoBanco")
	private ServicoBanco servicoBanco;

	public ListaAgencia() {
		verificarPermissaoBanco();
		preencherListView();
		add(paginacao());
		add(containerListView());
		add(initModal());
		add(formFiltro());

	}

	private void preencherListView() {
		Search search = new Search(Agencia.class);
		search.setFirstResult(count);
		search.setMaxResults(5);
		agenciasList = servicoAgencia.search(search);

	}

	private ListView<Integer> paginacao() {
		LoadableDetachableModel<List<Integer>> detachableModel = new LoadableDetachableModel<List<Integer>>() {

			private static final long serialVersionUID = 1L;

			@Override
			protected List<Integer> load() {
				int quantidade = servicoAgencia.countAll();
				float quantidadePaginas = (float) quantidade / 5;
				quantidade = (int) quantidadePaginas;
				if (quantidadePaginas > quantidade) {
					quantidade = quantidade + 1;
				}

				List<Integer> lista = new ArrayList<>();
				for (int i = 1; i <= quantidade; i++) {
					lista.add(i);
				}
				return lista;
			}
		};
		ListView<Integer> listView = new ListView<Integer>("paginas", detachableModel) {

			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Integer> item) {
				Integer pagina = item.getModelObject();

				AjaxLink<Void> index = new AjaxLink<Void>("index") {
					private static final long serialVersionUID = 1L;

					@Override
					public void onClick(AjaxRequestTarget target) {
						count = 5 * pagina;
						count = count - 5;
						preencherListView();
						target.add(containerListView);
					}
				};
				index.add(new Label("numero", pagina));
				item.add(index);
			}
		};
		return listView;
	}

	private WebMarkupContainer containerListView() {
		containerListView = new WebMarkupContainer("containerListView");
		containerListView.setOutputMarkupId(true);
		feedbackPanel = new CustomFeedbackPanel("feedBack");
		feedbackPanel.setOutputMarkupId(true);
		containerListView.add(feedbackPanel);
		containerListView.add(polularTabelaAgencias());

		return containerListView;
	}

	private Form<Agencia> formFiltro() {
		agenciaProcurar = new Agencia();
		formFiltro = new Form<Agencia>("formFiltro", new CompoundPropertyModel<>(agenciaProcurar));
		formFiltro.setOutputMarkupId(true);
		formFiltro.add(filtroNome());
		formFiltro.add(filtroNumero());
		formFiltro.add(campoBancoFiltro());

		formFiltro.add(acaoNovaAgencia());
		formFiltro.add(acaoImportar());
		formFiltro.add(acaoProcurar());
		formFiltro.add(gerarPdf());
		return formFiltro;
	}

	private AjaxSubmitLink acaoProcurar() {
		AjaxSubmitLink acaoProcurar = new AjaxSubmitLink("acaoProcurar", formFiltro) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void onSubmit(AjaxRequestTarget target, Form<?> form) {
				Search search = new Search(Agencia.class);

				if (agenciaProcurar.getNome() != null && !agenciaProcurar.getNome().equals("")) {
					search.addFilterILike("nome", "%" + agenciaProcurar.getNome() + "%");
				}
				if (agenciaProcurar.getNumero() != null && !agenciaProcurar.getNumero().equals("")) {
					search.addFilterILike("numero", "%" + agenciaProcurar.getNumero() + "%");
				}
				if (agenciaProcurar.getBanco() != null) {
					search.addFilterEqual("banco", agenciaProcurar.getBanco());
				}
				agenciasList = servicoAgencia.search(search);
				target.add(containerListView);
				super.onSubmit(target, form);
			}
		};
		return acaoProcurar;
	}

	private DropDownChoice<Banco> campoBancoFiltro() {
		ChoiceRenderer<Banco> banco = new ChoiceRenderer<Banco>("nome", "idBanco");
		IModel<List<Banco>> model = new LoadableDetachableModel<List<Banco>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Banco> load() {

				return servicoBanco.listar();
			}

		};

		DropDownChoice<Banco> DropBanco = new DropDownChoice<>("banco", model, banco);
		DropBanco.setOutputMarkupId(false);
		return DropBanco;
	}

	private TextField<String> filtroNumero() {
		TextField<String> numero = new TextField<>("numero");
		numero.setOutputMarkupId(true);
		return numero;
	}

	private TextField<String> filtroNome() {
		TextField<String> nome = new TextField<>("nome");
		return nome;
	}

	private ModalWindow initModal() {
		janela = new ModalWindow("janela");
		janela.setWindowClosedCallback(new WindowClosedCallback() {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClose(AjaxRequestTarget target) {

			}
		});
		return janela;
	}

	private ListView<Agencia> polularTabelaAgencias() {
		LoadableDetachableModel<List<Agencia>> detachableModel = new LoadableDetachableModel<List<Agencia>>() {
			private static final long serialVersionUID = 1L;

			@Override
			protected List<Agencia> load() {
				return agenciasList;
			}
		};
		listaAgencia = new ListView<Agencia>("listaAgencias", detachableModel) {
			private static final long serialVersionUID = 1L;

			@Override
			protected void populateItem(ListItem<Agencia> item) {
				Agencia agencia = item.getModelObject();

				item.add(new Label("numero", agencia.getNumero()).setOutputMarkupId(true));
				item.add(new Label("nome", agencia.getNome()).setOutputMarkupId(true));

				Search search = new Search(Conta.class);
				search.addFilterEqual("agencia", agencia);
				item.add(
						new Label("numeroContas", servicoAgencia.verificaSeTemContas(agencia)).setOutputMarkupId(true));

				item.add(new Label("nomeBanco", agencia.getBanco().getNome()).setOutputMarkupId(true));

				item.add(acaoAlterar(agencia));
				item.add(acaoDeletar(agencia));
			}
		};
		listaAgencia.setOutputMarkupId(true);
		return listaAgencia;
	}

	protected AjaxLink<Void> acaoDeletar(Agencia agencia) {
		AjaxLink<Void> acaoDeletar = new AjaxLink<Void>("deletarAgencia") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setInitialWidth(550);
				janela.setInitialHeight(300);
				janela.setResizable(false);
				Confirmacao confirmacao = new Confirmacao(janela.getContentId(), "da Agencia") {
					private static final long serialVersionUID = 1L;

					@Override
					public void execultarFechar(AjaxRequestTarget target, boolean tecla, String senha) {
						super.execultarFechar(target, tecla, senha);
						if (tecla == true) {
							Usuario usuarioLogado = (Usuario) getSession().getAttribute("usuarioLogado");
							if (usuarioLogado.getSenha().equals(senha)) {
								Retorno retorno = servicoAgencia.remover(agencia);
								if (retorno.isSucesso()) {
									feedbackPanel.success("Agencia deletada com sucesso!");
									janela.close(target);
									preencherListView();
									target.add(containerListView);
								} else {
									feedbackPanel = retorno.getMensagens(feedbackPanel);
									janela.close(target);
									target.add(feedbackPanel);
								}
							} else {
								feedbackPanel.error("Senha incorreta!");
								target.add(feedbackPanel);
							}
						} else {
							janela.close(target);
						}
					}
				};
				janela.setContent(confirmacao);
				janela.show(target);

			}
		};
		return acaoDeletar;
	}

	protected AjaxLink<Void> acaoAlterar(Agencia agencia) {
		AjaxLink<Void> acaoAlterarAgencia = new AjaxLink<Void>("alterarAgencia") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setMinimalWidth(500);
				janela.setInitialWidth(600);
				janela.setMinimalHeight(350);
				janela.setInitialHeight(400);
				CadastrarAgencia cadastrarAgencia = new CadastrarAgencia(janela.getContentId(), agencia) {
					private static final long serialVersionUID = 1L;

					@Override
					public void acaoSalvarCancelarAgencia(AjaxRequestTarget target, boolean tecla) {
						if (tecla) {
							feedbackPanel.success("Agencia alterada com sucesso!");
							preencherListView();
							target.add(containerListView);
						}
						janela.close(target);

						super.acaoSalvarCancelarAgencia(target, tecla);
					}
				};
				janela.setContent(cadastrarAgencia);
				janela.show(target);

			}
		};
		return acaoAlterarAgencia;
	}

	protected AjaxLink<Void> gerarPdf() {
		AjaxLink<Void> gerarPdf = new AjaxLink<Void>("gerarPdf") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				RelatorioExcel relatorio = new RelatorioExcel();

				final byte[] bytes = relatorio.GerarExcelInclusaoAgencia(servicoBanco.listar());

				final AJAXDownload download = new AJAXDownload("Inclusao de Agencias.xls") {
					private static final long serialVersionUID = 1L;

					@Override
					protected IResourceStream getResourceStream() {
						AbstractResourceStreamWriter streamWriter = new AbstractResourceStreamWriter() {
							private static final long serialVersionUID = 1L;

							@Override
							public void write(OutputStream output) throws IOException {
								output.write(bytes);

							}
						};
						return streamWriter;
					}
				};
				add(download);
				download.initiate(target);

			}
		};
		return gerarPdf;
	}

	private AjaxLink<Void> acaoImportar() {
		AjaxLink<Void> importar = new AjaxLink<Void>("importarAgencias") {
			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {
				janela.setInitialWidth(550);
				janela.setInitialHeight(270);
				janela.setResizable(false);
				ImportarAgencias importarAgencias = new ImportarAgencias(janela.getContentId()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void execultarFechar(AjaxRequestTarget target, boolean tecla,
							org.apache.wicket.markup.html.form.upload.FileUpload fileUpload) {
						super.execultarFechar(target, tecla, fileUpload);
						List<Agencia> listaAgencia = new ArrayList<>();
						Retorno retorno = new Retorno(true, null);
						if (tecla) {
							if (fileUpload != null) {
								RelatorioExcel relatorioExcel = new RelatorioExcel();

								listaAgencia = relatorioExcel.lerPlanilha(fileUpload, servicoBanco.listar());

								if (!listaAgencia.isEmpty()) {

									boolean sucesso = servicoAgencia.validarImportacaoDeAgencias(listaAgencia);
									if (sucesso == true) {
										retorno = servicoAgencia.importarAgencias(listaAgencia);
										if (retorno.isSucesso()) {
											feedbackPanel.success("Agencias incluidas com sucesso!");
											preencherListView();
											janela.close(target);
										} else {
											feedbackPanel = retorno.getMensagens(feedbackPanel);
										}

									} else { // mostra na tela os erros
										janela.close(target);
										abrirModalComErros(target, listaAgencia);
									}
								}
							} else {
								feedbackPanel.error("Selecione o arquivo para upload");
							}
						} else {
							janela.close(target);
						}
						target.add(containerListView);
					}

				};
				janela.setContent(importarAgencias);
				janela.show(target);
			}
		};
		return importar;
	}

	private void abrirModalComErros(AjaxRequestTarget target, List<Agencia> listaAgenciaErro) {
		janela.setInitialWidth(800);
		janela.setInitialHeight(400);
		janela.setResizable(false);
		ErrosImportacaoDeAgenciasPanel agenciasPanel = new ErrosImportacaoDeAgenciasPanel(janela.getContentId(),
				listaAgenciaErro);
		janela.setContent(agenciasPanel);
		janela.show(target);

	}

	private AjaxLink<Void> acaoNovaAgencia() {
		AjaxLink<Void> acaoNovaAgencia = new AjaxLink<Void>("novaAgencia") {

			private static final long serialVersionUID = 1L;

			@Override
			public void onClick(AjaxRequestTarget target) {

				janela.setMinimalWidth(500);
				janela.setInitialWidth(600);
				janela.setMinimalHeight(350);
				janela.setInitialHeight(400);
				CadastrarAgencia cadastrarAgencia = new CadastrarAgencia(janela.getContentId()) {
					private static final long serialVersionUID = 1L;

					@Override
					public void acaoSalvarCancelarAgencia(AjaxRequestTarget target, boolean tecla) {
						if (tecla) {
							feedbackPanel.success("Agencia incluida com sucesso!");
							preencherListView();
							target.add(containerListView);
						}
						janela.close(target);

						super.acaoSalvarCancelarAgencia(target, tecla);
					}
				};
				janela.setContent(cadastrarAgencia);
				janela.show(target);
			}

		};
		return acaoNovaAgencia;
	}

}
