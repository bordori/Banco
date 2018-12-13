package br.com.unika.interfaces;

import java.io.Serializable;
import java.util.List;

import com.googlecode.genericdao.search.Search;

import br.com.unika.util.Retorno;

public interface IServico<Entidade,id extends Serializable>  {

	public Retorno incluir(Entidade ent);
	
	public Retorno alterar(Entidade ent);
	
	public Entidade procurar(Entidade ent);
	
	public List<Entidade>  listar();
	
	public Retorno remover(Entidade ent);
	
	public List<Entidade> search(Search search);

	
}
