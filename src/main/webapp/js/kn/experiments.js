var app = angular.module("knApp");

app.controller('ExperimentsController', ['$scope', '$http','$rootScope', function($scope, $http, $rootScope) {
	$scope.experiments = [];
	
	$scope.slideDownNew = function() {
		$( "#new-experiment-form-container" ).slideDown("slow");
	}
	
	$scope.slideUpNew = function() {
		$( "#new-experiment-form-container" ).slideUp("slow");
	}
	
	$scope.hasPermissionTo = function(action, entity) {
		var current = $rootScope.current;
		if(current) {
			if(action==='CREATE_EXPERIMENT') {
				return current.role==='USER' || $scope.isAdmin();
			} else if(action==='DELETE_EXPERIMENT') {
				return $scope.isAdmin() || entity.owner.id == current.id;
			}
		}
		return false;
	}
	
	$scope.isAdmin = function() {
		return $rootScope.current.role==='ADMIN';
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
	
	$scope.delete = function(id) {
		$http.post('rest/experiment/delete', {'id' : id}).then(
				// Success
				function (resp) {
					init();
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
						$("#new-experiment-name").val('');
						$("#new-experiment-description").val('');
						$("#new-experiment-public").attr('checked', false);
					},
					// Error
					function (resp) {
						
					}
				);
	}
	
	init()
}]);