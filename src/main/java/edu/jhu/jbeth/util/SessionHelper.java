/**
 * 
 */
package edu.jhu.jbeth.util;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

/**
 * @author jordanbeth
 *
 */
public class SessionHelper {

    private SessionHelper() {
    }

    public static void terminate(HttpServletRequest request) {
	HttpSession session = request.getSession();
	terminate(session);
    }

    public static void terminate(HttpSession session) {
	session.invalidate();
    }

    public static <T> T getOrCreateBean(HttpSession session, Class<T> clazz) {
	String simpleName = clazz.getSimpleName();
	T bean = (T) session.getAttribute(simpleName);
	if (bean == null) {
	    try {
		bean = clazz.newInstance();
	    } catch (InstantiationException | IllegalAccessException e) {
		e.printStackTrace();
	    }

	    session.setAttribute(simpleName, bean);
	}

	return bean;
    }

    public static <T> T getAttribute(HttpSession session, String attributeName) {
	T attribute = (T) session.getAttribute(attributeName);
	return attribute;
    }

    public static <T> List<T> getListAttribute(HttpSession session, String attributeName) {
	List<T> list = (List<T>) session.getAttribute(attributeName);
	return list;
    }

}
