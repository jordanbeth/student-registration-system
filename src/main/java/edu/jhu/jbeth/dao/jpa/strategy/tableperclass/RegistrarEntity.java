/**
 * 
 */
package edu.jhu.jbeth.dao.jpa.strategy.tableperclass;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import edu.jhu.jbeth.dao.CourseDao;

/**
 * @author jordanbeth
 *
 */
@Entity
@Table(name = CourseDao.REGISTRAR_TABLE)
public class RegistrarEntity extends CourseRegistration {
    private static final long serialVersionUID = 1L;

    @Column(name = CourseDao.NUMBER_REGISTERED)
    private int numRegistered;

    public RegistrarEntity() {
	// The class must have a public or protected, no-argument constructor. The class
	// may have other constructors.
    }

    /**
     * @return the numberRegistered
     */
    public int getNumberRegistered() {
	return this.numRegistered;
    }

    /**
     * @param numberRegistered the numberRegistered to set
     */
    public void setNumberRegistered(int numberRegistered) {
	this.numRegistered = numberRegistered;
    }

    @Override
    public String toString() {
	return "RegistrarEntity [numRegistered=" + this.numRegistered + ", courseId=" + this.courseId + "]";
    }

}
