package edu.jhu.jbeth.dao.jdbc;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.CourseDao;

/**
 * 
 * @author jordanbeth
 *
 */
public class JDBCCourseDao implements CourseDao {
	// Singleton
	private static JDBCCourseDao INSTANCE;

	public static JDBCCourseDao getInstance() {
		if (INSTANCE == null) {
			INSTANCE = new JDBCCourseDao();
		}

		return INSTANCE;
	}

	private static final String SELECT_ALL_COURSES = "SELECT * FROM COURSES";

	private final PreparedStatement selectNumRegisteredByCourseNumStmt;
	private final PreparedStatement updateRegistrarStmt;
	private final PreparedStatement selectCourseAndNumRegisteredByCourseIdStmt;

	private Map<String, CourseBO> coursesCache;

	private JDBCCourseDao() {

		try {
			this.selectNumRegisteredByCourseNumStmt = JDBCConnection.getInstance()
					.prepareStatement("SELECT NUMBER_REGISTERED FROM REGISTRAR WHERE COURSE_ID = ?");

			this.updateRegistrarStmt = JDBCConnection.getInstance().prepareStatement(
					"UPDATE REGISTRAR SET NUMBER_REGISTERED = NUMBER_REGISTERED + 1 WHERE COURSE_ID = ?");

			this.selectCourseAndNumRegisteredByCourseIdStmt = JDBCConnection.getInstance().prepareStatement(
					"SELECT c.COURSE_ID, c.COURSE_TITLE, r.NUMBER_REGISTERED FROM COURSES as c INNER JOIN REGISTRAR as r ON c.COURSE_ID = r.COURSE_ID AND c.COURSE_ID = ?");

		} catch (SQLException e) {
			e.printStackTrace();
			throw new RuntimeException(e.getMessage());
		}

	}
	
	@Override
	public List<CourseBO> findAllCourses() {
		if (this.coursesCache == null) { // don't hit the database twice for static data
			Map<String, CourseBO> courseList = new HashMap<>();
			try (JDBCConnection conn = JDBCConnection.getInstance().tryOpen()) {
				Statement statement = conn.createStatement();
				ResultSet resultSet = statement.executeQuery(SELECT_ALL_COURSES);
				while (resultSet.next()) {
					String courseNumber = resultSet.getString(1);
					String courseName = resultSet.getString(2);
					CourseBO course = new CourseBO(courseNumber, courseName);

					int numberRegistered = findNumRegistered(courseNumber);
					course.setNumRegistered(numberRegistered);
					courseList.put(courseNumber, course);

				}

				this.coursesCache = courseList;
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}

		Collection<CourseBO> values = this.coursesCache.values();
		List<CourseBO> collect = values.stream().collect(Collectors.<CourseBO>toList());
		return Collections.unmodifiableList(collect);
	}

	@Override
	public CourseBO findCourseById(String courseId) {
		CourseBO course = null;
		if (this.coursesCache == null) {
			try (JDBCConnection conn = JDBCConnection.getInstance().tryOpen()) {
				this.selectCourseAndNumRegisteredByCourseIdStmt.setString(1, courseId);
				ResultSet resultSet = this.selectCourseAndNumRegisteredByCourseIdStmt.executeQuery();
				if (resultSet.first()) {
					String courseIdFromDB = resultSet.getString(COURSE_ID);
					String courseTitle = resultSet.getString(COURSE_TITLE);
					int numRegistered = resultSet.getInt(NUMBER_REGISTERED);
					course = new CourseBO(courseIdFromDB, courseTitle);
					course.setNumRegistered(numRegistered);
					this.coursesCache.put(courseIdFromDB, course);
				}

			} catch (SQLException e) {
				e.printStackTrace();
			}
		} else {
			// go to cache
			course = this.coursesCache.get(courseId);
		}

		return course;
	}

	@Override
	public boolean registerForCourse(CourseBO course) {

		boolean success = false;
		try (JDBCConnection conn = JDBCConnection.getInstance().tryOpen()) {

			String courseNumber = course.getCourseId();
			this.updateRegistrarStmt.setString(1, courseNumber);

			int executeUpdate = this.updateRegistrarStmt.executeUpdate();
			if (executeUpdate > 0) {
				success = true;
				course.incrementNumRegistered();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return success;
	}

	@Override
	public int findNumRegistered(String courseId) {
		int numRegistered = -1;

		try (JDBCConnection conn = JDBCConnection.getInstance().tryOpen()) {
			this.selectNumRegisteredByCourseNumStmt.setString(1, courseId);
			ResultSet resultSet = this.selectNumRegisteredByCourseNumStmt.executeQuery();
			if (resultSet.first()) {
				numRegistered = resultSet.getInt(NUMBER_REGISTERED);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

		return numRegistered;
	}

	@Override
	public void populateCache() {
		getInstance().findAllCourses();
	}

}
