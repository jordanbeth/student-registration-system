/**
 * 
 */
package edu.jhu.jbeth.bo;

import java.io.Serializable;

/**
 * @author jordanbeth
 *
 */
public class CourseBO implements Serializable {
    private static final long serialVersionUID = 1L;
    private final String courseId;
    private final String courseTitle;
    private int numRegistered;

    public CourseBO(String courseId, String courseName) {
	this.courseId = courseId;
	this.courseTitle = courseName;
    }

    /**
     * @return the courseId
     */
    public final String getCourseId() {
	return this.courseId;
    }

    /**
     * @return the courseTitle
     */
    public final String getCourseTitle() {
	return this.courseTitle;
    }

    public final void setNumRegistered(int numberRegistered) {
	this.numRegistered = numberRegistered;
    }

    public final int getNumRegistered() {
	return this.numRegistered;
    }

    public void incrementNumRegistered() {
	this.numRegistered++;
    }

    @Override
    public String toString() {
	return this.courseId + ": " + this.courseTitle;
    }

}
