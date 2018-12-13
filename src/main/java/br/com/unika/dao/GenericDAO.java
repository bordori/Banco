package br.com.unika.dao;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.NoResultException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Search;

import br.com.unika.util.Retorno;




public class GenericDAO<Entidade, Id extends Serializable> extends GenericDAOImpl<Entidade, Id> {
	public Session session;
	
	
	

	
	@Override
	public void setSessionFactory(SessionFactory sessionFactory) {
	// TODO Auto-generated method stub
	super.setSessionFactory(sessionFactory);
	}
	
	
	
	
	public Session getSession() {
		return session;
	}

	public void setSession(Session session) {
		this.session = session;
	}

	
	
	
	

	public Retorno salvarDAO(Entidade ent) {
		Retorno retorno = new Retorno(true, null);
		try {
		
			session = getSessionFactory().openSession();
			session.beginTransaction();
			this.save(ent);

			session.getTransaction().commit();
			session.close();
		} catch (Exception e) {
			System.out.println("Erro no Dao.salvar: " + e.getMessage());
			retorno = catchRetorno(retorno, "Erro no Dao.salvar: " + e.getMessage());
		}
		return retorno;
	}

	private Retorno catchRetorno(Retorno retorno, String msg) {
		retorno.setSucesso(false);
		retorno.addMensagem(msg);
		return retorno;
	}

	public Retorno removerDAO(Entidade ent) {
		Retorno retorno = new Retorno(true, null);
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			this.remove(ent);
			session.getTransaction().commit();
			session.close();

		} catch (IllegalArgumentException e) {
			System.out.println("Defina a tabela para Dao.remover: " + e.getMessage());
			retorno = catchRetorno(retorno, "Defina a tabela para Dao.remover: " + e.getMessage());
		} catch (NullPointerException e) {
			System.out.println("Defina a tabela para Dao.remover: " + e.getMessage());
			retorno = catchRetorno(retorno, "Defina a tabela para Dao.remover: " + e.getMessage());
		} catch (HibernateException e) {
			System.out.println("Nao foi encontrado na tabela " + e.getMessage());
			retorno = catchRetorno(retorno, "Nao foi encontrado na tabela ");
		} catch (Exception e) {
			System.out.println("Erro no Dao.remover: " + e.getMessage());
			retorno = catchRetorno(retorno, "Erro no Dao.remover: " + e.getMessage());
		}
		return retorno;
	}

	public List<Entidade> listarDAO( ) {
		ArrayList<Entidade> lista = new ArrayList<>();
		
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			lista =  (ArrayList<Entidade>) this.findAll();
			session.close();
			
		} catch (NullPointerException e) {
			System.out.println("Defina a tabela para usar Dao.listar");
			return null;
		}
		return lista;
	}

	public Retorno alterarDAO(Entidade ent) {

		Retorno retorno = new Retorno(true, null);

		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			session.merge(ent);
			session.getTransaction().commit();
			session.close();
		} catch (HibernateException e) {
			System.out.println("Nao foi encontrado na tabela " + e.getMessage());
			retorno = catchRetorno(retorno, "Nao foi encontrado na tabela ");
		} catch (NullPointerException e) {
			System.out.println(
					"Os campos obrigatorios nao estao preenchidos: " + e.getMessage() + "" + " ou tabela e nula.");
			retorno = catchRetorno(retorno,
					"Os campos obrigatorios nao estao preenchidos: " + e.getMessage() + "" + " ou tabela e nula.");
		} catch (Exception e) {
			System.out.println("Erro no Dao.alterar: " + e.getMessage());
			retorno = catchRetorno(retorno, "Erro no Dao.alterar: " + e.getMessage());
		}
		return retorno;
	}

	public Entidade procurarDAO( Id pk) {
		Entidade ent = null;
		try {
			session = getSessionFactory().openSession();
			session.beginTransaction();
			ent =  this.find(pk);
			session.getTransaction().commit();
			session.close();
		} catch (HibernateException e) {
			System.out.println("Nao foi encontrado na tabela" + e.getMessage());
		} catch (NoResultException e) {
			System.out.println("Nao foi encontrado nenhum dado "+e.getMessage());

		}
		return ent;

	}
	
	public List<Entidade> searchDAO(Search search) {
		
		session = getSessionFactory().openSession();
		session.beginTransaction();
		
		ArrayList<Entidade> lista = new ArrayList<>();
		lista =  (ArrayList<Entidade>) this.search(search);
		
		
		session.close();
		return lista;
	}
	
	
}
