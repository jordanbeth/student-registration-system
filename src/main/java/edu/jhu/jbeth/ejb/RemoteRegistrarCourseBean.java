package edu.jhu.jbeth.ejb;

import edu.jhu.jbeth.bo.CourseBO;

public interface RemoteRegistrarCourseBean {
    
    boolean registerForCourse(String userId, CourseBO course);

    boolean registerForCourseLong(String userId, CourseBO course) throws Exception;

    int findNumRegistered(String courseId);
    
}
