package br.com.unika.dao;

import java.io.Serializable;

import br.com.unika.interfaces.IControleDAO;
import br.com.unika.modelo.Movimentacao;

public class MovimentacaoDAO extends GenericDAO<Movimentacao, Long> implements IControleDAO<Movimentacao, Long>,Serializable {

	private static final long serialVersionUID = 1L;

}
