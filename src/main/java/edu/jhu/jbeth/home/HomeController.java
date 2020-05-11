package edu.jhu.jbeth.home;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import edu.jhu.jbeth.AppConstants;
import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.CourseDao;
import edu.jhu.jbeth.dao.DaoManager;
import edu.jhu.jbeth.util.SessionHelper;

/**
 * Servlet implementation class ClassRegistrationController
 */
@WebServlet(name = "HomeController", value = "/home", loadOnStartup = 1)
public class HomeController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public HomeController() {
	super();
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	String[] values = request.getParameterValues(AppConstants.Request.ACTION);
	if (values.length != 1) {
	    return;
	}

	final String nextPage;

	String value = values[0];
	switch (value) {
	case AppConstants.Request.COURSES:
	    // Go to the course registration page
	    nextPage = AppConstants.Pages.COURSE_REGISTRATION_JSF;
	    break;
	case AppConstants.Request.REGISTRATION_STATUS_REPORT:
	    // Go to the registration status report page
	    Collection<CourseBO> courses = DaoManager.courseDao().findAllCourses();
	    request.getSession().setAttribute(AppConstants.Session.COURSE_LIST, courses);
	    nextPage = AppConstants.Pages.COURSE_REG_STATUS_JSP;
	    break;
	case AppConstants.Request.STUDENT_REGISTRATION_STATUS_REPORT:
	    nextPage = AppConstants.Pages.REGISTRATION_LOOKUP_JSF;
	    break;
	case AppConstants.Request.LOGOUT:
	    // Go back to the index
	    SessionHelper.terminate(request);
	    nextPage = AppConstants.Pages.LOGIN_JSP;
	    break;
	default:
	    nextPage = AppConstants.Pages.HOME_JSP;
	    break;
	}

	RequestDispatcher requestDispatcher = request.getRequestDispatcher(nextPage);
	requestDispatcher.forward(request, response);

    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	doGet(request, response);
    }

}
