/**
 * 
 */
package edu.jhu.jbeth.home.courseregistration;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.ejb.EJB;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jms.JMSException;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.jhu.jbeth.AppConstants;
import edu.jhu.jbeth.bo.CourseBO;
import edu.jhu.jbeth.ejb.RemoteRegistrarCourseBean;
import edu.jhu.jbeth.login.LoginBean;
import edu.jhu.jbeth.message.JMSMessageProducer;
import edu.jhu.jbeth.util.Logged;
import edu.jhu.jbeth.util.SessionHelper;

/**
 * @author jordanbeth
 *
 */

@Named("registrationSupportBean")
@SessionScoped
public class RegistrationSupportBean implements Serializable {

    private static final Logger logger = Logger.getLogger(RegistrationSupportBean.class.getSimpleName());

    private static final long serialVersionUID = 1L;

    private static final String SUCCESS_MESSAGE = "You have been registered to ";
    private static final String ERROR_MESSAGE = "Sorry, the registration to this course has been closed based on availability";

    @Inject
    private CoursesSupportBean coursesSupportBean;
    
    @EJB(lookup = "java:global/StudentRegistrationSystem/RegistrarCourseBean!edu.jhu.jbeth.ejb.RemoteRegistrarCourseBean")
    private RemoteRegistrarCourseBean registrarCourseBean;

    private int courseCapacity;

    private final List<CourseBO> registeredCourses = new ArrayList<>();

    private String message;
    private String styleClass;

    public RegistrationSupportBean() {
	// Do nothing
    }

    @PostConstruct
    public void init() {
	this.message = "";
	this.styleClass = "";
	String courseCapacityAsString = FacesContext.getCurrentInstance().getExternalContext().getInitParameter(AppConstants.Config.COURSE_CAPACITY);
	this.courseCapacity = Integer.parseInt(courseCapacityAsString);
    }

    public static void logout() throws ServletException, IOException {
	HttpSession session = getSession();
	session.invalidate();

	goTo(AppConstants.Pages.LOGIN_JSP);
    }

    private static void goTo(String page) throws ServletException, IOException {
	// go to login page
	HttpServletRequest request = getRequest();
	RequestDispatcher requestDispatcher = request.getRequestDispatcher(page);
	requestDispatcher.forward(request, getResponse());
    }

    @Logged
    public void register() throws IOException {
	logger.info("Registering for course max enrollement = [" + this.courseCapacity + "]");

	CourseBO course = this.coursesSupportBean.getSelectedCourse();
	if (course == null) {
	    logger.severe("Unable to find the selected course.");
	    return;
	}

	logger.info(course.toString());

	HttpSession session = getSession();
	LoginBean loginBean = SessionHelper.getOrCreateBean(session, LoginBean.class);
	String userId = loginBean.getUserId();
	if (userId == null) {
	    return;
	}

	// Send the attempt to the message producer
	try {
	    JMSMessageProducer.getInstance().sendRegistrationMessage(userId, course);
	} catch (JMSException e) {
	    e.printStackTrace();
	}

	// set the default message to error
	setError();

	int numberRegistered = this.registrarCourseBean.findNumRegistered(course.getCourseId());
	if (numberRegistered < this.courseCapacity) {

	    boolean didRegister = this.registrarCourseBean.registerForCourse(userId, course);
	    if (didRegister) {
		this.registeredCourses.add(course);
		setSuccess(course);
	    }

	}

	FacesContext.getCurrentInstance().getExternalContext().redirect(AppConstants.Pages.COURSE_REGISTRATION_RESULT_JSF);
    }

    private void setSuccess(CourseBO course) {
	this.message = SUCCESS_MESSAGE + course.toString();
	this.styleClass = AppConstants.Styles.SUCCESS;
    }

    private void setError() {
	this.message = ERROR_MESSAGE;
	this.styleClass = AppConstants.Styles.ERROR;
    }

    public void home() throws ServletException, IOException {
	goTo(AppConstants.Pages.HOME_JSP);
    }

    public void back() throws IOException {
	FacesContext.getCurrentInstance().getExternalContext().redirect(AppConstants.Pages.COURSE_REGISTRATION_JSF);
    }

    /**
     * @return the message
     */
    public String getMessage() {
	return this.message;
    }

    /**
     * @return the styleClass
     */
    public String getStyleClass() {
	return this.styleClass;
    }

    /**
     * @param styleClass the styleClass to set
     */
    public void setStyleClass(String styleClass) {
	this.styleClass = styleClass;
    }

    private static HttpSession getSession() {
	HttpServletRequest request = getRequest();
	HttpSession session = request.getSession();
	return session;
    }

    private static HttpServletRequest getRequest() {
	HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
	return request;
    }

    private static HttpServletResponse getResponse() {
	HttpServletResponse response = (HttpServletResponse) FacesContext.getCurrentInstance().getExternalContext().getResponse();
	return response;
    }
}
