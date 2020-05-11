/**
 * 
 */
package edu.jhu.jbeth.dao.jdbc;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Hashtable;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

/**
 * @author jordanbeth
 *
 */
public class JDBCConnection implements AutoCloseable {
    // Singleton
    private static JDBCConnection INSTANCE;

    public static JDBCConnection getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new JDBCConnection();
	}

	return INSTANCE;
    }

    private static final String JNDI_NAME = "java:jboss/datasources/H2_784_JNDI";
    private static final String WILDFLY_CONTEXT_FACTORY = "org.wildfly.naming.client.WildFlyInitialContextFactory";

    private final Hashtable<String, String> env = new Hashtable<>();
    private Connection con;
    private DataSource ds;

    private JDBCConnection() {
	this.env.put(Context.INITIAL_CONTEXT_FACTORY, WILDFLY_CONTEXT_FACTORY);
	initConnection();
    } // end of DBConnection constructor

    private void initConnection() {
	try {
	    //  Get a Connection to the database
	    Context context = new InitialContext(this.env);
	    this.ds = (DataSource) context.lookup(JNDI_NAME);

	} catch (Exception e) {
	    e.printStackTrace();
	    throw new RuntimeException(e.getMessage());
	}
    }

    public PreparedStatement prepareStatement(String query) throws SQLException {
	tryOpen();
	return this.con.prepareStatement(query, ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
    }

    public Statement createStatement() throws SQLException {
	tryOpen();
	return this.con.createStatement();
    }

    public JDBCConnection tryOpen() {
	try {
	    if (this.con == null || this.con.isClosed()) {
		this.con = this.ds.getConnection();
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new RuntimeException(e.getMessage());
	}

	return this;
    }

    public void close() {
	try {
	    if (!this.con.isClosed()) {
		//  Close the connection
		this.con.close();
	    }
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new RuntimeException(e.getMessage());
	}
    }
}
