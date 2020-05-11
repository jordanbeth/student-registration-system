package edu.jhu.jbeth;

public final class AppConstants {

    public static final String SRS_PERSISTENCE_UNIT = "StudentRegistrationSystem";
    // public static final String SRS_PERSISTENCE_UNIT_JTA = "StudentRegistrationSystem_JTA";

    public static final class Pages {
	// jsp pages
	public static final String REGISTER2_JSP = "register-2.jsp";
	public static final String REGISTER1_JSP = "register-1.jsp";
	public static final String LOGIN_JSP = "index.jsp";
	public static final String HOME_JSP = "home.jsp";
	public static final String COURSE_REG_STATUS_JSP = "reg-status.jsp";
	public static final String COURSE_REG_STATUS_RESULT_JSP = "reg-status-result.jsp";

	// jsf
	public static final String COURSE_REGISTRATION_JSF = "courses.xhtml";

	public static final String REGISTRATION_LOOKUP_JSF = "registration-lookup.xhtml";
	public static final String COURSE_REGISTRATION_RESULT_JSF = "courses-result.xhtml";
    }

    public static final class Request {
	public static final String RESET = "reset";
	public static final String BAD_LOGIN = "errorAction";
	public static final String SELECTED_COURSES = "selectedCourses";
	public static final String ACTION = "action";
	public static final String LOGIN = "login";
	public static final String LOGOUT = "logout";
	public static final String REGISTER = "register";
	public static final String COURSES = "courses";
	public static final String HIDDEN = "hidden";
	public static final String REGISTRATION_STATUS_REPORT = "registrationStatusReport";
	public static final String STUDENT_REGISTRATION_STATUS_REPORT = "studentRegistrationStatusReport";
	public static final String NEW_USER_REGISTRATION = "newUserRegistration";
    }

    public static final class Session {
	// session attributes
	public static final String ERRORS = "errors";
	public static final String SUCCESS = "success";
	public static final String MAX_ATTEMPT_BREACHED = "maxAttemptBreached";
	public static final String COURSE_LIST = "courseList";
	public static final String SELECTED_COURSE_LIST = "selectedCourseList";
    }

    public static final class Config {
	// configuration
	public static final String MAX_ATTEMPTS_CONFIG = "maxAttempts";
	public static final String COURSE_CAPACITY = "courseCapacity";
    }

    public static final class Styles {
	public static final String ERROR = "text-danger";
	public static final String SUCCESS = "text-success";
    }

    public static final class Message {
	// JMS config
	public static final String USERNAME = "mquser";
	public static final String PASSWORD = "mqpassword";
	public static final String INITIAL_CONTEXT_FACTORY = "org.wildfly.naming.client.WildFlyInitialContextFactory";
	public static final String PROVIDER_URL = "http-remoting://127.0.0.1:8080";
	public static final String DEFAULT_CONNECTION_FACTORY = "jms/RemoteConnectionFactory";
	public static final String REG_COURSE_TOPIC = "jms/topic/RegCourseTopic";
    }

}
