var app = angular.module("knApp");

app.controller('ExperimentsController', ['$scope', '$http', function($scope, $http) {
	$scope.experiments = [];
	
	$scope.slideDownNew = function() {
		$( "#new-experiment-form-container" ).slideDown("slow");
	}
	
	$scope.slideUpNew = function() {
		$( "#new-experiment-form-container" ).slideUp("slow");
	}
	
	$scope.create = function() {
		var name = $("#new-experiment-name").val();
		var description = $("#new-experiment-description").val();
		var isPublic = false;
		if($("#new-experiment-public").is(":checked")) {
			isPublic = true;
		}
		
		$http.post('rest/experiment/create',
			{'name' : name,
			'description' : description,
			'public' : isPublic}
			).then(
				// Success
				function (resp) {
					init();
					$scope.slideUpNew();
				},
				// Error
				function (resp) {
					
				}
			);
	}
	
	$scope.load = function(id) {
		window.location = window.location.origin + "/kutatasinaplo/experimentPage.html?id="+id; 
	}
	
	function init() {
		$http.get('rest/experiment/list/experiment').then(
					// Success
					function (resp) {
						if(resp.data) {
							var experimentsData = resp.data.experiment;
							if(experimentsData.constructor == Array) {
									$scope.experiments = resp.data.experiment;
							} else {
								$scope.experiments[0] = experimentsData;
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