/**
 * 
 */
package edu.jhu.jbeth.home.registrationlookup;

import java.io.IOException;
import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

import javax.annotation.PostConstruct;
import javax.enterprise.context.SessionScoped;
import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.inject.Named;
import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import edu.jhu.jbeth.AppConstants;
import edu.jhu.jbeth.bo.StudentBO;
import edu.jhu.jbeth.dao.DaoManager;
import edu.jhu.jbeth.dao.StudentDao;
import edu.jhu.jbeth.login.LoginBean;
import edu.jhu.jbeth.util.Logged;
import edu.jhu.jbeth.util.SessionHelper;

/**
 * @author jordanbeth
 *
 */

@Named("registrationLookupBean")
@SessionScoped
public class RegistrationLookupBean implements Serializable {
    private static final Logger logger = Logger.getLogger(RegistrationLookupBean.class.getSimpleName());

    private static final long serialVersionUID = 1L;

    private final StudentDao studentDao = DaoManager.studentDao();

    private String lastName;
    private String message;
    private String styleClass;
    private List<StudentBO> studentResults;

    public RegistrationLookupBean() {
	// Do nothing
    }

    @PostConstruct
    public void init() {
	this.message = "";
	this.styleClass = "";
	this.lastName = "";
	this.studentResults = null;
    }

    public static void logout() throws ServletException, IOException {
	SessionHelper.terminate(getSession());
	goTo(AppConstants.Pages.LOGIN_JSP);
    }

    private static void goTo(String page) throws ServletException, IOException {
	// go to login page
	HttpServletRequest request = getRequest();
	RequestDispatcher requestDispatcher = request.getRequestDispatcher(page);
	requestDispatcher.forward(request, getResponse());
    }

    @Logged
    public void lookup() throws IOException {
	logger.info("Looking up registration report by last name = [" + this.lastName + "]");

	if (this.lastName == null) {
	    logger.severe("Unable to lookup registration for null last name.");
	    return;
	}
	
	reset();

	// throws exception
	isLoggedIn();

	// run the report
	List<StudentBO> students = this.studentDao.findByLastName(this.lastName);
	if (students == null || students.isEmpty()) {
	    setError("Student not found.");
	    return;
	}

	this.studentResults = students;

	// Refresh
	ExternalContext ec = FacesContext.getCurrentInstance().getExternalContext();
	ec.redirect(((HttpServletRequest) ec.getRequest()).getRequestURI());
    }

    private void reset() {
	this.studentResults = null;
	this.message = null;
    }

    private static void isLoggedIn() {
	HttpSession session = getSession();
	LoginBean loginBean = SessionHelper.getOrCreateBean(session, LoginBean.class);
	String userId = loginBean.getUserId();
	if (userId == null) {
	    throw new RuntimeException("User is not logged in.");
	}
    }

    private void setError(String error) {
	this.message = error;
	this.styleClass = AppConstants.Styles.ERROR;
    }

    public static void home() throws ServletException, IOException {
	goTo(AppConstants.Pages.HOME_JSP);
    }

    public static void back() throws IOException {
	FacesContext.getCurrentInstance().getExternalContext().redirect(AppConstants.Pages.HOME_JSP);
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
     * @return the studentResults
     */
    public List<StudentBO> getStudentResults() {
        return this.studentResults;
    }
    
    /**
     * @return the lastName
     */
    public String getLastName() {
        return this.lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     * @param studentResults the studentResults to set
     */
    public void setStudentResults(List<StudentBO> studentResults) {
        this.studentResults = studentResults;
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
