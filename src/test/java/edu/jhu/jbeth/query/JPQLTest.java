package edu.jhu.jbeth.query;

import java.sql.SQLException;
import java.util.List;
import java.util.Set;

import org.jboss.logging.Logger;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import edu.jhu.jbeth.DataProvider;
import edu.jhu.jbeth.TestJPAStudentDao;
import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.bo.StudentBO;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.CoursesEntity;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.StudentEntity;
import edu.jhu.jbeth.util.Logged;
import edu.jhu.jbeth.util.TCPConnection;

public class JPQLTest {
    private static final Logger logger = Logger.getLogger(JPQLTest.class);

    private static final String PERSISTANCE_UNIT_NAME = "StudentRegistrationSystem_TablePerConcreteClass";

    private static TestJPAStudentDao studentDao;

    @BeforeClass
    public static void connect() throws SQLException {
	// start the TCP Server
	TCPConnection.start();
	studentDao = new TestJPAStudentDao(PERSISTANCE_UNIT_NAME);
    }

    @AfterClass
    public static void disconnect() {
	// stop the TCP Server
	TCPConnection.stop();
    }

    @Logged
    @Before
    public void beforeTest() {
	studentDao.beginTestTransaction();
	StudentEntity studentEntity = DataProvider.student1();
	String userId = studentEntity.getUserId();
	String password = studentEntity.getPassword();
	String firstName = studentEntity.getFirstName();
	String lastName = studentEntity.getLastName();
	String ssn = studentEntity.getSsn();
	String email = studentEntity.getEmail();
	String address = studentEntity.getAddress();
	studentDao.registerStudent(userId, password, firstName, lastName, ssn, email, address);
    }

    @After
    public void afterTest() {
	// do not make changes to the underlying db
	studentDao.rollback();
    }

    @Logged
    @Test
    public void testFindCoursesByLastName() {
	StudentEntity studentEntity = DataProvider.student1();

	CourseBO course = DataProvider.getCourseBO();
	logger.info("registered for course... " + course);
	studentDao.registerForCourse(studentEntity.getUserId(), course);
	Set<CoursesEntity> courses2 = studentEntity.getCourses();
	logger.info(courses2);

	String lastName = studentEntity.getLastName();
	logger.info("findByLastName " + lastName);
	List<StudentBO> result = studentDao.findByLastName(lastName);
	logger.info("findByLastName result = " + result);
	Assert.assertTrue(!result.isEmpty());
	StudentBO studentBO = result.get(0);
	Set<CourseBO> courses = studentBO.getCourses();
	Assert.assertTrue(!courses.isEmpty());
	CourseBO first = courses.iterator().next();
	String actualCourseId = first.getCourseId();

	Assert.assertEquals(actualCourseId, course.getCourseId());
    }
}
