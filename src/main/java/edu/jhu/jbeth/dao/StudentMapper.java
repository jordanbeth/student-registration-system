package edu.jhu.jbeth.dao;

import java.util.Set;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.bo.StudentBO;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.CoursesEntity;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.StudentEntity;

/**
 * 
 * @author jordanbeth
 *
 */
public class StudentMapper {

    public static StudentEntity createEntity(String userId, String password, String firstName, String lastName, String ssn, String email, String address) {

	StudentEntity studentEntity = new StudentEntity();
	studentEntity.setUserId(userId);
	studentEntity.setPassword(password);
	studentEntity.setFirstName(firstName);
	studentEntity.setLastName(lastName);
	studentEntity.setSsn(ssn);
	studentEntity.setEmail(email);
	studentEntity.setAddress(address);

	return studentEntity;
    }

    public static StudentBO mapToBO(StudentEntity studentEntity) {
	StudentBO studentBean = new StudentBO();
	studentBean.setUserId(studentEntity.getUserId());
	studentBean.setFirstName(studentEntity.getFirstName());
	studentBean.setLastName(studentEntity.getLastName());
	studentBean.setSsn(studentEntity.getSsn());
	studentBean.setEmail(studentEntity.getEmail());
	studentBean.setAddress(studentEntity.getAddress());

	Set<CoursesEntity> courses = studentEntity.getCourses();
	if (courses != null) {
	    Set<CourseBO> courseBOs = CourseMapper.mapToBO(courses);
	    studentBean.setCourses(courseBOs);
	}

	return studentBean;
    }

}
