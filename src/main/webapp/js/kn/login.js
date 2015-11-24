var app = angular.module('knApp');

app.controller('AuthController', [ '$scope', '$rootScope', '$http', function($scope, $rootScope, $http) {

	$scope.login = function() {
		var name = $("#username").val(); 
		var pwd =  $("#pwd").val();
			
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

	var init = function() {

	};

	init();
} ]);