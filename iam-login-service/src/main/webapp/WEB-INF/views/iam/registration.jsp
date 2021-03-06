<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<html>
<head>
<title>Registration Form</title>
<base href="<c:url value='/'/>">

<link rel="stylesheet"
	href="<c:url value='/webjars/bootstrap/css/bootstrap.min.css'/>"></link>
  
<link href="<c:url value='/resources/css/registration_app.css' />"
	rel="stylesheet"></link>
</head>

<body ng-app="registrationApp" class="ng-cloak">
	<div class="generic-container"
		ng-controller="RegistrationController as ctrl">
		<div class="panel panel-default">
			<div class="panel-heading">
				<span class="lead">User Registration Form </span>
			</div>
			<div class="formcontainer">
				<form ng-submit="ctrl.submit()" name="registrationForm" class="form-horizontal">
					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="givenname">First Name</label>
							<div class="col-md-7">
								<input type="text" ng-model="ctrl.user.name.givenName"
									id="givenname" class="username form-control input-sm"
									placeholder="First name" required ng-minlength="3" />
								<div class="has-error" ng-show="registrationForm.$dirty">
									<span ng-show="registrationForm.givenname.$error.required"> This is a required field</span>
									<span ng-show="registrationForm.givenname.$error.minlength">Minimum length required is 3</span>
									<span ng-show="registrationForm.givenname.$invalid">This field is invalid</span>
								</div>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="familyname">Family Name</label>
							<div class="col-md-7">
								<input type="text" ng-model="ctrl.user.name.familyName"
									id="familyName" class="username form-control input-sm"
									placeholder="Family name" required ng-minlength="3" />
								<div class="has-error" ng-show="registrationForm.$dirty">
									<span ng-show="registrationForm.familyname.$error.required"> This is a required field</span>
									<span ng-show="registrationForm.familyname.$error.minlength">Minimum length required is 3</span>
									<span ng-show="registrationForm.familyname.$invalid">This field is invalid</span>
								</div>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="email">Email</label>
							<div class="col-md-7">
								<input type="email" ng-model="ctrl.user.emails[0].value" id="email"
									class="email form-control input-sm"
									placeholder="Email" required />
								<div class="has-error" ng-show="registrationForm.$dirty">
									<span ng-show="registrationForm.email.$error.required">This is a required field</span>
									<span ng-show="registrationForm.email.$invalid">This field is invalid </span>
								</div>
							</div>
						</div>
					</div>
					
					<div class="row">
						<div class="form-group col-md-12">
							<label class="col-md-2 control-lable" for="username">Username</label>
							<div class="col-md-7">
								<input type="text" ng-model="ctrl.user.userName"
									id="userName" class="username form-control input-sm"
									placeholder="Username" required ng-minlength="3" />
								<div class="has-error" ng-show="registrationForm.$dirty">
									<span ng-show="registrationForm.usernname.$error.required"> This is a required field</span>
									<span ng-show="registrationForm.username.$error.minlength">Minimum length required is 3</span>
									<span ng-show="registrationForm.username.$invalid">This field is invalid</span>
								</div>
							</div>
						</div>
					</div>

					<div class="row">
						<div class="form-actions floatRight">
							<input type="submit" value="Register" class="btn btn-primary btn-sm" ng-disabled="registrationForm.$invalid">
							<button type="button" ng-click="ctrl.reset()" class="btn btn-warning btn-sm" ng-disabled="registrationForm.$pristine">Reset Form</button>
						</div>
					</div>
				</form>
			</div>
		</div>
	</div>

	<script
    type="text/javascript"
		src="<c:url value='/webjars/angularjs/angular.min.js'/>"></script>
 
	<script src="<c:url value='/resources/js/registration_app.js' />"></script>
	<script
		src="<c:url value='/resources/js/service/registration_service.js' />"></script>
	<script
		src="<c:url value='/resources/js/controller/registration_controller.js' />"></script>
</body>
</html>