package edu.jhu.jbeth.jta;

import java.util.Properties;

import javax.ejb.EJBException;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import org.jboss.logging.Logger;

import edu.jhu.jbeth.DataProvider;
import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.StudentEntity;
import edu.jhu.jbeth.ejb.RemoteRegistrarCourseBean;

public class RegistrarCourseBeanClient {

    private static final Logger logger = Logger.getLogger(RegistrarCourseBeanClient.class);

    private static final String INITIAL_CONTEXT_FACTORY = "org.wildfly.naming.client.WildFlyInitialContextFactory";
    private static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";
    private static final String USERNAME = "mquser";
    private static final String PASSWORD = "mqpassword";
    private static final String EJB = "ejb:/StudentRegistrationSystem/RegistrarCourseBean!edu.jhu.jbeth.ejb.RemoteRegistrarCourseBean";
    private static final String URL_PKG_PREFIXES = "org.jboss.ejb.client.naming";

    public enum TransactionMode {
	SHORT, LONG;

	TransactionMode toggle() {
	    if (this == SHORT) {
		return LONG;
	    } else {
		return SHORT;
	    }
	}
    }

    private TransactionMode transactionMode;
    private final RemoteRegistrarCourseBean registrarCourseBean;
    private final int iterations;

    public RegistrarCourseBeanClient(int iterations, TransactionMode transactionMode) throws NamingException {
	this.iterations = iterations;
	this.transactionMode = transactionMode;
	this.registrarCourseBean = lookupRegistrarCourseBean();
    }

    private static RemoteRegistrarCourseBean lookupRegistrarCourseBean() throws NamingException {
	// Set up the namingContext for the JNDI lookup
	final Properties env = new Properties();
	env.put(Context.INITIAL_CONTEXT_FACTORY, INITIAL_CONTEXT_FACTORY);
	env.put(Context.PROVIDER_URL, PROVIDER_URL);
	env.put(Context.SECURITY_PRINCIPAL, USERNAME);
	env.put(Context.SECURITY_CREDENTIALS, PASSWORD);
	env.put(Context.URL_PKG_PREFIXES, URL_PKG_PREFIXES);
	Context namingContext = new InitialContext(env);

	return (RemoteRegistrarCourseBean) namingContext.lookup(EJB);
    }

    public final void register(String testUserId, CourseBO course) throws Exception {
	if (this.transactionMode == null) {
	    logger.error("Transaction mode is null. transactionMode=" + this.transactionMode);
	    return;
	}

	for (int i = 1; i <= this.iterations; i++) {

	    logger.info(String.format("--- Starting transaction %d of %d, mode = %s ---", i, this.iterations, this.transactionMode));

	    // Get number registered in DB before update
	    String courseId = course.getCourseId();
	    int numRegisteredBefore = this.registrarCourseBean.findNumRegistered(courseId);

	    logger.info(String.format("Number registered for course id [%s] before UPDATE = [%d] ", courseId, numRegisteredBefore));

	    logger.info(String.format("Updating course id [%s] in [%s MODE]", courseId, this.transactionMode));

	    switch (this.transactionMode) {
	    case SHORT: {
		boolean registerForCourse = this.registrarCourseBean.registerForCourse(testUserId, course);
		logger.info(String.format("Registration success = [%s]", registerForCourse));
		break;
	    }
	    case LONG: {
		try {
		    boolean registerForCourse = this.registrarCourseBean.registerForCourseLong(testUserId, course);
		    logger.info(String.format("Registration success = [%s]", registerForCourse));

		} catch (EJBException e) {
		    logger.info("* An EJBException was encountered *");
		} catch (Exception e) {
		    logger.info(String.format("* An Exception was encountered *"));
		}
		break;
	    }
	    default:
		throw new IllegalArgumentException("Unsupported transaction mode: " + this.transactionMode);
	    }

	    int numRegisteredAfter = this.registrarCourseBean.findNumRegistered(courseId);
	    logger.info(String.format("Number registered for course id [%s] after UPDATE = [%d] ", courseId, numRegisteredAfter));

	    this.transactionMode = this.transactionMode.toggle();
	    logger.info(String.format("Transaction complete. Switched mode to %s", this.transactionMode));
	}
    }

    public static void main(String[] args) throws Exception {
	if (args.length != 1) {
	    throw new IllegalStateException("usage SHORT | LONG");
	}
	TransactionMode transactionMode;
	try {
	    String transactionModeAsString = args[0];
	    logger.info("transactionModeAsString = " + transactionModeAsString);
	    transactionMode = TransactionMode.valueOf(transactionModeAsString);
	} catch (Exception ex) {
	    throw new IllegalStateException("usage SHORT | LONG");
	}

	int attempts = 4;
	RegistrarCourseBeanClient client = new RegistrarCourseBeanClient(attempts, transactionMode);

	client.register(DataProvider.TEST_USER_ID, DataProvider.getCourseBO());
    }
}
