package edu.jhu.jbeth.dao;

import java.util.HashSet;
import java.util.Set;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.jpa.strategy.tableperclass.CoursesEntity;

public class CourseMapper {

    public static Set<CourseBO> mapToBO(Set<CoursesEntity> courses) {
	Set<CourseBO> courseBOs = new HashSet<>();
	for (CoursesEntity coursesEntity : courses) {
	    courseBOs.add(mapToBO(coursesEntity));
	}

	return courseBOs;
    }

    public static CourseBO mapToBO(CoursesEntity coursesEntity) {
	String courseId = coursesEntity.getCourseId();
	String courseTitle = coursesEntity.getCourseTitle();
	CourseBO courseBO = new CourseBO(courseId, courseTitle);
	return courseBO;
    }

    public static CoursesEntity createCoursesEntity(String courseId, String courseTitle) {
	CoursesEntity entity = new CoursesEntity();
	entity.setCourseId(courseId);
	entity.setCourseTitle(courseTitle);
	return entity;
    }

}
