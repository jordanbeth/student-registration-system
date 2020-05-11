package edu.jhu.jbeth.login;

import edu.jhu.jbeth.bo.StudentBO;

public class LoginBean {
    private String userId;
    private String password;
    private boolean success;
    private int attempts;

    private StudentBO studentBean;

    public LoginBean() {
	// do nothing
    }

    /**
     * @return the userId
     */
    public final String getUserId() {
	return this.userId;
    }

    /**
     * @param userName the userId to set
     */
    public final void setUserId(String userName) {
	this.userId = userName;
    }

    /**
     * @return the password
     */
    public final String getPassword() {
	return this.password;
    }

    /**
     * @param password the password to set
     */
    public final void setPassword(String password) {
	this.password = password;
    }

    /**
     * @return the success
     */
    public final boolean isSuccess() {
	return this.success;
    }

    /**
     * @param success the success to set
     */
    public final void setSuccess() {
	this.success = true;
    }

    /**
     * @return the studentBean
     */
    public final StudentBO getStudentBean() {
	return this.studentBean;
    }

    /**
     * @param studentBean the userBean to set
     */
    public final void setStudentBean(StudentBO studentBean) {
	this.studentBean = studentBean;
    }

    public final int attemptLogin() {
	this.attempts += 1;
	return this.attempts;
    }

    public void reset() {
	setPassword(null);
	setUserId(null);
	setStudentBean(null);
	this.success = false;
    }

}
