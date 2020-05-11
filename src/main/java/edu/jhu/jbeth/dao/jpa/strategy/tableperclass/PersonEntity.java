/**
 * 
 */
package edu.jhu.jbeth.dao.jpa.strategy.tableperclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;

import edu.jhu.jbeth.dao.StudentDao;
import edu.jhu.jbeth.dao.jpa.PersistantEntity;

/**
 * @author jordanbeth
 *
 */

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
public abstract class PersonEntity implements PersistantEntity {

    @Id
    @Column(name = StudentDao.USER_ID)
    protected String userId;
    @Column(name = StudentDao.PASSWORD)
    protected String password;
    @Column(name = StudentDao.FIRST_NAME)
    protected String firstName;
    @Column(name = StudentDao.LAST_NAME)
    protected String lastName;
    @Column(name = StudentDao.ADDRESS)
    protected String address;

    public PersonEntity() {
	// do nothing
    }

    public String getUserId() {
	return this.userId;
    }

    public void setUserId(String userId) {
	this.userId = userId;
    }

    /**
     * @return the firstName
     */
    public String getFirstName() {
	return this.firstName;
    }

    /**
     * @param firstName the firstName to set
     */
    public void setFirstName(String firstName) {
	this.firstName = firstName;
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
     * @return the address
     */
    public String getAddress() {
	return this.address;
    }

    /**
     * @param address the address to set
     */
    public void setAddress(String address) {
	this.address = address;
    }

    /**
     * @return the password
     */
    public String getPassword() {
	return this.password;
    }

    /**
     * @param password the password to set
     */
    public void setPassword(String password) {
	this.password = password;
    }

}
