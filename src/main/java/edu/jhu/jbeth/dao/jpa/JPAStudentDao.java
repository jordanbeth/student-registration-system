/**
 * 
 */
package edu.jhu.jbeth.dao.jpa;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.Query;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.bo.StudentBO;
import edu.jhu.jbeth.dao.StudentDao;
import edu.jhu.jbeth.dao.StudentMapper;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.CoursesEntity;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.StudentEntity;

/**
 * @author jordanbeth
 *
 */
public class JPAStudentDao implements StudentDao {
    private static final Logger logger = Logger.getLogger(JPAStudentDao.class.getSimpleName());
    // Singleton
    private static JPAStudentDao INSTANCE;

    public static JPAStudentDao getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new JPAStudentDao();
	}

	return INSTANCE;
    }

    public static final String FIND_BY_LAST_NAME = "StudentEntity.findByLastName";
    private static final String SELECT_STUDENT_QUERY = "SELECT * FROM STUDENT WHERE USER_ID = :USER_ID AND PASSWORD = :PASSWORD";

    private final JPAConnection conn = JPAConnection.getInstance();

    private JPAStudentDao() {

    }

    @Override
    public boolean registerStudent(String userId, String password, String firstName, String lastName, String ssn, String email, String address) {

	StudentEntity studentEntity = StudentMapper.createEntity(userId, password, firstName, lastName, ssn, email, address);

	this.conn.begin();
	this.conn.persist(studentEntity);
	this.conn.commit();

	if (this.conn.getEntityManager().contains(studentEntity)) {
	    return true;
	} else {
	    return false;
	}

    }

    @Override
    public StudentBO logon(String userId, String password) {
	Query query = this.conn.createNativeQuery(SELECT_STUDENT_QUERY, StudentEntity.class);
	query.setParameter(StudentDao.USER_ID, userId);
	query.setParameter(StudentDao.PASSWORD, password);

	StudentBO studentBean = null;
	try {
	    StudentEntity studentEntity = (StudentEntity) query.getSingleResult();
	    studentBean = StudentMapper.mapToBO(studentEntity);
	} catch (Exception ex) {
	    logger.info("No student found with user id and password.");
	}

	return studentBean;
    }

    @Override
    public List<StudentBO> findByLastName(String lastName) {
	Query query = this.conn.createNamedQuery(FIND_BY_LAST_NAME);
	query.setParameter(1, lastName);

	List<StudentBO> studentBOs = new ArrayList<>();

	try {
	    List<StudentEntity> results = (List<StudentEntity>) query.getResultList();
	    for (StudentEntity studentEntity : results) {
		StudentBO studentBO = StudentMapper.mapToBO(studentEntity);
		logger.info("Found student by last name: [ " + studentBO + "]");
		studentBOs.add(studentBO);
	    }
	} catch (Exception ex) {
	    logger.info("No student found with last name = [" + lastName + "]");
	}

	return studentBOs;
    }

    @Override
    public boolean registerForCourse(String userId, CourseBO course) {
	StudentEntity studentEntity = this.conn.find(StudentEntity.class, userId);

	if (studentEntity == null) {
	    return false;
	}
	String courseId = course.getCourseId();

	CoursesEntity coursesEntity = this.conn.find(CoursesEntity.class, courseId);
	if (coursesEntity == null) {
	    return false;
	}

	Set<CoursesEntity> courses = studentEntity.getCourses();
	
	boolean contains = courses.contains(coursesEntity);
	if (contains) {
	    return false;
	}
	
	courses.add(coursesEntity);

	this.conn.persist(studentEntity);
	this.conn.persist(coursesEntity);
	return true;
    }

}
