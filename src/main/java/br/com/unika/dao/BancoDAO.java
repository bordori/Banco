package br.com.unika.dao;

import java.io.Serializable;

import br.com.unika.interfaces.IControleDAO;
import br.com.unika.modelo.Banco;

public class BancoDAO extends GenericDAO<Banco, Long> implements IControleDAO<Banco, Long>,Serializable{

	
	private static final long serialVersionUID = 1L;

}
