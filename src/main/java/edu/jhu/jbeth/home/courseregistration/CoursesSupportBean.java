/**
 * 
 */
package edu.jhu.jbeth.home.courseregistration;

import java.io.Serializable;
import java.util.List;
import java.util.Set;
import java.util.TreeMap;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.inject.Named;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.DaoManager;

/**
 * @author jordanbeth
 *
 */

@Named("coursesSupportBean")
@SessionScoped
public class CoursesSupportBean implements Serializable {
    private static final Logger logger = Logger.getLogger(CoursesSupportBean.class.getSimpleName());
    private static final long serialVersionUID = 1L;

    private final TreeMap<String, CourseBO> coursesByStringRepresentation = new TreeMap<>();

    private Set<String> coursesAsStrings = this.coursesByStringRepresentation.keySet();
    private String selectedCourseAsString;

    public CoursesSupportBean() {
	// Do nothing
    }

    @PostConstruct
    public void init() {
	// Initialize the list of values for the SelectOneMenu
	populateCourseList();
    }

    private void populateCourseList() {
	logger.info("Populating courses from database...");
	List<CourseBO> courses = DaoManager.courseDao().findAllCourses();

	int coursesCount = courses.size();
	if (coursesCount > 0) {
	    CourseBO course = courses.get(0);
	    String courseAsString = course.toString();
	    this.coursesByStringRepresentation.put(courseAsString, course);
	    this.selectedCourseAsString = courseAsString;
	}

	for (int i = 1; i < coursesCount; i++) {
	    CourseBO course = courses.get(i);
	    String courseAsString = course.toString();
	    this.coursesByStringRepresentation.put(courseAsString, course);
	}
    }

    public String getSelectedCourseAsString() {
	return this.selectedCourseAsString;
    }

    public void setSelectedCourseAsString(String selectedCourseAsString) {
	this.selectedCourseAsString = selectedCourseAsString;
    }

    public Set<String> getCoursesAsStrings() {
	return this.coursesAsStrings;
    }

    public CourseBO getSelectedCourse() {
	return this.coursesByStringRepresentation.get(this.selectedCourseAsString);
    }
}
