package edu.jhu.jbeth.query;

import java.sql.SQLException;
import java.util.List;

import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Root;

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
import edu.jhu.jbeth.dao.CourseDao;
import edu.jhu.jbeth.dao.StudentDao;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.CoursesEntity;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.StudentEntity;
import edu.jhu.jbeth.util.Logged;
import edu.jhu.jbeth.util.TCPConnection;

public class CriteriaAPITest {

    private static final Logger logger = Logger.getLogger(CriteriaAPITest.class);

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

	CourseBO course = DataProvider.getCourseBO();
	studentDao.registerForCourse(studentEntity.getUserId(), course);
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

	CriteriaBuilder criteriaBuilder = studentDao.getCriteriaBuilder();
	CriteriaQuery<CoursesEntity> criteriaQuery = criteriaBuilder.createQuery(CoursesEntity.class);
	Root<StudentEntity> studentRoot = criteriaQuery.from(StudentEntity.class);

	String userId = studentEntity.getUserId();
	criteriaQuery.where(criteriaBuilder.equal(studentRoot.get("userId"), userId));
	Join<StudentEntity, CoursesEntity> join = studentRoot.join("courses");

	CriteriaQuery<CoursesEntity> cq = criteriaQuery.select(join);
        TypedQuery<CoursesEntity> query = studentDao.createQuery(cq);
        List<CoursesEntity> result = query.getResultList();
	Assert.assertTrue(!result.isEmpty());
	CoursesEntity course = result.get(0);
	Assert.assertNotNull(course);
	String actualCourseId = course.getCourseId();
	Assert.assertEquals(actualCourseId, course.getCourseId());
    }
}
