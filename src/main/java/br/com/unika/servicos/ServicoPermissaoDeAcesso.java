package br.com.unika.servicos;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.wicket.model.IModel;
import org.apache.wicket.model.Model;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.googlecode.genericdao.search.Search;

import br.com.unika.dao.PermissaoDeAcessoDAO;
import br.com.unika.interfaces.IServico;
import br.com.unika.modelo.PermissaoDeAcesso;
import br.com.unika.modelo.Usuario;
import br.com.unika.util.Retorno;
import br.com.unika.util.Validacao;




@Service
public class ServicoPermissaoDeAcesso implements IServico<PermissaoDeAcesso,Long>, Serializable {

	private static final long serialVersionUID = 1L;
	
	@SpringBean(name="permissaoDeAcessoDAO")
	PermissaoDeAcessoDAO permissaoDeAcessoDAO;
	

	

	@Override
	public Retorno incluir(PermissaoDeAcesso permissao) {

		Retorno retorno = new Retorno(true, null);
		permissao = (PermissaoDeAcesso) Validacao.retiraEspacoDesnecessarios(permissao);
		retorno = validacaoDeNegocio(permissao);
		
		if (!retorno.isSucesso()) {
			return retorno;
		} 
	
		retorno = permissaoDeAcessoDAO.salvarDAO(permissao);
	
		return retorno;
	}

	@Override
	public Retorno alterar(PermissaoDeAcesso permissao) {

		Retorno retorno = new Retorno(true, null);
		permissao = (PermissaoDeAcesso) Validacao.retiraEspacoDesnecessarios(permissao);
		retorno = validacaoDeNegocio(permissao);
		
		if (!retorno.isSucesso()) {
			return retorno;
		} 
		
		retorno = permissaoDeAcessoDAO.alterarDAO(permissao);

		return retorno;

	}

	@Override
	public PermissaoDeAcesso procurar(PermissaoDeAcesso permissao) {
		if (permissao != null && permissao.getIdPermissao() != null) {
			
			return permissaoDeAcessoDAO.procurarDAO(permissao.getIdPermissao());
			
		} 
		return null;
	}

	@Override
	public List<PermissaoDeAcesso> listar() {
		return permissaoDeAcessoDAO.listarDAO();
		

	}
	
	

	@Override
	public Retorno remover(PermissaoDeAcesso permissao) {
		
		Retorno retorno = new Retorno(true, null);
	
		
		if (permissao != null && permissao.getIdPermissao() != null) {
			if (permissao.getUsuarios() != null && !permissao.getUsuarios().isEmpty()) {
				System.out.println("A permissao não deve ter nenhum usuario vinculado");
				retorno.setSucesso(false);
				retorno.addMensagem("A permissao não deve ter nenhum usuario vinculado");
				return retorno;
			} else {
				
				retorno = permissaoDeAcessoDAO.removerDAO(permissao);
				
				
			}

		} else {
			System.out.println("Nenhuma PK inserida ou com o valor null");
			retorno.setSucesso(false);
			retorno.addMensagem("Nenhuma PK inserida ou com o valor null");
		}
		return retorno;
	}

	@Override
	public List<PermissaoDeAcesso> search(Search search) {
		ArrayList<PermissaoDeAcesso> lista = new ArrayList<>();
		lista = (ArrayList<PermissaoDeAcesso>) permissaoDeAcessoDAO.searchDAO(search);
		
		return lista;
	}
	
	
	@Override
	public int count(Search search) {
		return permissaoDeAcessoDAO.countDAO(search);
	}
	
	
	
	private Retorno validacaoDeNegocio(PermissaoDeAcesso permissao) {
		Retorno retorno = new Retorno(true,null);
		
		
		if (permissao.getDescricao() == null || permissao.getDescricao().length() < 3 || permissao.getDescricao().length() > 15) {
			retorno.setSucesso(false);
			retorno.addMensagem("Descrição Deve Ter Entre 3 e 15 Digitos!");
		}
		
		if(permissao.getAlterarBanco() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Permissão de Alterar Banco Deve Ser Preenchida!");
		}
		
		if(permissao.getAlterarConta() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Permissão de Alterar Contas Deve Ser Preenchida!");
		}
		
		if(permissao.getAlterarPermissoes() == null) {
			retorno.setSucesso(false);
			retorno.addMensagem("Permissão de Alterar Permissôes Deve Ser Preenchida!");
		}
		
		return retorno;
	}

	
	public void setPermissaoDeAcessoDAO(PermissaoDeAcessoDAO permissaoDeAcessoDAO) {
		this.permissaoDeAcessoDAO = permissaoDeAcessoDAO;
	}

	

}
