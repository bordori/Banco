package br.com.unika.interfaces;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Entity;

import com.googlecode.genericdao.search.Search;

import br.com.unika.util.Retorno;



public interface IControleDAO<Entidade,Id extends Serializable> {

	public Retorno salvarDAO(Entidade ent);
	public Retorno removerDAO(Entidade ent);
	public List<Entidade> listarDAO();
	public Retorno alterarDAO(Entidade ent);
	public Entidade procurarDAO(Id pk);
	public List<Entidade> searchDAO(Search search);
	public int countDAO(Search search);
}
