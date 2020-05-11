/**
 * 
 */
package edu.jhu.jbeth;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.persistence.Query;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;

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
public class TestJPAStudentDao implements StudentDao {
    private static final Logger logger = Logger.getLogger(TestJPAStudentDao.class.getSimpleName());

    public static final String FIND_BY_LAST_NAME = "StudentEntity.findByLastName";
    private static final String SELECT_STUDENT_QUERY = "SELECT * FROM STUDENT WHERE USER_ID = :USER_ID AND PASSWORD = :PASSWORD";

    private EntityTransaction entityTransaction;
    private EntityManager em;

    public TestJPAStudentDao(String persistanceUnitName) {
	EntityManagerFactory emf = Persistence.createEntityManagerFactory(persistanceUnitName);
	this.em = emf.createEntityManager();
	this.entityTransaction = this.em.getTransaction();
	this.entityTransaction.setRollbackOnly();
    }

    @Override
    public boolean registerStudent(String userId, String password, String firstName, String lastName, String ssn, String email, String address) {

	StudentEntity studentEntity = StudentMapper.createEntity(userId, password, firstName, lastName, ssn, email, address);

	this.em.persist(studentEntity);

	if (this.em.contains(studentEntity)) {
	    return true;
	} else {
	    return false;
	}

    }

    @Override
    public StudentBO logon(String userId, String password) {
	Query query = this.em.createNativeQuery(SELECT_STUDENT_QUERY, StudentEntity.class);
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
	Query query = this.em.createNamedQuery(FIND_BY_LAST_NAME);
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
	StudentEntity studentEntity = this.em.find(StudentEntity.class, userId);

	if (studentEntity == null) {
	    return false;
	}
	logger.info("Found student entity: " + studentEntity);
	String courseId = course.getCourseId();

	logger.info("Finding courses entity for id = " + courseId);
	CoursesEntity coursesEntity = this.em.find(CoursesEntity.class, courseId);
	if (coursesEntity == null) {
	    logger.info("Did not find courses entity: " + coursesEntity);
	    return false;
	}

	logger.info("Found courses entity: " + coursesEntity);
	Set<CoursesEntity> courses = studentEntity.getCourses();

	boolean contains = courses.contains(coursesEntity);
	if (contains) {
	    return false;
	}

	courses.add(coursesEntity);

	logger.info("Persisting student entity: " + studentEntity);
	this.em.persist(studentEntity);
	this.em.persist(coursesEntity);
	return true;
    }

    public void rollback() {
	this.entityTransaction.rollback();
    }

    public void beginTestTransaction() {
	this.entityTransaction.begin();
    }

    public CriteriaBuilder getCriteriaBuilder() {
	return this.em.getCriteriaBuilder();
    }

    public <T> TypedQuery<T> createQuery(CriteriaQuery<T> cq) {
	return this.em.createQuery(cq);
    }

}
