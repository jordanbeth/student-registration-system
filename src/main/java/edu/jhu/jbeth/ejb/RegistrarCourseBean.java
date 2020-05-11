package edu.jhu.jbeth.ejb;

import java.util.Set;
import java.util.logging.Logger;

import javax.ejb.EJBException;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TransactionAttribute;
import javax.ejb.TransactionAttributeType;
import javax.ejb.TransactionManagement;
import javax.ejb.TransactionManagementType;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import edu.jhu.jbeth.AppConstants;
import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.CoursesEntity;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.RegistrarEntity;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.StudentEntity;

/**
 * EJB implementation class RegistrarCourseBean
 */

@Stateless
@Remote(RemoteRegistrarCourseBean.class)
@TransactionAttribute(TransactionAttributeType.SUPPORTS)
@TransactionManagement(value = TransactionManagementType.CONTAINER)
public class RegistrarCourseBean implements RemoteRegistrarCourseBean {
    private static final Logger logger = Logger.getLogger(RegistrarCourseBean.class.getSimpleName());

    @PersistenceContext(unitName = AppConstants.SRS_PERSISTENCE_UNIT)
    private EntityManager em;

    /**
     * Default constructor.
     */
    public RegistrarCourseBean() {
	// Do nothing
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean registerForCourse(String userId, CourseBO course) {
	logger.info("Start registerForCourse(userId, course). userId=[" + userId + "], course[" + course + "]");

	StudentEntity studentEntity = this.em.find(StudentEntity.class, userId);
	logger.info("studentEntity = " + studentEntity);
	if (studentEntity == null) {
	    return false;
	}
	String courseId = course.getCourseId();

	CoursesEntity coursesEntity = this.em.find(CoursesEntity.class, courseId);
	logger.info("coursesEntity = " + coursesEntity);
	if (coursesEntity == null) {
	    return false;
	}

	Set<CoursesEntity> courses = studentEntity.getCourses();
	boolean contains = courses.contains(coursesEntity);
	logger.info("courses = " + contains);
	if (contains) {
	    courses.remove(coursesEntity);
	}

	RegistrarEntity registrarEntity = this.em.find(RegistrarEntity.class, courseId);
	logger.info("registrarEntity = " + registrarEntity);
	if (registrarEntity == null) {
	    return false;
	}

	int newNumRegistered = registrarEntity.getNumberRegistered() + 1;
	registrarEntity.setNumberRegistered(newNumRegistered);

	courses.add(coursesEntity);
	logger.info("registrarEntity after update = " + registrarEntity);
	this.em.persist(registrarEntity);
	this.em.persist(studentEntity);
	this.em.persist(coursesEntity);
	this.em.flush();

	logger.info("End registerForCourse(). true");
	return true;
    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public boolean registerForCourseLong(String userId, CourseBO course) throws Exception {
	logger.info("Start registerForCourseLong(userId, course). userId=[" + userId + "], course[" + course + "]");

	try {

	    StudentEntity studentEntity = this.em.find(StudentEntity.class, userId);
	    if (studentEntity == null) {
		return false;
	    }
	    String courseId = course.getCourseId();

	    CoursesEntity coursesEntity = this.em.find(CoursesEntity.class, courseId);
	    if (coursesEntity == null) {
		return false;
	    }

	    Set<CoursesEntity> courses = studentEntity.getCourses();

	    boolean contains = courses.contains(coursesEntity);
	    if (contains) {
		courses.remove(coursesEntity);
	    }

	    RegistrarEntity registrarEntity = this.em.find(RegistrarEntity.class, courseId);
	    if (registrarEntity == null) {
		return false;
	    }

	    int newNumRegistered = registrarEntity.getNumberRegistered() + 1;
	    registrarEntity.setNumberRegistered(newNumRegistered);

	    courses.add(coursesEntity);

	    courses.add(coursesEntity);

	    Thread.sleep(4_000); // 4 seconds

	    this.em.persist(registrarEntity);
	    this.em.persist(studentEntity);
	    this.em.persist(coursesEntity);
	    this.em.flush();
	    logger.info("End registerForCourseLong().");
	    return true;

	} catch (EJBException e) {
	    throw new Exception("EJBException Timeout! [" + e.getMessage() + "]");
	} catch (Exception e) {
	    throw e;
	}

    }

    @Override
    @TransactionAttribute(TransactionAttributeType.REQUIRED)
    public int findNumRegistered(String courseId) {
	final int numRegistered;
	RegistrarEntity registrarEntity = this.em.find(RegistrarEntity.class, courseId);
	if (registrarEntity == null) {
	    numRegistered = 0;
	} else {
	    numRegistered = registrarEntity.getNumberRegistered();
	}

	return numRegistered;
    }

}
