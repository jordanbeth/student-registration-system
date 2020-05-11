package edu.jhu.jbeth.strategy;
import java.sql.SQLException;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Ignore;
import org.junit.Test;

import edu.jhu.jbeth.DataProvider;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.StudentEntity;
import edu.jhu.jbeth.util.TCPConnection;

public class TablePerClassStrategyTest {

    private static final Logger logger = Logger.getLogger(TablePerClassStrategyTest.class);

    private static final String PERSISTANCE_UNIT_NAME = "StudentRegistrationSystem_TablePerConcreteClass";
    private static EntityManager em;
    private static EntityTransaction transaction;

    @BeforeClass
    public static void connect() throws SQLException {
	// start the TCP Server
	TCPConnection.start();

	EntityManagerFactory emf = Persistence.createEntityManagerFactory(PERSISTANCE_UNIT_NAME);
	em = emf.createEntityManager();
    }

    @AfterClass
    public static void disconnect() {
	// stop the TCP Server
	TCPConnection.stop();
    }

    @Before
    public void beforeTest() {
	transaction = em.getTransaction();
	transaction.begin();
    }

    @After
    public void afterTest() {
	// do not make changes to the underlying db
	transaction.rollback();
    }

    @Test
    @Ignore
    public void testCreate() {
	logger.info("Start testCreate().");
	StudentEntity studentEntity = DataProvider.student1();

	em.persist(studentEntity);

	boolean result = em.contains(studentEntity);
	logger.info("student entity persisted = [" + result + "]");
	Assert.assertTrue(result);

	logger.info("removing student entity...");
	em.remove(studentEntity);

	boolean resultRemoved = em.contains(studentEntity);
	logger.info("student entity is present = [" + resultRemoved + "]");
	Assert.assertFalse(resultRemoved);

	logger.info("End testCreate().");
    }

    @Test
    @Ignore
    public void testUpdate() {
	logger.info("Start testUpdate().");
	StudentEntity studentEntity = DataProvider.student1();

	em.persist(studentEntity);

	boolean result = em.contains(studentEntity);
	logger.info("student entity persisted = [" + result + "]");
	Assert.assertTrue(result);

	// update
	final String email = "bogusEmail@gmail.com";
	studentEntity.setEmail(email);
	em.persist(studentEntity);
	StudentEntity find = em.find(StudentEntity.class, studentEntity.getUserId());

	Assert.assertTrue(find != null);
	Assert.assertTrue(email.equals(find.getEmail()));
	logger.info("student entity updated... " + studentEntity);

	logger.info("removing student entity..." + studentEntity);
	em.remove(studentEntity);

	boolean resultRemoved = em.contains(studentEntity);
	logger.info("student entity is present = [" + resultRemoved + "]");
	Assert.assertFalse(resultRemoved);
	logger.info("End testUpdate().");

    }
}
