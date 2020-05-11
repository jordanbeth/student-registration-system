/**
 * 
 */
package edu.jhu.jbeth.dao.jpa.strategy.tableperclass;

import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.Table;

import edu.jhu.jbeth.dao.CourseDao;

/**
 * @author jordanbeth
 *
 */
@Entity
@Table(name = CourseDao.COURSES_TABLE)
public class CoursesEntity extends CourseRegistration {
    private static final long serialVersionUID = 1L;

    @Column(name = CourseDao.COURSE_TITLE)
    private String courseTitle;

    @ManyToMany(mappedBy = "courses")
    private Set<StudentEntity> studentEntities;


    public CoursesEntity() {
	// The class must have a public or protected, no-argument constructor. The class
	// may have other constructors.
    }

    /**
     * @return the courseTitle
     */
    public String getCourseTitle() {
	return this.courseTitle;
    }

    /**
     * @param courseTitle the courseTitle to set
     */
    public void setCourseTitle(String courseTitle) {
	this.courseTitle = courseTitle;
    }

    /**
     * @return the studentEntities
     */
    public Set<StudentEntity> getStudentEntities() {
        return this.studentEntities;
    }

    /**
     * @param studentEntities the studentEntities to set
     */
    public void setStudentEntities(Set<StudentEntity> studentEntities) {
        this.studentEntities = studentEntities;
    }
    
    
    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("CoursesEntity [courseTitle=");
	builder.append(this.courseTitle);
	builder.append(", courseId=");
	builder.append(this.courseId);
	builder.append("]");
	return builder.toString();
    }

}
