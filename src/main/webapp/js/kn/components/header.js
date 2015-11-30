var app = angular.module("knApp");

app.controller('HeaderController', ['$scope','$http','$rootScope','$timeout', function($scope, $http, $rootScope, $timeout) {
	$scope.user;
	$scope.message;
	
	$scope.init = function() {
		$http.get("/kutatasinaplo/rest/auth/current").
			success(function(resp) {
				$scope.user = resp;
				$rootScope.current = resp;
			}).
			error(function(data) {
			});
		$rootScope.$watch('message', function(newValue, oldValue) {
			$scope.message=newValue;
			$timeout(function(){
				$rootScope.message.present = false;
			}, 10000);
		});
		
		$http.get('/kutatasinaplo/rest/auth/current').then(
      // Success
      function(resp) {
        var userData = resp.data;
        $scope.isAdmin = userData.role == 'ADMIN';
      });

	}
	
	$scope.logout = function() {
		$http.get("/kutatasinaplo/rest/auth/logout").
			success(function(resp) {
				delete $scope.user;
				delete $rootScope.current;
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