package br.com.unika.dao;

import java.io.Serializable;

import br.com.unika.interfaces.IControleDAO;
import br.com.unika.modelo.Usuario;

public class UsuarioDAO extends GenericDAO<Usuario, Long> implements IControleDAO<Usuario, Long>,Serializable{

	private static final long serialVersionUID = 1L;

}
