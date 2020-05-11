package edu.jhu.jbeth.login;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.jhu.jbeth.AppConstants;
import edu.jhu.jbeth.bo.StudentBO;
import edu.jhu.jbeth.dao.DaoManager;
import edu.jhu.jbeth.dao.StudentDao;
import edu.jhu.jbeth.util.RequestHelper;
import edu.jhu.jbeth.util.SessionHelper;

/**
 * Servlet implementation class LoginController
 */

@WebServlet(name = "LoginController", value = "/login")
public class LoginController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final String USER_ID = "user_id";
    private static final String PASSWORD = "pwd";

    private static int MAX_ATTEMPTS;

    private final StudentDao studentDao = DaoManager.studentDao();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public LoginController() {
	super();
    }

    public void init(ServletConfig servletConfig) throws ServletException {

	String maxAttemptsConfig = AppConstants.Config.MAX_ATTEMPTS_CONFIG;
	String maxAttemptsAsString = servletConfig.getInitParameter(maxAttemptsConfig);
	if (maxAttemptsAsString == null) {
	    throw new IllegalStateException("Initialization Parameter [" + maxAttemptsConfig + "] cannot be null");
	}

	try {
	    MAX_ATTEMPTS = Integer.parseInt(maxAttemptsAsString);
	} catch (NumberFormatException ex) {
	    throw new IllegalStateException("Initialization Parameter [" + maxAttemptsConfig + "] must be an integer");
	}
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	// Set content type and other response header fields first
	response.setContentType("text/html");

	HttpSession session = request.getSession();
	LoginBean bean = SessionHelper.getOrCreateBean(session, LoginBean.class);
	// Handle reset
	if (RequestHelper.hasParameterValue(request, AppConstants.Request.RESET)) {
	    handleReset(request, response, session, bean);
	    return;
	}

	// Handle reset
	if (RequestHelper.hasParameter(request, AppConstants.Request.BAD_LOGIN)) {
	    handleBadLogin(request, response);
	    return;
	}

	// Handle form submission
	Enumeration<String> params = request.getParameterNames();

	String paramName = null;
	String[] paramValues = null;

	List<String> errorList = new ArrayList<>();
	session.setAttribute(AppConstants.Session.ERRORS, errorList);

	// Parse the form parameters
	while (params.hasMoreElements()) {
	    paramName = (String) params.nextElement();
	    paramValues = request.getParameterValues(paramName);
	    handleParam(bean, paramName, paramValues, errorList);
	}

	attemptLogin(session, bean);

	if (errorList.isEmpty()) {
	    // valid, check data against db
	    StudentBO userBean = this.studentDao.logon(bean.getUserId(), bean.getPassword());

	    if (userBean != null) {
		// logon success
		bean.setSuccess();
		bean.setStudentBean(userBean);
	    }

	    // send validated user to their home page
	    RequestDispatcher requestDispatcher = request.getRequestDispatcher(AppConstants.Pages.HOME_JSP);
	    requestDispatcher.forward(request, response);
	    return;
	}

	RequestDispatcher requestDispatcher = request.getRequestDispatcher(AppConstants.Pages.LOGIN_JSP);
	requestDispatcher.forward(request, response);
    }

    private static void handleBadLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	String parameterValue = request.getParameter(AppConstants.Request.BAD_LOGIN);

	final String page;
	switch (parameterValue) {
	case AppConstants.Request.LOGIN:
	    page = AppConstants.Pages.LOGIN_JSP;
	    break;
	case AppConstants.Request.REGISTER:
	    page = AppConstants.Pages.REGISTER1_JSP;
	    break;
	default:
	    throw new IllegalStateException("Invalid parameter value: " + parameterValue);
	}

	RequestDispatcher dispatcher = request.getRequestDispatcher(page);
	dispatcher.forward(request, response);
    }

    private static void handleReset(HttpServletRequest request, HttpServletResponse response, HttpSession session, LoginBean bean) throws ServletException, IOException {
	bean.reset();
	session.removeAttribute(AppConstants.Session.ERRORS);
	RequestDispatcher dispatcher = request.getRequestDispatcher(AppConstants.Pages.LOGIN_JSP);
	dispatcher.forward(request, response);
    }

    private static void attemptLogin(HttpSession session, LoginBean bean) {
	int loginAttempt = bean.attemptLogin();

	if (loginAttempt >= MAX_ATTEMPTS) {
	    session.setAttribute(AppConstants.Session.MAX_ATTEMPT_BREACHED, Boolean.TRUE);
	} else {
	    session.setAttribute(AppConstants.Session.MAX_ATTEMPT_BREACHED, Boolean.FALSE);
	}
    }

    private static void handleParam(LoginBean bean, String paramName, String[] paramValues, List<String> errorList) {
	switch (paramName) {
	case USER_ID:
	    validateUserId(paramValues, errorList, bean);
	    break;

	case PASSWORD:
	    validatePassword(paramValues, errorList, bean);
	    break;
	default:
	    break;
	}

    }

    private static void validateUserId(String[] paramValues, List<String> errorList, LoginBean bean) {
	if (paramValues.length != 1) {
	    errorList.add("Please enter a user id.");
	    return;
	}

	String userId = paramValues[0];

	// The length should be 8-16 characters.
	if (userId.length() < 8 || userId.length() > 16) {
	    errorList.add("User id must be between 8 and 16 characters.");
	    return;
	}

	// The user id cannot contain a "space".
	if (userId.contains(" ")) {
	    errorList.add("User id cannot contain any blank characters.");
	    return;
	}

	bean.setUserId(userId);
    }

    private static void validatePassword(String[] paramValues, List<String> errorList, LoginBean bean) {
	if (paramValues.length != 1) {
	    errorList.add("Please enter your password.");
	    return;
	}

	String password = paramValues[0];

	// The length for password should be 8-16 characters.
	if (password.length() < 8 || password.length() > 16) {
	    errorList.add("Password must between 8 and 16 characters.");
	    return;
	}

	// The password cannot contain a "space".
	if (password.contains(" ")) {
	    errorList.add("Password cannot contain any blank characters.");
	    return;
	}

	bean.setPassword(password);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

}
