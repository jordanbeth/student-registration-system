<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.jhu.jbeth.util.SessionHelper"%>
<%@page import="edu.jhu.jbeth.bo.CourseBO"%>
<%@page import="edu.jhu.jbeth.AppConstants"%>
<!DOCTYPE html>
<html>

<head>
<%@ include file="head.jsp"%>
</head>

<body>

	<div class="top-bar">
		<h1>Student Registration Site</h1>
	</div>

	<div class="container container-fluid body-container">
		<div class="container text-center">
			<div class="header">
				<h1>Registration Status Report</h1>
			</div>
		</div>


		<div class="content">
			<div class="container">

				<table class="table table-hover">
					<thead>
						<tr>
							<th scope="col">#</th>
							<th scope="col">Course Id</th>
							<th scope="col">Course Name</th>
							<th scope="col">Number Registered</th>
						</tr>
					</thead>
					<%
					    List<CourseBO> courseList = SessionHelper.getListAttribute(request.getSession(),
																	AppConstants.Session.SELECTED_COURSE_LIST);
															if (courseList != null) {

																for (int i = 0; i < courseList.size(); i++) {
																	CourseBO course = courseList.get(i);
																	String courseId = course.getCourseId();
																	String courseName = course.getCourseTitle();
																	int numRegistered = course.getNumRegistered();
					%>
					<tbody>
						<tr>
							<th scope="row"><%=i + 1%></th>
							<td><%=courseId%></td>
							<td><%=courseName%></td>
							<td><%=numRegistered%></td>
						</tr>
					</tbody>
					<%
						}
							}
					%>
				</table>

				<%@ include file="errors.jsp"%>

				<div class="form-group row">
					<a class="btn btn-primary auto"
						href="/StudentRegistrationSystem/<%=AppConstants.Pages.COURSE_REG_STATUS_JSP%>">Back</a>
				</div>

			</div>
		</div>
	</div>

	<div id="footer">SRS &copy; 2020</div>

</body>

</html>