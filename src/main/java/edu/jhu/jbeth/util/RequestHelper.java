/**
 * 
 */
package edu.jhu.jbeth.util;

import javax.servlet.http.HttpServletRequest;

/**
 * @author jordanbeth
 *
 */
public class RequestHelper {

    private RequestHelper() {
	// do nothing
    }

    public static boolean hasParameterValue(HttpServletRequest request, final String value) {
	String[] parameterValues = request.getParameterValues(value);
	if (parameterValues != null && parameterValues.length == 1) {
	    if (value.equalsIgnoreCase(parameterValues[0])) {
		return true;
	    }
	}

	return false;
    }

    public static boolean hasParameter(HttpServletRequest request, final String name) {
	String parameterValue = request.getParameter(name);
	if (parameterValue != null) {
	    return true;
	}

	return false;
    }
}
