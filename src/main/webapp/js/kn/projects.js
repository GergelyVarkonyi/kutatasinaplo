var app = angular.module("knApp");

app.controller('ProjectsController', ['$scope', '$http', function($scope, $http) {
	$scope.projects = [];
	
	$scope.edited = {};
	
	$scope.ok = true;
	
	$scope.slideDownNew = function() {
		$( "#new-experiment-form-container" ).slideDown("slow");
	}
	
	$scope.slideUpNew = function() {
		$( "#new-experiment-form-container" ).slideUp("slow");
	}
	
	$scope.create = function(form) {
		if (form.$valid){
			$http.post('/kutatasinaplo/rest/project/create',
					$scope.edited
			).then(
					// Success
					function (resp) {
						$scope.projects.push($scope.edited);
						$scope.edited = null;
					},
					// Error
					function (resp) {
						
					}
			);
			form.$setPristine();
		    form.$setUntouched();
		    $scope.ok = true;
		} else {
			$scope.ok = false;
		}
	}
	
	function init() {
		$http.get('/kutatasinaplo/rest/project/list').then(
					// Success
					function (resp) {
						$scope.projects = resp.data.project;
					},
					
					// Error
					function (resp) {
						
					}
				);
	}
	
	init()
}]);