package br.com.unika.dao;

import java.io.Serializable;

import org.hibernate.HibernateException;

import com.googlecode.genericdao.search.Search;

import br.com.unika.interfaces.IControleDAO;
import br.com.unika.modelo.Conta;
import br.com.unika.util.Retorno;

public class ContaDAO extends GenericDAO<Conta, Long> implements IControleDAO<Conta, Long>,Serializable{

	private static final long serialVersionUID = 1L;

	public Retorno transferir(Conta contaFavorecido, Conta conta) {
		Retorno retorno = new Retorno(true, null);

		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			session.merge(contaFavorecido);
			session.merge(conta);
			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			System.out.println("Erro no Dao.alterar: " + e.getMessage());
			retorno.setSucesso(false);
			retorno.addMensagem("Erro na Transferencia!");
			session.getTransaction().rollback();
		}
		return retorno;
	}

	
}
