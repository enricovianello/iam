'use strict';

angular.module('registrationApp').controller('RequestManagementController', RequestManagementController);

RequestManagementController.$inject = ['$scope', 'RegistrationRequestService'];

function RequestManagementController($scope, RegistrationRequestService){
	var vm = this;
	vm.operationResult;
	vm.textAlert;
	
	vm.listRequests = listRequests;
	vm.approveRequest = approveRequest;
	vm.rejectRequest = rejectRequest;

	vm.list = [];
	vm.sortType = 'status';
	vm.sortReverse = false;
	

	function listRequests(status) {
		RegistrationRequestService.listRequests(status).then(
				function(result) {
					vm.list = result.data;
				},
				function(errResponse) {
					vm.textAlert = errResponse.data.error_description || errResponse.data.detail;
					vm.operationResult = 'err';
				})
	};

	function approveRequest(uuid) {
		RegistrationRequestService.updateRequest(uuid, 'APPROVED').then(
				function() {
					vm.textAlert = "Approvation success";
					vm.operationResult = 'ok';
					vm.listRequests();
				},
				function(errResponse) {
					vm.textAlert = errResponse.data.error_description || errResponse.data.detail;
					vm.operationResult = 'err';
				})
	};

	function rejectRequest(uuid) {
		RegistrationRequestService.updateRequest(uuid, 'REJECTED').then(
				function() {
					vm.textAlert = "Operation success";
					vm.operationResult = 'ok';
					vm.listRequests();
				},
				function(errResponse) {
					vm.textAlert = errResponse.data.error_description || errResponse.data.detail;
					vm.operationResult = 'err';
				})
	};

};

angular.module('registrationApp').controller('RegistrationController', RegistrationController);

RegistrationController.$inject = [ '$scope', '$uibModalInstance', '$window', 'RegistrationRequestService' ];

function RegistrationController($scope, $uibModalInstance, $window, RegistrationRequestService) {
	$scope.user = {
		schemas : [ "urn:ietf:params:scim:schemas:core:2.0:User",
				"urn:indigo-dc:scim:schemas:IndigoUser" ],
		name : {
			givenName : '',
			familyName : '',
		},
		active : "false",
		userName : '',
		emails : [ {
			type : "work",
			value : '',
			primary : "true",
		} ],
	};

	$scope.list = [];
	$scope.sortType = 'status';
	$scope.sortReverse = false;
	
	$scope.textAlert;
	$scope.operationResult;

	$scope.createUser = function(user) {
		RegistrationRequestService.create(user).then(
				function() {
					$window.location.href = "/registration/submitted";
				},
				function(errResponse) {
					$scope.operationResult = 'err';
					$scope.textAlert = errResponse.data.error_description || errResponse.data.detail;
					return $q.reject(errResponse);
				})
	};

	$scope.submit = function() {
		$scope.createUser($scope.user);
	};

	$scope.reset = function() {
		$scope.user.name = {
			givenName : '',
			familyName : '',
		};
		$scope.user.userName = '';
		$scope.user.emails = [ {
			type : "work",
			value : '',
			primary : "true",
		} ];
		$scope.registrationForm.$setPristine();
	};

	$scope.dismiss = function() {
		$uibModalInstance.close();
	};
};

angular.module('registrationApp').controller('RegistrationFormModalController', RegistrationFormModalController);

RegistrationFormModalController.$inject = [ '$scope', '$uibModal' ];

function RegistrationFormModalController($scope, $uibModal) {
	$scope.open = function() {
		$uibModal.open({
			templateUrl : "/resources/iam/template/registration.html",
			controller : "RegistrationController",
			size : '600px',
			animation : true
		});
	};
};