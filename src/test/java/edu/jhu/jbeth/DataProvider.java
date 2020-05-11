package edu.jhu.jbeth;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.StudentMapper;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.StudentEntity;

/**
 * 
 * @author jordanbeth
 *
 */
public class DataProvider {

    public static final String _605_784 = "605.784";
    public static final String TEST_USER_ID = "testtest";

    public static StudentEntity student1() {
	String userId = "test_user123";
	String password = "password";
	String firstName = "Jim";
	String lastName = "Jackson";

	String ssn = "111111111";
	String email = "jimjack@gmail.com";
	String address = "8 Beach Lane";
	StudentEntity studentEntity = StudentMapper.createEntity(userId, password, firstName, lastName, ssn, email, address);
	
	return studentEntity;
    }
    
    public static CourseBO getCourseBO() {
	return new CourseBO(_605_784, "Enterprise Computing with Java");
    }

}
