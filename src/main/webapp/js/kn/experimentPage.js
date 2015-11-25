var app = angular.module("knApp");

app.controller('ExperimentController', ['$scope', '$http', function($scope, $http) {
	$scope.data = {};
	$scope.users = [];
	
	$scope.save = function() {
	}
	
	$scope.edit = function() {
		
	}
	
	$scope.slideDownAdder = function() {
		$( "#add-participants-form-container" ).slideDown("slow");
	}
	
	$scope.slideUpAdder = function() {
		$( "#add-participants-form-container" ).slideUp("slow");
	}
	
	function init() {
		var id = $.url(window.location).param('id');
		$http.post('rest/experiment/load', id).then(
					// Success
					function (resp) {
						$scope.data = resp.data;
					},
					// Error
					function (resp) {
						
					}
				);
		
		$http.get('rest/user/list/user').then(
				// Success
				function (resp) {
					var userData = resp.data.user;
					if(userData.constructor == Array) {
						$scope.users = userData;
					} else {
						$scope.users[0] = userData;
					}
				},
				// Error
				function (resp) {
					
				}
			);
		
		
		$('#multiselect').multiselect();
	}
	
	init()
}]);