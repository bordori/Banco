package br.com.unika.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.spring.injection.annot.SpringBean;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.UsuarioDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.Usuario;
import br.com.unika.util.Retorno;

public class ServicoUsuario implements IServico<Usuario, Long>,Serializable{

	private static final long serialVersionUID = 1L;

	@SpringBean(name="usuarioDAO")
	private UsuarioDAO usuarioDAO;
	

	

	@Override
	public Retorno incluir(Usuario usuario) {

		Retorno retorno = new Retorno(true, null);
		
		
		if (!retorno.isSucesso()) {
			return retorno;
		} 
	
		retorno = usuarioDAO.salvarDAO(usuario);
	
		return retorno;
	}

	@Override
	public Retorno alterar(Usuario usuario) {

		Retorno retorno = new Retorno(true, null);
		
		
		if (!retorno.isSucesso()) {
			return retorno;
		} 
		
		retorno = usuarioDAO.alterarDAO(usuario);

		return retorno;

	}

	@Override
	public Usuario procurar(Usuario usuario) {
		if (usuario != null && usuario.getIdUsuario() != null) {
			
			return usuarioDAO.procurarDAO(usuario.getIdUsuario());
			
		} 
		return null;
	}

	@Override
	public List<Usuario> listar() {
		return usuarioDAO.listarDAO();
		

	}

	@Override
	public Retorno remover(Usuario usuario) {
		
		Retorno retorno = new Retorno(true, null);
	
		
		if (usuario != null && usuario.getIdUsuario() != null) {
				
				retorno = usuarioDAO.removerDAO(usuario);
			
		} else {
			System.out.println("Nenhuma PK inserida ou com o valor null");
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhuma PK inserida ou com o valor null");
		}
		return retorno;
	}

	@Override
	public List<Usuario> search(Search search) {
		ArrayList<Usuario> lista = new ArrayList<>();
		lista = (ArrayList<Usuario>) usuarioDAO.searchDAO(search);
		
		return lista;
	}
	
	public void setUsuarioDAO(UsuarioDAO usuarioDAO) {
		this.usuarioDAO = usuarioDAO;
	}
}
