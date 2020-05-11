package edu.jhu.jbeth.dao.jpa;

import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import javax.persistence.TypedQuery;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.CourseDao;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.CoursesEntity;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.RegistrarEntity;

public class JPACourseDao implements CourseDao {
    private static final Logger logger = Logger.getLogger(JPACourseDao.class.getSimpleName());

    // Singleton
    private static JPACourseDao INSTANCE;

    public static JPACourseDao getInstance() {
	if (INSTANCE == null) {
	    INSTANCE = new JPACourseDao();
	}

	return INSTANCE;
    }

    private final JPAConnection conn = JPAConnection.getInstance();

    @Override
    public CourseBO findCourseById(String courseId) {
	CoursesEntity coursesEntity = this.conn.find(CoursesEntity.class, courseId);
	return mapWithRegistrarData(coursesEntity);
    }

    @Override
    public List<CourseBO> findAllCourses() {
	TypedQuery<CoursesEntity> query = this.conn.createQueryAll(CoursesEntity.class.getSimpleName(), CoursesEntity.class);
	List<CoursesEntity> courseEntities = query.getResultList();
	List<CourseBO> courses = courseEntities.stream().map(c -> mapWithRegistrarData(c)).collect(Collectors.toList());
	return courses;
    }

    private CourseBO mapWithRegistrarData(CoursesEntity c) {
	logger.info("Mapping... [" + c + "]");
	final CourseBO course = map(c);
	String courseId = c.getCourseId();
	RegistrarEntity registrarEntity = getRegistrarEntityByCourseId(courseId);
	if (registrarEntity != null) {
	    int numReg = registrarEntity.getNumberRegistered();
	    course.setNumRegistered(numReg);
	}
	return course;
    }

    @Override
    public void populateCache() {
	// Do nothing
    }

    @Override
    public boolean registerForCourse(CourseBO course) {
	String courseId = course.getCourseId();
	CoursesEntity coursesEntity = this.conn.find(CoursesEntity.class, courseId);
	if (coursesEntity != null) {
	    this.conn.begin();
	    RegistrarEntity registrarEntity = getRegistrarEntityByCourseId(courseId);
	    int newNumRegistered = registrarEntity.getNumberRegistered() + 1;
	    registrarEntity.setNumberRegistered(newNumRegistered);
	    this.conn.commit();
	    course.setNumRegistered(newNumRegistered);
	    return true;

	}
	return false;
    }

    private RegistrarEntity getRegistrarEntityByCourseId(String courseId) {
	TypedQuery<RegistrarEntity> selectRegistrar = this.conn.typedQuery("from " + RegistrarEntity.class.getSimpleName() + " r where r.courseId = :COURSE_ID", RegistrarEntity.class);
	selectRegistrar.setParameter(COURSE_ID, courseId);
	RegistrarEntity registrarEntity = selectRegistrar.getSingleResult();
	logger.info("registrarEntity... [" + registrarEntity + "]");
	return registrarEntity;
    }

    private static CourseBO map(CoursesEntity coursesEntity) {
	String courseId = coursesEntity.getCourseId();
	String courseTitle = coursesEntity.getCourseTitle();

	CourseBO course = new CourseBO(courseId, courseTitle);
	return course;
    }

    @Override
    public int findNumRegistered(String courseId) {
	int numberRegistered = -1;

	CoursesEntity coursesEntity = this.conn.find(CoursesEntity.class, courseId);
	if (coursesEntity != null) {
	    RegistrarEntity registrarEntity = this.conn.find(RegistrarEntity.class, courseId);
	    if (registrarEntity != null) {
		numberRegistered = registrarEntity.getNumberRegistered();
	    }
	}
	return numberRegistered;
    }
}
