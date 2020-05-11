package edu.jhu.jbeth.register;

/**
 * Java Bean for registration.
 * 
 * @author jordanbeth
 *
 */
public class RegistrationBean {

    private String userId;
    private String password;
    private String firstName;
    private String lastName;
    private String email;
    private String address;
    private String city;
    private String state;
    private String zipCode;
    private String ssn;

    private boolean isComplete;

    public RegistrationBean() {
	// do nothing
    }

    /**
     * @return the userId
     */
    public final String getUserId() {
	return this.userId;
    }

    /**
     * @param userId the userId to set
     */
    public final void setUserId(String userId) {
	this.userId = userId;
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
     * @return the firstName
     */
    public final String getFirstName() {
	return this.firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public final void setFirstName(String firstName) {
	this.firstName = firstName;
    }

    /**
     * @return the lastName
     */
    public final String getLastName() {
	return this.lastName;
    }

    /**
     * @param lastName the lastName to set
     */
    public final void setLastName(String lastName) {
	this.lastName = lastName;
    }

    /**
     * @return the email
     */
    public final String getEmail() {
	return this.email;
    }

    /**
     * @param email the email to set
     */
    public final void setEmail(String email) {
	this.email = email;
    }

    /**
     * @return the address
     */
    public final String getAddress() {
	return this.address;
    }

    /**
     * @param address the address to set
     */
    public final void setAddress(String address) {
	this.address = address;
    }

    /**
     * @return the city
     */
    public final String getCity() {
	return this.city;
    }

    /**
     * @param city the city to set
     */
    public final void setCity(String city) {
	this.city = city;
    }

    /**
     * @return the state
     */
    public final String getState() {
	return this.state;
    }

    /**
     * @param state the state to set
     */
    public final void setState(String state) {
	this.state = state;
    }

    /**
     * @return the zipCode
     */
    public final String getZipCode() {
	return this.zipCode;
    }

    /**
     * @param zipCode the zipCode to set
     */
    public final void setZipCode(String zipCode) {
	this.zipCode = zipCode;
    }

    /**
     * @return ssn the ssn to set
     */
    public void setSsn(String fullSsn) {
	this.ssn = fullSsn;
    }

    /**
     * @return the ssn
     */
    public String getSsn() {
	return this.ssn;
    }

    public boolean isComplete() {
	return this.isComplete;
    }

    public void complete() {
	this.isComplete = true;
    }

    public void reset() {
	this.userId = null;
	this.password = null;
	this.firstName = null;
	this.lastName = null;
	this.email = null;
	this.address = null;
	this.city = null;
	this.state = null;
	this.zipCode = null;
	this.ssn = null;
	this.isComplete = false;
    }

}
