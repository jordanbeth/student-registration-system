package edu.jhu.jbeth.dao;

import edu.jhu.jbeth.dao.jpa.JPACourseDao;
import edu.jhu.jbeth.dao.jpa.JPAStudentDao;

public class DaoManager {
	
	public static final CourseDao courseDao() {
		return JPACourseDao.getInstance();
	}
	
	public static final StudentDao studentDao() {
		return JPAStudentDao.getInstance();
	}

}
