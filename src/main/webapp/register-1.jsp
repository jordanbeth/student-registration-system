<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.jhu.jbeth.register.RegistrationBean"%>
<%@page import="edu.jhu.jbeth.util.SessionHelper"%>
<%@page import="edu.jhu.jbeth.AppConstants"%>
<%@ page session="true"%>
<!DOCTYPE html>
<html>

<head>
<%@ include file="head.jsp"%>
</head>

<body>

	<div class="body-container">
		<div class="container text-center">
			<h2>Student Registration System</h2>
		</div>

		<%
			RegistrationBean bean = SessionHelper.getOrCreateBean(request.getSession(), RegistrationBean.class);
		%>

		<div class="container center-lg-form-div">
			<h3 class="form-header text-center">Registration Form A</h3>

			<form action="/StudentRegistrationSystem/register" method="POST">

				<div class="form-group row w-75 auto-side">
					<label class="col-sm-3 col-form-label text-right" for="userId">User
						Id:</label>
					<div class="col-sm-6">
						<input class="form-control" type="text" name="user_id"
							value="${bean.userId}" />
					</div>
				</div>

				<div class="form-group row w-75 auto-side">
					<label class="col-sm-3 col-form-label text-right" for="pwd">Password:</label>
					<div class="col-sm-6">
						<input class="form-control" type="password" name="pwd" />
					</div>
				</div>

				<div class="form-group row w-75 auto-side">
					<label class="col-sm-3 col-form-label text-right" for="pwd">Password
						(repeat):</label>
					<div class="col-sm-6">
						<input class="form-control" type="password" name="pwd" />
					</div>
				</div>

				<div class="form-group row w-75 auto-side">
					<label class="col-sm-3 col-form-label text-right" for="first_name">First
						Name:</label>
					<div class="col-sm-6">
						<input class="form-control" type="text" name="first_name"
							value="${bean.firstName}">
					</div>
				</div>

				<div class="form-group row w-75 auto-side">
					<label class="col-sm-3 col-form-label text-right" for="last_name">Last
						Name:</label>
					<div class="col-sm-6">
						<input class="form-control" type="text" name="last_name"
							value="${bean.lastName}">
					</div>
				</div>

				<div class="form-group row w-75 auto-side">
					<label class="col-sm-3 col-form-label text-right" for="ssn">SSN:</label>
					<div class="col-sm-2">
						<input class="form-control" type="text" name="ssn" />
					</div>
					---
					<div class="col-sm-2">
						<input class="form-control" type="text" name="ssn" />
					</div>
					---
					<div class="col-sm-2">
						<input class="form-control" type="text" name="ssn" />
					</div>
				</div>

				<div class="form-group row w-75 auto-side">
					<label class="col-sm-3 col-form-label text-right" for="email">Email:</label>
					<div class="col-sm-6">
						<input class="form-control" type="text" name="email"
							value="${bean.email}">
					</div>
				</div>

				<div class="form-group row w-75 auto-side">
					<div class="col-sm-3 col-form-label"></div>
					<div class="col-sm-6">
						<input class="btn btn-primary" type="submit" name="continue"
							value="Continue">
					</div>
				</div>
			</form>

			<%@ include file="errors.jsp"%>
		</div>


	</div>

</body>

</html>