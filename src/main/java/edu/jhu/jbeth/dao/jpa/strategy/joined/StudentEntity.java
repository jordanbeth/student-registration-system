/**
 * 
 */
package edu.jhu.jbeth.dao.jpa.strategy.joined;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.PrimaryKeyJoinColumn;
import javax.persistence.Table;
import javax.persistence.Transient;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.StudentDao;
import edu.jhu.jbeth.dao.jpa.strategy.Level;

/**
 * @author jordanbeth
 *
 */

@Entity(name = "StudentEntityJoined")
@Table(name = StudentDao.STUDENT)
@PrimaryKeyJoinColumn(name = StudentDao.USER_ID)
public class StudentEntity extends PersonEntity {

    @Column(name = StudentDao.EMAIL)
    private String email;

    @Column(name = StudentDao.LEVEL)
    @Enumerated(EnumType.STRING)
    private Level level;

    @Transient
    private List<CourseBO> courses = new ArrayList<>();

    public StudentEntity() {
	// do nothing
    }

    public void addCourse(CourseBO course) {
	this.courses.add(course);
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
     * @return the level
     */
    public Level getLevel() {
	return this.level;
    }

    /**
     * @param level the level to set
     */
    public void setLevel(Level level) {
	this.level = level;
    }

}
