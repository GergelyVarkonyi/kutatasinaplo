var app = angular.module("knApp");

app.controller('ProjectsController', ['$scope', '$http', function($scope, $http) {
	$scope.projects = [];
	
	function init() {
		$http.get('/kutatasinaplo/rest/project/list').then(
					// Success
					function (resp) {
						if(resp.data) {
							var projectsData = resp.data.project;
							if(projectsData.constructor == Array) {
									$scope.projects = resp.data.experiment;
							} else {
								$scope.projects[0] = projectsData;
							}
						}
					},
					
					// Error
					function (resp) {
						
					}
				);
	}
	
	init()
}]);