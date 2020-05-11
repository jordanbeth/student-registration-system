<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>

<%@page import="java.util.List"%>
<%@page import="edu.jhu.jbeth.util.SessionHelper"%>
<%@page import="edu.jhu.jbeth.AppConstants"%>


<%
	List<String> errors = SessionHelper.getListAttribute(request.getSession(), AppConstants.Session.ERRORS);
	if (errors != null) {
%>

<ul>
	<%
		for (String error : errors) {
	%>
	<li class="text-danger"><%=error%></li>

	<%
		}
	%>
</ul>
<%
	}
%>
