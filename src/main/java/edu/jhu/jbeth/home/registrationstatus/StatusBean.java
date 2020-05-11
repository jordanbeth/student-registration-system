package edu.jhu.jbeth.home.registrationstatus;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.ejb.LocalBean;
import javax.ejb.Stateless;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.CourseDao;
import edu.jhu.jbeth.dao.DaoManager;

/**
 * Session Bean implementation class Status
 */
@Stateless
@LocalBean
public class StatusBean {

    private final CourseDao courseDao = DaoManager.courseDao();

    /**
     * Default constructor.
     */
    public StatusBean() {
	// Do nothing
    }

    public CourseBO getStatus(String courseId) {
	CourseBO course = this.courseDao.findCourseById(courseId);
	return course;

    }

    public List<CourseBO> getAllStatuses() {
	Collection<CourseBO> courses = this.courseDao.findAllCourses();

	List<CourseBO> coursesUpdated = new ArrayList<>();
	for (CourseBO course : courses) {
	    String courseId = course.getCourseId();
	    CourseBO updatedCourse = getStatus(courseId);
	    coursesUpdated.add(updatedCourse);
	}

	return coursesUpdated;
    }

}
