var app = angular.module("knApp");

app.controller('HeaderController', ['$scope','$http', function($scope, $http) {
	$scope.user;
	
	$scope.init = function() {
		$http.get("/kutatasinaplo/rest/auth/current").
			success(function(resp) {
				$scope.user = resp;
			}).
			error(function(data) {
			});
	}
	
	$scope.logout = function() {
		$http.get("/kutatasinaplo/rest/auth/logout").
			success(function(resp) {
				delete $scope.user;
				window.location = window.location.origin + "/kutatasinaplo/experiments.html";
			}).
			error(function(data) {
			});
	}
	
	$scope.signIn = function() {
		window.location = window.location.origin + "/kutatasinaplo/login.html";
	}
	
	$scope.init();
}]);

app.directive('header', function() {
  return {
    templateUrl: '/kutatasinaplo/components/header.html',
    restrict: 'E'
  };
});