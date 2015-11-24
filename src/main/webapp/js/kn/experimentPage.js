var app = angular.module("knApp");

app.controller('ExperimentController', ['$scope', '$http', function($scope, $http) {
	$scope.data = {};
	
	$scope.save = function() {
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
	}
	
	init()
}]);