<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.jhu.jbeth.util.SessionHelper"%>
<%@page import="edu.jhu.jbeth.bo.CourseBO"%>
<%@page import="edu.jhu.jbeth.AppConstants"%>
<!DOCTYPE html>
<html>

<head>
<meta charset="UTF-8">
<link rel="stylesheet" type="text/css" href="css/bootstrap.css" />
<link rel="stylesheet" type="text/css" href="css/main.css" />
<title>SRS</title>
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



		<div class="content center-md-form-div">
			<p>Check any specific course(s) and click Submit:</p>

			<div class="container status-report-container">
				<%
				    List<CourseBO> courseList = SessionHelper.getListAttribute(request.getSession(),
											AppConstants.Session.COURSE_LIST);
				%>
				<form action="/StudentRegistrationSystem/register" method="POST">
					<%
					    for (CourseBO course : courseList) {

												String courseNumber = course.getCourseId();
					%>
					<div class="form-group row">
						<div class="form-check">
							<input class="form-check-input" type="checkbox"
								name="<%=AppConstants.Request.SELECTED_COURSES%>"
								id="<%=courseNumber%>" value="<%=courseNumber%>" /> <label
								class="form-check-label" for="<%=courseNumber%>"> <%=course.toString()%>
							</label>
						</div>
					</div>
					<%
						}
					%>

					<input type="hidden" name="<%=AppConstants.Request.HIDDEN%>"
						value="<%=AppConstants.Request.REGISTRATION_STATUS_REPORT%>" />

					<div class="form-group row">

						<div class="auto">
							<button class="btn btn-primary" type="Submit" name="submit"
								value="Submit">Submit</button>

							<a class="btn btn-secondary"
								href="/StudentRegistrationSystem/<%=AppConstants.Pages.HOME_JSP%>">Home</a>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<div id="footer">SRS &copy; 2020</div>

</body>

</html>