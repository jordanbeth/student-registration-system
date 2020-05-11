package edu.jhu.jbeth.dao.jpa.strategy.tableperclass;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import edu.jhu.jbeth.dao.CourseDao;
import edu.jhu.jbeth.dao.jpa.PersistantEntity;

@MappedSuperclass
public abstract class CourseRegistration implements Serializable, PersistantEntity {
    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = CourseDao.COURSE_ID)
    protected String courseId;

    public CourseRegistration() {
	// The class must have a public or protected, no-argument constructor. The class
	// may have other constructors.
    }

    /**
     * @return the courseId
     */
    public String getCourseId() {
	return this.courseId;
    }

    /**
     * @param courseId the courseId to set
     */
    public void setCourseId(String courseId) {
	this.courseId = courseId;
    }

}
