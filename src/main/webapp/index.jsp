<%@page import="edu.jhu.jbeth.AppConstants"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.jhu.jbeth.login.LoginBean"%>
<%@page import="edu.jhu.jbeth.util.SessionHelper"%>
<!DOCTYPE html>
<html>

<head>
<%@ include file="head.jsp"%>
</head>

<body>

	<%
		Boolean isbreached = SessionHelper.getAttribute(request.getSession(),
				AppConstants.Session.MAX_ATTEMPT_BREACHED);
		if (isbreached != null && isbreached) {
			request.getSession().invalidate();
	%>

	<div class="container container-fluid body-container">
		<div class="container text-center">
			<br />
			<h3>You have reached the maximum number of login attempts.</h3>
			<h3>Your session has been terminated.</h3>
		</div>
	</div>

	<%
		} else {
			LoginBean bean = SessionHelper.getOrCreateBean(request.getSession(), LoginBean.class);
	%>

	<div class="top-bar">
		<h1>Welcome to the Student Registration Site</h1>
	</div>
	<div class="container container-fluid body-container">

		<div class="container center-md-form-div">
			<h3 class="register-header">If you already have an account,
				please log in</h3>
			<form action="/StudentRegistrationSystem/login" method="POST">

				<div class="form-group row">
					<label class="col-sm-3 col-form-label text-right" for="userId">User
						Id:</label>
					<div class="col-sm-9">
						<input class="form-control login-field" type="text" name="user_id"
							value="${bean.userId}" />
					</div>
				</div>

				<div class="form-group row">
					<label class="col-sm-3 col-form-label text-right" for="pwd">Password:</label>
					<div class="col-sm-9">
						<input class="form-control login-field" type="password" name="pwd"
							value="${bean.password}" />
					</div>
				</div>

				<div class="form-group row">

					<div class="col-sm-12 text-center">
						<input class="btn btn-primary" type="submit" name="login"
							value="Login"> <input class="btn btn-secondary"
							type="submit" name="reset" value="Reset">
					</div>
				</div>

				<%@ include file="errors.jsp"%>
			</form>
		</div>

		<div class="container form-div text-center">
			<h3 class="register-header">For new users, please register first</h3>
			<a class="btn btn-primary"
				href="/StudentRegistrationSystem/register1.jsp">Register</a>
		</div>
	</div>

	<div id="footer">SRS &copy; 2020</div>
	<%
		}
	%>
</body>

</html>