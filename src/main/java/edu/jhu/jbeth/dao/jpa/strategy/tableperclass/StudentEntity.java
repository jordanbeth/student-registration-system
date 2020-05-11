/**
 * 
 */
package edu.jhu.jbeth.dao.jpa.strategy.tableperclass;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

import edu.jhu.jbeth.dao.CourseDao;
import edu.jhu.jbeth.dao.StudentDao;
import edu.jhu.jbeth.dao.jpa.JPAStudentDao;
import edu.jhu.jbeth.dao.jpa.strategy.Level;

/**
 * @author jordanbeth
 *
 */

@Entity
@NamedQuery(name = JPAStudentDao.FIND_BY_LAST_NAME, query = "SELECT s FROM StudentEntity s WHERE " + StudentDao.LAST_NAME + " = ?1")
@Table(name = StudentDao.STUDENT)
public class StudentEntity extends PersonEntity {

    @Column(name = StudentDao.EMAIL)
    private String email;
    @Column(name = StudentDao.SSN)
    private String ssn;
    @Column(name = StudentDao.LEVEL)
    private Level level;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = CourseDao.STUDENT_HAS_COURSES, joinColumns = @JoinColumn(name = StudentDao.USER_ID), inverseJoinColumns = @JoinColumn(name = CourseDao.COURSE_ID))
    private Set<CoursesEntity> courses = new HashSet<>();

    public StudentEntity() {
	// do nothing
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

    /**
     * @return the courses
     */
    public Set<CoursesEntity> getCourses() {
	return this.courses;
    }

    /**
     * @param courses the courses to set
     */
    public void setCourses(Set<CoursesEntity> courses) {
	this.courses = courses;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("StudentEntity [email=");
	builder.append(this.email);
	builder.append(", ssn=");
	builder.append(this.ssn);
	builder.append(", level=");
	builder.append(this.level);
	builder.append(", userId=");
	builder.append(this.userId);
	builder.append(", password=");
	builder.append(this.password);
	builder.append(", firstName=");
	builder.append(this.firstName);
	builder.append(", lastName=");
	builder.append(this.lastName);
	builder.append(", address=");
	builder.append(this.address);
	builder.append("]");
	return builder.toString();
    }

}
