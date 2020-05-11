/**
 * 
 */
package edu.jhu.jbeth.bo;

import java.util.HashSet;
import java.util.Set;

/**
 * @author jordanbeth
 *
 */

public class StudentBO {

    private String userId;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String ssn;
    private Set<CourseBO> courses = new HashSet<>();

    public StudentBO() {
	// do nothing
    }

    public void addCourse(CourseBO course) {
	this.courses.add(course);
    }

    public String getUserId() {
	return this.userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
	return this.firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public String getLastName() {
	return this.lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public String getEmail() {
	return this.email;
    }

    /**
     * @param email the email to set
     */
    public void setEmail(String email) {
	this.email = email;
    }

    /**
     * @return the address
     */
    public String getAddress() {
	return this.address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
	this.address = address;
    }

    /**
     * @return the ssn
     */
    public String getSsn() {
	return this.ssn;
    }

    /**
     * @param ssn the ssn to set
     */
    public void setSsn(String ssn) {
	this.ssn = ssn;
    }

    public Set<CourseBO> getCourses() {
	return this.courses;
    }

    public void setCourses(Set<CourseBO> courses) {
	this.courses = courses;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("StudentBO [userId=");
	builder.append(this.userId);
	builder.append(", firstName=");
	builder.append(this.firstName);
	builder.append(", lastName=");
	builder.append(this.lastName);
	builder.append(", email=");
	builder.append(this.email);
	builder.append(", address=");
	builder.append(this.address);
	builder.append(", ssn=");
	builder.append(this.ssn);
	builder.append(", courses=");
	builder.append(this.courses);
	builder.append("]");
	return builder.toString();
    }	
    
}
