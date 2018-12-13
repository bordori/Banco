package br.com.unika.dao;

import java.io.Serializable;

import br.com.unika.interfaces.IControleDAO;
import br.com.unika.modelo.Conta;

public class ContaDAO extends GenericDAO<Conta, Long> implements IControleDAO<Conta, Long>,Serializable{

	private static final long serialVersionUID = 1L;

}
