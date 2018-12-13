package br.com.unika.dao;

import java.io.Serializable;

import org.springframework.stereotype.Repository;

import br.com.unika.interfaces.IControleDAO;
import br.com.unika.modelo.PermissaoDeAcesso;

@Repository
public class PermissaoDeAcessoDAO extends GenericDAO<PermissaoDeAcesso, Long> implements IControleDAO<PermissaoDeAcesso, Long>,Serializable {


	private static final long serialVersionUID = 1L;

	
	

}
