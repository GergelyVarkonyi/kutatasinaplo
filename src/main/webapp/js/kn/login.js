var app = angular.module('knApp');

app.controller('AuthController', [ '$scope', '$rootScope', '$http', function($scope, $rootScope, $http) {
	
	$scope.validationMap = {'username':{'valid': true, 'msg': ''},
							'pwd':{'valid': true, 'msg': ''}};

	$scope.login = function() {
		var name = $("#username").val(); 
		var pwd =  $("#pwd").val();
		var validationError = false;
		
		if(!name) {
			$scope.validationMap.username.valid=false;
			$scope.validationMap.username.msg='Username is requeired.';
			validationError = true;
		} else {
			$scope.validationMap.username.valid=true;
		}
		if(!pwd) {
			$scope.validationMap.pwd.valid=false;
			$scope.validationMap.pwd.msg='Password is requeired.';
			validationError = true;
		} else {
			$scope.validationMap.pwd.valid=true;
		}
		
		if (validationError) {
			return;
		}
			
		$http.post("rest/auth/login", {
			'name' : name,
			'pwd' : pwd
		}, {
			'Content-type' : "application/json"
		}).success(function(data) {
			window.location = window.location.origin + "/kutatasinaplo/auth/index.html";
		}).error(function(data) {
		});
	}
	
	$scope.register = function() {
		window.location = window.location.origin + "/kutatasinaplo/register.html";
	}

	var init = function() {

	};

	init();
} ]);