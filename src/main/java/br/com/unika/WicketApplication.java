package br.com.unika;

import org.apache.wicket.markup.html.WebPage;
import org.apache.wicket.protocol.http.WebApplication;
import org.apache.wicket.spring.injection.annot.SpringComponentInjector;

import br.com.unika.paginas.ClienteConta;
import br.com.unika.paginas.HomePage;
import br.com.unika.paginas.ListaAgencia;
import br.com.unika.paginas.ListaBancos;
import br.com.unika.paginas.ListaClientes;
import br.com.unika.paginas.ListaContas;
import br.com.unika.paginas.ListaContatos;
import br.com.unika.paginas.Menu;


/**
 * Application object for your web application.
 * If you want to run this application without deploying, run the Start class.
 * 
 * @see br.com.unika.Start#main(String[])
 */
public class WicketApplication extends WebApplication
{
	/**
	 * @see org.apache.wicket.Application#getHomePage()
	 */
	@Override
	public Class<? extends WebPage> getHomePage()
	{
		return HomePage.class;
	}

	/**
	 * @see org.apache.wicket.Application#init()
	 */
	@Override
	public void init()
	{
		super.init();
		
		mountPage("menu", Menu.class);
		mountPage("menu/gerenciarbancos/bancos", ListaBancos.class);
		mountPage("menu/gerenciarbancos/agencias", ListaAgencia.class);
		mountPage("menu/gerenciarcliente/contas", ListaContas.class);
		mountPage("menu/gerenciarcliente/clientes", ListaClientes.class);
		mountPage("menu/minhaconta/contas", ClienteConta.class);
		mountPage("menu/minhaconta/contatos", ListaContatos.class);

		
		getComponentInstantiationListeners().add(new SpringComponentInjector(this));
		// add your configuration here
	}
}
