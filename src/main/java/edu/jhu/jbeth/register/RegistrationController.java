package edu.jhu.jbeth.register;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.ejb.EJB;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.jhu.jbeth.AppConstants;
import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.dao.DaoManager;
import edu.jhu.jbeth.dao.StudentDao;
import edu.jhu.jbeth.home.registrationstatus.StatusBean;
import edu.jhu.jbeth.util.RequestHelper;
import edu.jhu.jbeth.util.SessionHelper;

/**
 * Servlet implementation class RegistrationController
 */
@WebServlet(name = "RegistrationController", value = "/register")
public class RegistrationController extends HttpServlet {
    private static final long serialVersionUID = 1L;

    private static final Pattern VALID_EMAIL_ADDRESS_REGEX = Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    private static final String USER_ID = "user_id";
    private static final String PASSWORD = "pwd";
    private static final String FIRST_NAME = "first_name";
    private static final String LAST_NAME = "last_name";
    private static final String SSN = "ssn";
    private static final String EMAIL = "email";
    private static final String ADDRESS = "address";
    private static final String CITY = "city";
    private static final String STATE = "state";
    private static final String ZIP_CODE = "zip_code";
    private static final String REGISTER = "register";
    private static final String CONTINUE = "continue";

    @EJB
    StatusBean statusBean;

    StudentDao studentDao = DaoManager.studentDao();

    /**
     * @see HttpServlet#HttpServlet()
     */
    public RegistrationController() {
	super();
    }

    public void init(ServletConfig servletConfig) throws ServletException {
	super.init(servletConfig);
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

	// Set content type and other response header fields first
	response.setContentType("text/html");

	HttpSession session = request.getSession();

	boolean hasHidden = RequestHelper.hasParameter(request, AppConstants.Request.HIDDEN);
	if (hasHidden) {

	    String[] parameterValues = request.getParameterValues(AppConstants.Request.HIDDEN);
	    if (parameterValues.length == 1) {
		String hiddenFieldValue = parameterValues[0];
		switch (hiddenFieldValue) {
		case AppConstants.Request.NEW_USER_REGISTRATION:
		    // handle new user registration
		    handleNewUserRegistration(request, response, session);
		    break;

		case AppConstants.Request.REGISTRATION_STATUS_REPORT:
		    // handle registration status report
		    handleRegistrationStatusReport(request, response, session);
		default:
		    break;
		}
	    }

	}

    }

    private void handleRegistrationStatusReport(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {

	String[] selectedCourseIds = request.getParameterValues(AppConstants.Request.SELECTED_COURSES);

	List<CourseBO> courseStatuses;
	if (selectedCourseIds == null) {
	    courseStatuses = this.statusBean.getAllStatuses();
	} else {
	    courseStatuses = new ArrayList<>();

	    List<String> errors = new ArrayList<>();

	    for (String courseId : selectedCourseIds) {
		CourseBO status = this.statusBean.getStatus(courseId);
		if (status == null) {
		    // error
		    errors.add("Course id [" + courseId + "] is not a valid course id.");
		    session.setAttribute(AppConstants.Session.ERRORS, errors);
		} else {
		    courseStatuses.add(status);
		}
	    }
	}

	session.setAttribute(AppConstants.Session.SELECTED_COURSE_LIST, courseStatuses);
	// Route to next page
	RequestDispatcher dispatcher = request.getRequestDispatcher(AppConstants.Pages.COURSE_REG_STATUS_RESULT_JSP);
	dispatcher.forward(request, response);
    }

    private void handleNewUserRegistration(HttpServletRequest request, HttpServletResponse response, HttpSession session) throws ServletException, IOException {
	RegistrationBean bean = SessionHelper.getOrCreateBean(session, RegistrationBean.class);

	// Handle reset
	if (RequestHelper.hasParameterValue(request, AppConstants.Request.RESET)) {
	    // go back to first form and reset bean
	    bean.reset();
	    session.removeAttribute(AppConstants.Session.ERRORS);
	    RequestDispatcher dispatcher = request.getRequestDispatcher(AppConstants.Pages.REGISTER1_JSP);
	    dispatcher.forward(request, response);
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

	// Route to next page
	RequestDispatcher dispatcher = request.getRequestDispatcher(AppConstants.Pages.REGISTER1_JSP);

	if (isContinue(request) && isFirstFormValid(bean)) {

	    dispatcher = request.getRequestDispatcher(AppConstants.Pages.REGISTER2_JSP);

	} else if (isRegister(request)) {

	    if (isBeanValid(bean)) {

		String userId = bean.getUserId();
		String password = bean.getPassword();
		String firstName = bean.getFirstName();
		String lastName = bean.getLastName();
		String ssn = bean.getSsn();
		String email = bean.getEmail();
		String address = createAddress(bean);

		boolean register = this.studentDao.registerStudent(userId, password, firstName, lastName, ssn, email, address);
		if (register) {
		    session.setAttribute(AppConstants.Session.SUCCESS, Boolean.TRUE);
		    dispatcher = request.getRequestDispatcher(AppConstants.Pages.LOGIN_JSP);
		} else {
		    session.setAttribute(AppConstants.Session.SUCCESS, Boolean.FALSE);
		    dispatcher = request.getRequestDispatcher(AppConstants.Pages.REGISTER2_JSP);
		}
	    } else {
		session.setAttribute(AppConstants.Session.SUCCESS, Boolean.FALSE);
		dispatcher = request.getRequestDispatcher(AppConstants.Pages.REGISTER2_JSP);
	    }
	}

	dispatcher.forward(request, response);
    }

    private static String createAddress(RegistrationBean bean) {
	String address = bean.getAddress();
	String city = bean.getCity();
	String state = bean.getState();
	String zipCode = bean.getZipCode();

	StringBuilder sb = new StringBuilder();
	sb.append(address);
	sb.append(", ");
	sb.append(city);
	sb.append(", ");
	sb.append(state);
	sb.append(", ");
	sb.append(zipCode);

	return sb.toString();
    }

    private static void handleParam(RegistrationBean bean, String paramName, String[] paramValues, List<String> errorList) {
	switch (paramName) {

	case USER_ID:
	    validateUserId(paramValues, errorList, bean);
	    break;

	case PASSWORD:
	    validatePassword(paramValues, errorList, bean);
	    break;

	case FIRST_NAME:
	    validateFirstName(paramValues, errorList, bean);
	    break;

	case LAST_NAME:
	    validateLastName(paramValues, errorList, bean);
	    break;

	case SSN:
	    validateSSN(paramValues, errorList, bean);
	    break;

	case EMAIL:
	    validateEmail(paramValues, errorList, bean);
	    break;

	case ADDRESS:
	    validateAddress(paramValues, errorList, bean);
	    break;

	case CITY:
	    validateCity(paramValues, errorList, bean);
	    break;

	case STATE:
	    validateState(paramValues, errorList, bean);
	    break;

	case ZIP_CODE:
	    validateZipCode(paramValues, errorList, bean);
	    break;

	default:

	}
    }

    private static boolean isRegister(HttpServletRequest request) {
	return RequestHelper.hasParameter(request, REGISTER);
    }

    private static boolean isContinue(HttpServletRequest request) {
	return RequestHelper.hasParameter(request, CONTINUE);
    }

    private static boolean isBeanValid(RegistrationBean bean) {
	return isFirstFormValid(bean) && isSecondFormValid(bean);
    }

    private static boolean isSecondFormValid(RegistrationBean bean) {
	if (bean.getAddress() == null) {
	    return false;
	}

	if (bean.getCity() == null) {
	    return false;
	}

	if (bean.getState() == null) {
	    return false;
	}

	if (bean.getZipCode() == null) {
	    return false;
	}

	return true;
    }

    private static boolean isFirstFormValid(RegistrationBean bean) {
	if (bean.getUserId() == null) {
	    return false;
	}
	if (bean.getPassword() == null) {
	    return false;
	}

	if (bean.getFirstName() == null) {
	    return false;
	}

	if (bean.getLastName() == null) {
	    return false;
	}

	if (bean.getSsn() == null) {
	    return false;
	}

	if (bean.getEmail() == null) {
	    return false;
	}

	return true;
    }

    private static void validateZipCode(String[] paramValues, List<String> errorList, RegistrationBean bean) {
	if (paramValues.length != 1) {
	    errorList.add("Please enter a zip code.");
	    return;
	}

	String param = paramValues[0];
	if (isNumeric(param)) {
	    bean.setZipCode(param);
	} else {
	    errorList.add("Zip code can only have numeric values.");
	}

    }

    public static boolean isNumeric(String str) {
	for (char c : str.toCharArray()) {
	    if (!Character.isDigit(c)) {
		return false;
	    }
	}
	return true;
    }

    private static void validateCity(String[] paramValues, List<String> errorList, RegistrationBean bean) {
	if (paramValues.length != 1) {
	    errorList.add("Please enter an address.");
	    return;
	}

	String city = paramValues[0];

	if (city.length() > 1) {
	    bean.setCity(city);
	} else {
	    errorList.add("City must be at least 2 characters.");
	}
    }

    private static void validateState(String[] paramValues, List<String> errorList, RegistrationBean bean) {
	if (paramValues.length != 1) {
	    errorList.add("Please enter a state.");
	    return;
	}

	String state = paramValues[0];
	if (state.length() <= 1) {
	    errorList.add("Please enter a state.");
	    return;
	}

	bean.setState(state);
    }

    private static void validateAddress(String[] paramValues, List<String> errorList, RegistrationBean bean) {
	if (paramValues.length != 1) {
	    errorList.add("Please enter an address.");
	    return;
	}

	String address = paramValues[0];

	if (address.length() > 5) {
	    bean.setAddress(address);
	} else {
	    errorList.add("Address must be at least 5 characters.");
	}
    }

    private static void validateEmail(String[] paramValues, List<String> errorList, RegistrationBean bean) {
	if (paramValues.length != 1) {
	    errorList.add("Please enter an email");
	    return;
	}

	String email = paramValues[0];

	Matcher matcher = VALID_EMAIL_ADDRESS_REGEX.matcher(email);
	if (matcher.find()) {
	    bean.setEmail(email);
	} else {
	    errorList.add("Please enter a valid email");

	}

    }

    private static void validateSSN(String[] paramValues, List<String> errorList, RegistrationBean bean) {
	if (paramValues.length != 3) {
	    errorList.add("Please enter a full SSN.");
	    return;
	}

	String fullSsn = "";
	for (String val : paramValues) {
	    if (isNumeric(val)) {
		fullSsn += val;
	    } else {
		errorList.add("SSN must only contain numeric values.");
		return;
	    }
	}

	if (fullSsn.length() != 9) {
	    errorList.add("Invalid length of SSN.");
	    return;

	}

	bean.setSsn(fullSsn);
    }

    private static void validateLastName(String[] paramValues, List<String> errorList, RegistrationBean bean) {
	if (paramValues.length != 1) {
	    errorList.add("Please enter a last name.");
	    return;
	}

	String param = paramValues[0];

	if (param.length() < 1) {
	    errorList.add("Last name must be at least 1 character.");
	    return;
	}

	bean.setLastName(param);
    }

    private static void validateFirstName(String[] paramValues, List<String> errorList, RegistrationBean bean) {
	if (paramValues.length != 1) {
	    errorList.add("Please enter a first name.");
	    return;
	}

	String param = paramValues[0];

	if (param.length() < 1) {
	    errorList.add("First name must be at least 1 character.");
	    return;
	}

	bean.setFirstName(param);

    }

    private static void validatePassword(String[] paramValues, List<String> errorList, RegistrationBean bean) {
	if (paramValues.length != 2) {
	    errorList.add("Please enter and confirm your password.");
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

	String confirmPass = paramValues[1];
	if (!password.equals(confirmPass)) {
	    errorList.add("Passwords must match.");
	    return;
	}

	bean.setPassword(password);
    }

    private static void validateUserId(String[] paramValues, List<String> errorList, RegistrationBean bean) {
	if (paramValues.length != 1) {
	    errorList.add("Please enter a user id.");
	    return;
	}

	String param = paramValues[0];

	// The length should be 8-16 characters.
	if (param.length() < 8 || param.length() > 16) {
	    errorList.add("User id must be between 8 and 16 characters.");
	    return;
	}

	// The user id cannot contain a "space".
	if (param.contains(" ")) {
	    errorList.add("User id cannot contain any blank characters.");
	    return;
	}

	bean.setUserId(param);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
	doGet(request, response);
    }

}
