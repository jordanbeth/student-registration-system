/**
 * 
 */
package edu.jhu.jbeth.dao.jpa;

import javax.ejb.Singleton;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;

import edu.jhu.jbeth.AppConstants;

/**
 * @author jordanbeth
 *
 */

public class JPAConnection {
    // Singleton
    private static JPAConnection INSTANCE;

    public static JPAConnection getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new JPAConnection();
	}

	return INSTANCE;
    }

    private final EntityManager em;
 
    private JPAConnection() {

	try {
	    //  Get a Connection to the database
	    EntityManagerFactory emf = Persistence.createEntityManagerFactory(AppConstants.SRS_PERSISTENCE_UNIT);
	    this.em = emf.createEntityManager();

	} catch (Exception e) {
	    e.printStackTrace();
	    throw new RuntimeException(e.getMessage());
	}
    } // end of DBConnection constructor

    public void begin() {
	this.em.getTransaction().begin();
    }

    public void commit() {
	this.em.getTransaction().commit();
    }

    public EntityManager getEntityManager() {
	return this.em;
    }

    public <T> T find(Class<T> clazz, String id) {
	return this.em.find(clazz, id);
    }

    public Query query(String qlString) {
	return this.em.createQuery(qlString);
    }

    public <T> TypedQuery<T> typedQuery(String qlString, Class<T> clazz) {
	return this.em.createQuery(qlString, clazz);
    }

    public <T> TypedQuery<T> createQueryAll(String tableName, Class<T> clazz) {
	return this.em.createQuery("from " + tableName, clazz);
    }

    public void persist(PersistantEntity entity) {
	this.em.persist(entity);
    }

    public <T> Query createNativeQuery(String query, Class<T> clazz) {
	return this.em.createNativeQuery(query, clazz);
    }

    public Query createNamedQuery(String name) {
	return this.em.createNamedQuery(name);
    }
}
