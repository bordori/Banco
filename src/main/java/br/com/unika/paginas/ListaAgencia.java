package br.com.unika.paginas;

import java.beans.Transient;
import java.io.ByteArrayOutputStream;
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
import org.apache.wicket.markup.html.form.upload.FileUploadField;
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
import br.com.unika.util.NotificationPanel;
import br.com.unika.util.RelatorioExcel;
import br.com.unika.util.Retorno;

public class ListaAgencia extends NavBar implements Serializable{

	private static final long serialVersionUID = 1L;

	private ListView<Agencia> listaAgencia;
	private ModalWindow janela;
	private NotificationPanel notificationPanel;
	private WebMarkupContainer containerListView;
	private TextField<String> nome, numero;
	private Agencia agenciaProcurar;
	private Form<Agencia> formFiltro;
	private List<Agencia> agenciasList;

	@SpringBean(name = "servicoAgencia")
	private ServicoAgencia servicoAgencia;

	@SpringBean(name = "servicoBanco")
	private ServicoBanco servicoBanco;

	
	public ListaAgencia() {
		verificarPermissaoBanco();
		preencherListView();
		add(containerListView());
		add(initModal());
		add(formFiltro());

	}

	private void preencherListView() {
		agenciasList = servicoAgencia.listar();

	}

	private WebMarkupContainer containerListView() {
		containerListView = new WebMarkupContainer("containerListView");
		containerListView.setOutputMarkupId(true);
		notificationPanel = new NotificationPanel("feedBack");
		notificationPanel.setOutputMarkupId(true);
		containerListView.add(notificationPanel);
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
		numero = new TextField<>("numero");
		numero.setOutputMarkupId(true);
		return numero;
	}

	private TextField<String> filtroNome() {
		nome = new TextField<>("nome");
		return nome;
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
									notificationPanel.mensagem("Agencia Deletada com Sucesso!", "sucesso");
									janela.close(target);
									preencherListView();
									target.add(containerListView);
								} else {
									notificationPanel.mensagem(retorno.getRetorno(), "erro");
									janela.close(target);
									target.add(notificationPanel);
								}
							} else {
								notificationPanel.mensagem("Senha Incorreta!", "erro");
								target.add(notificationPanel);
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
					protected void acaoSalvarCancelarAgencia(AjaxRequestTarget target,boolean tecla) {
						if (tecla) {
							notificationPanel.mensagem("Agencia Alterada Com Sucesso!", "sucesso");
							preencherListView();
							target.add(containerListView);
						}
						janela.close(target);
						
						super.acaoSalvarCancelarAgencia(target,tecla);
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
	
	private AjaxLink<Void> acaoImportar(){
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
					public void execultarFechar(AjaxRequestTarget target, boolean tecla, org.apache.wicket.markup.html.form.upload.FileUpload fileUpload) {
						super.execultarFechar(target, tecla, fileUpload);
						List<Agencia> listaAgencia = new ArrayList<>();
						Retorno retorno = new Retorno(true, null);
						if (tecla) {
							if (fileUpload != null) {
								RelatorioExcel relatorioExcel = new RelatorioExcel();
								if (fileUpload.getContentType().equals("application/vnd.ms-excel")) {//.xls
									listaAgencia = relatorioExcel.lerPlanilha(fileUpload,servicoBanco.listar(), true);
								}else if(fileUpload.getContentType().equals("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet")){//.xlsx
									listaAgencia = relatorioExcel.lerPlanilha(fileUpload,servicoBanco.listar(), false);
								}
								
								if (!listaAgencia.isEmpty()) {
									
									boolean sucesso = servicoAgencia.validarImportacaoDeAgencias(listaAgencia);
									if (sucesso == true ) {
										retorno = servicoAgencia.importarAgencias(listaAgencia);
										if (retorno.isSucesso()) {
											notificationPanel.mensagem("Agencias Incluidas com sucesso!", "sucesso");
											preencherListView();
											janela.close(target);
										}else {
											notificationPanel.mensagem(retorno.getRetorno(), "erro");
										}
										
									}else { //mostra na tela os erros 
										janela.close(target);
										abrirModalComErros(target,listaAgencia);
									}
								}
							}else {
								notificationPanel.mensagem("Selecione Arquivo para Upload", "erro");
							}
						}else {
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
		ErrosImportacaoDeAgenciasPanel agenciasPanel = new ErrosImportacaoDeAgenciasPanel(janela.getContentId(), listaAgenciaErro);
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
					protected void acaoSalvarCancelarAgencia(AjaxRequestTarget target,boolean tecla) {
						if (tecla) {
							notificationPanel.mensagem("Agencia Incluida Com Sucesso!", "sucesso");
							preencherListView();
							target.add(containerListView);
						}
						janela.close(target);
	
						super.acaoSalvarCancelarAgencia(target,tecla);
					}
				};
				janela.setContent(cadastrarAgencia);
				janela.show(target);
			}

		};
		return acaoNovaAgencia;
	}

}
