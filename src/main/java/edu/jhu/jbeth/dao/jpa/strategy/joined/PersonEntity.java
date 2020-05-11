/**
 * 
 */
package edu.jhu.jbeth.dao.jpa.strategy.joined;

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

@Entity(name = "PersonEntityJoined")
@Inheritance(strategy = InheritanceType.JOINED)
public class PersonEntity implements PersistantEntity {

    @Id
    @Column(name = StudentDao.USER_ID)
    protected String userId;
    @Column(name = StudentDao.FIRST_NAME)
    protected String firstName;
    @Column(name = StudentDao.LAST_NAME)
    protected String lastName;
    @Column(name = StudentDao.ADDRESS)
    protected String address;
    @Column(name = StudentDao.SSN)
    protected String ssn;

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
     * @return the ssn
     */
    public String getSsn() {
	return this.ssn;
    }

    /**
     * @param ssn the ssn to set
     */
    public void setSsn(String ssn) {
	this.ssn = ssn;
    }
}
