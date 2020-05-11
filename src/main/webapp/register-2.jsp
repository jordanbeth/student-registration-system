<%@ page language="java" contentType="text/html; charset=UTF-8"
	pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="edu.jhu.jbeth.register.RegistrationBean"%>
<%@page import="edu.jhu.jbeth.util.SessionHelper"%>
<%@page import="edu.jhu.jbeth.AppConstants"%>
<!DOCTYPE html>
<html>

<head>
<%@ include file="head.jsp"%>
</head>

<body>

	<div class="body-container">
		<div class="container center-lg-form-div">
			<%
				RegistrationBean bean = SessionHelper.getOrCreateBean(request.getSession(), RegistrationBean.class);
			%>
			<p class="text-center">
				Thanks,
				<%=bean.getFirstName()%>!
			</p>
			<%
				
			%>

			<p class="text-center">Please fill out the rest of the
				information below to complete your registration.</p>
			<form action="/StudentRegistrationSystem/register" method="POST">
				<h3 class="form-header text-center">Registration Form B</h3>
				<div class="form-group row w-75 auto-side">
					<label class="col-sm-3 col-form-label text-right" for="address">Address:</label>
					<div class="col-sm-9">
						<input class="form-control" type="text" name="address">
					</div>
				</div>
				<div class="form-group row w-75 auto-side">
					<label class="col-sm-3 col-form-label text-right">City,
						State:</label>

					<div class="col-sm-4">
						<input class="form-control" type="text" name="city">
					</div>
					<div class="col-sm-1">,</div>
					<div class="col-sm-4">
						<select name="state">
							<option value="" disabled selected>--</option>
							<option value="AL">Alabama</option>
							<option value="AK">Alaska</option>
							<option value="AZ">Arizona</option>
							<option value="AR">Arkansas</option>
							<option value="CA">California</option>
							<option value="CO">Colorado</option>
							<option value="CT">Connecticut</option>
							<option value="DE">Delaware</option>
							<option value="DC">District Of Columbia</option>
							<option value="FL">Florida</option>
							<option value="GA">Georgia</option>
							<option value="HI">Hawaii</option>
							<option value="ID">Idaho</option>
							<option value="IL">Illinois</option>
							<option value="IN">Indiana</option>
							<option value="IA">Iowa</option>
							<option value="KS">Kansas</option>
							<option value="KY">Kentucky</option>
							<option value="LA">Louisiana</option>
							<option value="ME">Maine</option>
							<option value="MD">Maryland</option>
							<option value="MA">Massachusetts</option>
							<option value="MI">Michigan</option>
							<option value="MN">Minnesota</option>
							<option value="MS">Mississippi</option>
							<option value="MO">Missouri</option>
							<option value="MT">Montana</option>
							<option value="NE">Nebraska</option>
							<option value="NV">Nevada</option>
							<option value="NH">New Hampshire</option>
							<option value="NJ">New Jersey</option>
							<option value="NM">New Mexico</option>
							<option value="NY">New York</option>
							<option value="NC">North Carolina</option>
							<option value="ND">North Dakota</option>
							<option value="OH">Ohio</option>
							<option value="OK">Oklahoma</option>
							<option value="OR">Oregon</option>
							<option value="PA">Pennsylvania</option>
							<option value="RI">Rhode Island</option>
							<option value="SC">South Carolina</option>
							<option value="SD">South Dakota</option>
							<option value="TN">Tennessee</option>
							<option value="TX">Texas</option>
							<option value="UT">Utah</option>
							<option value="VT">Vermont</option>
							<option value="VA">Virginia</option>
							<option value="WA">Washington</option>
							<option value="WV">West Virginia</option>
							<option value="WI">Wisconsin</option>
							<option value="WY">Wyoming</option>
						</select>
					</div>
				</div>

				<div class="form-group row w-75 auto-side">
					<label class="col-sm-3 col-form-label text-right" for="zip_code">Zip/Postal
						Code:</label>
					<div class="col-sm-9">
						<input class="form-control" type="text" name="zip_code">
					</div>
				</div>

				<input type="hidden" name="<%=AppConstants.Request.HIDDEN%>"
					value="<%=AppConstants.Request.NEW_USER_REGISTRATION%>" />

				<div class="form-group row w-75 auto-side">
					<div class="col-sm-3"></div>
					<div class="col-sm-9">
						<input class="btn btn-secondary" type="submit" name="reset"
							value="Reset"> <input class="btn btn-primary"
							type="submit" name="register" value="Register">
					</div>
				</div>

				<%@ include file="errors.jsp"%>

			</form>

		</div>
	</div>
</body>

</html>