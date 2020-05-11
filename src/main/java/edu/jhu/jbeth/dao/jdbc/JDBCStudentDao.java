package edu.jhu.jbeth.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.bo.StudentBO;
import edu.jhu.jbeth.dao.StudentDao;

/**
 * 
 * @author jordanbeth
 *
 */
public class JDBCStudentDao implements StudentDao {
    // Singleton
    private static JDBCStudentDao INSTANCE;

    public static JDBCStudentDao getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new JDBCStudentDao();
	}

	return INSTANCE;
    }

    private final PreparedStatement selectUserCredentialsStmt;
    private final PreparedStatement insertStudentStmt;

    private JDBCStudentDao() {

	try {
	    this.selectUserCredentialsStmt = JDBCConnection.getInstance().prepareStatement("SELECT * FROM STUDENT WHERE USER_ID = ? AND PASSWORD = ?");

	    this.insertStudentStmt = JDBCConnection.getInstance().prepareStatement("INSERT INTO STUDENT VALUES (?, ?, ?, ?, ?, ?, ?)");
	} catch (SQLException e) {
	    e.printStackTrace();
	    throw new RuntimeException(e.getMessage());
	}

    }

    public StudentBO logon(String userId, String password) {
	StudentBO userBean = null;
	try (JDBCConnection conn = JDBCConnection.getInstance().tryOpen()) {

	    this.selectUserCredentialsStmt.setString(1, userId);
	    this.selectUserCredentialsStmt.setString(2, password);
	    ResultSet resultSet = this.selectUserCredentialsStmt.executeQuery();
	    this.selectUserCredentialsStmt.clearParameters();

	    if (resultSet.first()) {
		String firstName = resultSet.getString(FIRST_NAME);
		String lastName = resultSet.getString(LAST_NAME);
		String email = resultSet.getString(EMAIL);
		String address = resultSet.getString(ADDRESS);

		userBean = new StudentBO();
		userBean.setFirstName(firstName);
		userBean.setLastName(lastName);
		userBean.setEmail(email);
		userBean.setAddress(address);
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	}

	return userBean;
    }

    public boolean registerStudent(String userId, String password, String firstName, String lastName, String ssn, String email, String address) {

	boolean success = false;
	try (JDBCConnection conn = JDBCConnection.getInstance().tryOpen()) {

	    this.insertStudentStmt.setString(1, userId);
	    this.insertStudentStmt.setString(2, password);
	    this.insertStudentStmt.setString(3, firstName);
	    this.insertStudentStmt.setString(4, lastName);
	    this.insertStudentStmt.setString(5, ssn);
	    this.insertStudentStmt.setString(6, email);
	    this.insertStudentStmt.setString(7, address);

	    int executeUpdate = this.insertStudentStmt.executeUpdate();
	    if (executeUpdate > 0) {
		success = true;
	    }

	} catch (SQLException e) {
	    e.printStackTrace();
	}
	return success;
    }

    @Override
    public List<StudentBO> findByLastName(String lastName) {
	// TODO: implement
	throw new IllegalStateException("Method not yet implemented...");
    }

    @Override
    public boolean registerForCourse(String userId, CourseBO course) {
	// TODO: implement
	throw new IllegalStateException("Method not yet implemented...");
    }

}
