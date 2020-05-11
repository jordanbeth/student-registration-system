package edu.jhu.jbeth.message;

import java.util.logging.Logger;

import javax.ejb.ActivationConfigProperty;
import javax.ejb.MessageDriven;
import javax.jms.JMSException;
import javax.jms.MapMessage;
import javax.jms.Message;
import javax.jms.MessageListener;

/**
 * 
 * A Message Driven Bean that receives and processes the messages that are sent
 * to the topic "topic/RegCourseTopic".
 */
@MessageDriven(name = "MessageBean", activationConfig = { @ActivationConfigProperty(propertyName = "destinationLookup", propertyValue = "topic/RegCourseTopic"),
	@ActivationConfigProperty(propertyName = "destinationType", propertyValue = "javax.jms.Topic"),
	@ActivationConfigProperty(propertyName = "acknowledgeMode", propertyValue = "Auto-acknowledge") })
public class MessageBean implements MessageListener {
    private static final Logger logger = Logger.getLogger(MessageBean.class.toString());

    public void onMessage(Message message) {
	try {
	    if (message instanceof MapMessage) {
		MapMessage mapMessage = (MapMessage) message;
		RegCourseTopicPayload payload = new RegCourseTopicPayload(mapMessage);
		logger.info(payload.toString());
	    }
	} catch (JMSException e) {
	    throw new RuntimeException(e);
	}
    }

}