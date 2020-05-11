package edu.jhu.jbeth.message;

import edu.jhu.jbeth.bo.CourseBO;

public interface MessageProducer {
    void sendRegistrationMessage(String userId, CourseBO course) throws Exception;
}