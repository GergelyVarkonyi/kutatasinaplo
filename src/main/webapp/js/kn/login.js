var app = angular.module('baseApp');

app.controller('AuthController', [ '$scope', '$rootScope', '$http', function($scope, $rootScope, $http) {

	$scope.login = function() {
		$http.post("rest/auth/login", {
			'name' : "admin",
			'pwd' : "0000"
		}, {
			'Content-type' : "application/json"
		}).success(function(data) {
			window.location = window.location.origin + "kutatasinaplo/auth/index.html";
		}).error(function(data) {
		});
	}

	var init = function() {

	};

	init();
} ]);