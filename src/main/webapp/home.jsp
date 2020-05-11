<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="edu.jhu.jbeth.bo.StudentBO"%>
<%@page import="edu.jhu.jbeth.login.LoginBean"%>
<%@page import="edu.jhu.jbeth.util.SessionHelper"%>
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
	<%
	    LoginBean loginBean = SessionHelper.getOrCreateBean(request.getSession(), LoginBean.class);
			StudentBO studentBean = loginBean.getStudentBean();
			if (studentBean == null) {
	%>

	<div class="container container-fluid body-container">
		<div class="container text-center">
			<h3>Wrong User Id and/or Password</h3>
			<h3>Please try to login again or register</h3>
		</div>

		<div class="container center-sm-form-div">
			<h5>Select your next action:</h5>
			<form action="/StudentRegistrationSystem/login" method="POST">
				<div class="form-group">
					<div class="form-check">
						<input class="form-check-input" id="loginRadio" type="radio"
							name="<%=AppConstants.Request.BAD_LOGIN%>"
							value="<%=AppConstants.Request.LOGIN%>" checked> <label
							class="form-check-label" for="loginRadio"> Login </label>
					</div>
				</div>
				<div class="form-group">
					<div class="form-check">
						<input class="form-check-input" id="registerRadio" type="radio"
							name="<%=AppConstants.Request.BAD_LOGIN%>"
							value="<%=AppConstants.Request.REGISTER%>"> <label
							class="form-check-label" for="registerRadio"> Register to
							SRS </label>
					</div>
				</div>
				<div class="form-group row home-submit-btn">
					<button type="submit" class="btn btn-primary">Submit</button>
				</div>

			</form>
		</div>
	</div>

	<%
		} else {
	%>

	<div class="container container-fluid body-container">
		<div class="container center-sm-form-div">
			<h5>Select your next action:</h5>
			<form action="/StudentRegistrationSystem/home" method="POST">
				<div class="form-group">
					<div class="form-check">
						<input class="form-check-input" id="registerRadio" type="radio"
							name="<%=AppConstants.Request.ACTION%>"
							value="<%=AppConstants.Request.COURSES%>" checked> <label
							class="form-check-label" for="registerRadio"> Course Registration </label>
					</div>
				</div>
				<div class="form-group">
					<div class="form-check">
						<input class="form-check-input" id="registrationStatusReport"
							type="radio" name="<%=AppConstants.Request.ACTION%>"
							value="<%=AppConstants.Request.REGISTRATION_STATUS_REPORT%>">
						<label class="form-check-label" for="registrationStatusReport">
							Registration Status Report </label>
					</div>
				</div>

				<div class="form-group">
					<div class="form-check">
						<input class="form-check-input"
							id="studentRegistrationStatusReport" type="radio"
							name="<%=AppConstants.Request.ACTION%>"
							value="<%=AppConstants.Request.STUDENT_REGISTRATION_STATUS_REPORT%>">
						<label class="form-check-label"
							for="studentRegistrationStatusReport"> Student
							Registration Lookup </label>
					</div>
				</div>
				<div class="form-group">
					<div class="form-check">
						<input class="form-check-input" id="logoutRadio" type="radio"
							name="<%=AppConstants.Request.ACTION%>"
							value="<%=AppConstants.Request.LOGOUT%>"> <label
							class="form-check-label" for="logoutRadio"> Logout </label>
					</div>
				</div>
				<div class="form-group row home-submit-btn">
					<button type="submit" class="btn btn-primary">Submit</button>
				</div>

			</form>
		</div>
	</div>
	<%
		}
	%>

	<div id="footer">SRS &copy; 2020</div>
</body>

</html>