package br.com.unika.paginas;

import java.util.Calendar;
import java.util.GregorianCalendar;

import org.apache.wicket.spring.injection.annot.SpringBean;

import br.com.unika.modelo.PermissaoDeAcesso;
import br.com.unika.modelo.Usuario;
import br.com.unika.servicos.ServicoPermissaoDeAcesso;
import br.com.unika.servicos.ServicoUsuario;
import br.com.unika.util.Retorno;

public class HomePage extends NavBar {
	private static final long serialVersionUID = 1L;
	
	@SpringBean(name="servicoUsuario")
	private ServicoUsuario servicoUsuario;
	
	public HomePage() {
		Usuario usuario = new Usuario();
		usuario.setNome("  ");
		usuario.setSobrenome("jjkj");
		usuario.setTelefone("(62)99115-3602");
		usuario.setCpf("700.794.811-16");
		usuario.setEmail("jeanbordori@gmail.com");
		Calendar c = new GregorianCalendar();
		
		c.set(Calendar.YEAR, 1996);
		c.set(Calendar.MONTH, 11);
		c.set(Calendar.DAY_OF_MONTH,25);
		usuario.setDataNascimento(c);
		usuario.setCep("75144-610");
		usuario.setEndereco("Rua Matadouro Industrial");
		usuario.setNumero("15");
		usuario.setComplemento("final da rua ");
		usuario.setBairro("Vila Fabril");
		usuario.setSexo(true);
		usuario.setLogin("");
		usuario.setSenha("");
		PermissaoDeAcesso permissaoDeAcesso = new PermissaoDeAcesso();
		permissaoDeAcesso.setIdPermissao(new Long(1));
		usuario.setPermissaoDeAcesso(permissaoDeAcesso);
		
		Retorno retorno = servicoUsuario.incluir(usuario);
		
	}

} 
