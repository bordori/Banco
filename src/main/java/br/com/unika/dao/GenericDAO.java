package br.com.unika.dao;

import java.io.Serializable;
import java.util.ArrayList;

import javax.persistence.NoResultException;

import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.springframework.stereotype.Repository;

import com.googlecode.genericdao.dao.hibernate.GenericDAOImpl;
import com.googlecode.genericdao.search.Search;

import br.com.unika.util.Retorno;




public class GenericDAO<Entidade, Id extends Serializable> extends GenericDAOImpl<Entidade, Id> {
	protected Session session;
	private SessionFactory sessionFactory = HibernateUtil.getSessionFactory();
	//private SessionFactory sessionFactory;
	

	

	public void setSession(Session session) {
		this.session = session;
		
	}

	public Session getSession() {
		return session;
	}
	
	public Retorno salvarDAO(Entidade ent) {
		Retorno retorno = new Retorno(true, null);
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			this.setSession(session);
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
			session = sessionFactory.openSession();
			session.beginTransaction();
			this.setSession(session);
			session.delete(ent);
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

	public Retorno listarDAO( ) {
		ArrayList<?> lista = new ArrayList<>();
		Retorno retorno = new Retorno(true, null);
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			this.setSessionFactory(sessionFactory);
			//this.setSessionFactory(HibernateUtil.getSessionFactory());
			//lista = (ArrayList<?>) session.createQuery("SELECT " + tab.nomeTabela() + " from " + tab.getClass().getName() + " " + tab.nomeTabela()).list();
			lista = (ArrayList<?>) this.findAll();
			session.close();
			retorno.setLista(lista);
			return retorno;
		} catch (NullPointerException e) {
			System.out.println("Defina a tabela para usar Dao.listar");
			retorno = catchRetorno(retorno, "Defina a tabela para usar Dao.listar");
		}
		return retorno;
	}

	public Retorno alterarDAO(Entidade ent) {

		Retorno retorno = new Retorno(true, null);

		try {
			session = sessionFactory.openSession();
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

	public Retorno procurarDAO(Entidade ent,Id pk) {
		Retorno retorno = new Retorno(true, null);
		ArrayList<?> tabela = new ArrayList<>();
		try {
			session = sessionFactory.openSession();
			session.beginTransaction();
			this.setSession(session);
			/*tabela = (ArrayList<?>) session
					.createQuery("SELECT " + ent.nomeTabela() + "" + " from " + ent.getClass().getName() + " "
							+ ent.nomeTabela() + " WHERE " + ent.nomePk() + " = " + ent.getPkValor())
					.list();*/
			this.find(pk);
			session.getTransaction().commit();
			session.close();
			retorno.setLista(tabela);
			return retorno;
		} catch (HibernateException e) {
			System.out.println("Nao foi encontrado na tabela" + e.getMessage());
			retorno = catchRetorno(retorno, "Nao foi encontrado na tabela " + e.getMessage());
		} catch (NoResultException e) {
			System.out.println("Nao foi encontrado nenhum dado "+e.getMessage());
			retorno = catchRetorno(retorno, "Nao foi encontrado nenhum dado ");
		}
		return retorno;

	}
	
	public Retorno searchDAO(Search search) {
		Retorno retorno = new Retorno(true, null);
		
		session = sessionFactory.openSession();
		session.beginTransaction();
		this.setSessionFactory(sessionFactory);
		
		ArrayList<?> lista = new ArrayList<>();
		lista = (ArrayList<?>) this.search(search);
		retorno.setLista(lista);
		
		session.close();
		return retorno;
	}
	
	
}
