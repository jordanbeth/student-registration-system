package edu.jhu.jbeth.message;

import java.time.LocalDate;
import java.util.Properties;
import java.util.logging.Logger;

import javax.jms.DeliveryMode;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.Session;
import javax.jms.Topic;
import javax.jms.TopicConnection;
import javax.jms.TopicConnectionFactory;
import javax.jms.TopicPublisher;
import javax.jms.TopicSession;
import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import edu.jhu.jbeth.AppConstants;
import edu.jhu.jbeth.bo.CourseBO;

public class JMSMessageProducer implements MessageProducer {
    private static final Logger logger = Logger.getLogger(JMSMessageProducer.class.getSimpleName());

    public static JMSMessageProducer getInstance() {
	return INSTANCE;
    }

    private static final JMSMessageProducer INSTANCE = new JMSMessageProducer();

    private final TopicConnection topicConnection;
    private final InitialContext namingContext;
    private final TopicPublisher topicPublisher;
    private final TopicSession topicSession;

    private JMSMessageProducer() {
	// Set up the namingContext for the JNDI lookup
	Properties env = new Properties();
	env.put(Context.INITIAL_CONTEXT_FACTORY, AppConstants.Message.INITIAL_CONTEXT_FACTORY);
	env.put(Context.PROVIDER_URL, System.getProperty(Context.PROVIDER_URL, AppConstants.Message.PROVIDER_URL));

	try {
	    this.namingContext = new InitialContext(env);

	    // Perform the JNDI lookups
	    String connectionFactoryString = System.getProperty("connection.factory", AppConstants.Message.DEFAULT_CONNECTION_FACTORY);
	    // lookup the topic
	    Topic topic = (Topic) this.namingContext.lookup(AppConstants.Message.REG_COURSE_TOPIC);
	    // lookup the topic connection factory
	    TopicConnectionFactory topicConnectionFactory = (TopicConnectionFactory) this.namingContext.lookup(connectionFactoryString);
	    this.topicConnection = topicConnectionFactory.createTopicConnection(AppConstants.Message.USERNAME, AppConstants.Message.PASSWORD);
	    // create a topic session
	    this.topicSession = this.topicConnection.createTopicSession(false, Session.AUTO_ACKNOWLEDGE);
	    // create a topic publisher
	    this.topicPublisher = this.topicSession.createPublisher(topic);
	    this.topicPublisher.setDeliveryMode(DeliveryMode.PERSISTENT);
	} catch (NamingException e) {
	    e.printStackTrace();
	    throw new RuntimeException(e.getMessage());
	} catch (JMSException e) {
	    e.printStackTrace();
	    throw new RuntimeException(e.getMessage());
	}
    }

    @Override
    public void sendRegistrationMessage(String userId, CourseBO course) throws JMSException {
	RegCourseTopicPayload payload = new RegCourseTopicPayload(userId, course.getCourseId(), course.getCourseTitle(), LocalDate.now().toString());
	MapMessage message = payload.toMapMessage(this.topicSession.createMapMessage());
	sendMessage(message);
    }

    private void sendMessage(Message message) {
	try {
	    // publish the message
	    this.topicPublisher.publish(message);
	} catch (Exception e) {
	    e.printStackTrace();
	}
    }

    @Override
    protected void finalize() throws Throwable {
	if (this.namingContext != null) {
	    try {
		this.namingContext.close();
	    } catch (NamingException e) {
		e.printStackTrace();
	    }
	}

	if (this.topicConnection != null) {
	    try {
		this.topicConnection.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
	
	if (this.topicPublisher != null) {
	    try {
		this.topicPublisher.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}	
	
	if (this.topicSession != null) {
	    try {
		this.topicSession.close();
	    } catch (Exception e) {
		e.printStackTrace();
	    }
	}
    }
}
