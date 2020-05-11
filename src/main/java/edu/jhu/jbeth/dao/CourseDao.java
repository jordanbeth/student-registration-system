package edu.jhu.jbeth.dao;

import java.util.List;

import edu.jhu.jbeth.bo.CourseBO;

public interface CourseDao {

    static final String COURSES_TABLE = "COURSES";
    static final String COURSE_ID = "COURSE_ID";
    static final String COURSE_TITLE = "COURSE_TITLE";
    static final String REGISTRAR_TABLE = "REGISTRAR";
    static final String NUMBER_REGISTERED = "NUMBER_REGISTERED";
    static final String STUDENT_HAS_COURSES = "STUDENT_HAS_COURSES";

    CourseBO findCourseById(String courseId);

    List<CourseBO> findAllCourses();

    void populateCache();

    boolean registerForCourse(CourseBO course);

    int findNumRegistered(String courseNumber);

}
