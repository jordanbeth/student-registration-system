package edu.jhu.jbeth.dao;

import java.util.List;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.bo.StudentBO;

public interface StudentDao {
	static final String STUDENT = "STUDENT";
	static final String ADDRESS = "ADDRESS";
	static final String EMAIL = "EMAIL";
	static final String LAST_NAME = "LAST_NAME";
	static final String FIRST_NAME = "FIRST_NAME";
	static final String PASSWORD = "PASSWORD";
	static final String USER_ID = "USER_ID";
	static final String SSN = "SSN";
	static final String LEVEL = "LEVEL";
	
	boolean registerStudent(String userId, String password, String firstName, String lastName, String ssn, String email,
			String address);

	StudentBO logon(String userId, String password);
	
	List<StudentBO> findByLastName(String lastName);

	boolean registerForCourse(String userId, CourseBO course);

}
