package edu.jhu.jbeth.message;

import javax.jms.JMSException;
import javax.jms.MapMessage;

public class RegCourseTopicPayload {
    private static final String USER_ID = "User_ID";
    private static final String COURSE_ID = "Course_ID";
    private static final String COURSE_NAME = "Course_Name";
    private static final String DATE_OF_REGISTRATION = "Date_of_Registration";

    private final String userId;
    private final String courseId;
    private final String courseName;
    private final String dateOfReg;

    public RegCourseTopicPayload(MapMessage mapMessage) throws JMSException {
	this(mapMessage.getString(USER_ID), mapMessage.getString(COURSE_ID), mapMessage.getString(COURSE_NAME), mapMessage.getString(DATE_OF_REGISTRATION));
    }

    public RegCourseTopicPayload(String userId, String courseId, String courseName, String dateOfReg) throws JMSException {
	this.userId = userId;
	this.courseId = courseId;
	this.courseName = courseName;
	this.dateOfReg = dateOfReg;
    }

    public MapMessage toMapMessage(MapMessage msg) throws JMSException {
	// User_ID
	msg.setString(USER_ID, this.userId);
	// Course_ID
	msg.setString(COURSE_ID, this.courseId);
	// Course_Name
	msg.setString(COURSE_NAME, this.courseName);
	// Date_of_Registration
	msg.setString(DATE_OF_REGISTRATION, this.dateOfReg);
	return msg;
    }

    @Override
    public String toString() {
	StringBuilder builder = new StringBuilder();
	builder.append("RegPayload [userId=");
	builder.append(this.userId);
	builder.append(", courseId=");
	builder.append(this.courseId);
	builder.append(", courseName=");
	builder.append(this.courseName);
	builder.append(", dateOfReg=");
	builder.append(this.dateOfReg);
	builder.append("]");
	return builder.toString();
    }
}