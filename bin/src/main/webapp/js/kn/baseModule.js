var app = angular.module('baseModule', []);

app.controller('ListController', function($scope, $http) {
	var list = [];
	var definition = {};

	$scope.load = function(filter) {
		$http.post("/ugykezelo/module/gen/testModule/load/definition", {data:filter.table}
		).success(function(data, status, headers, config) {
			// this callback will be called asynchronously
			// when the response is available
			$scope.definition = data;
			$http.post("/ugykezelo/module/gen/testModule/load/all", {data:filter.table}
			).success(function(data, status, headers, config) {
				// this callback will be called asynchronously
				// when the response is available
				$scope.list = data;
			}).error(function(data, status, headers, config) {
				// called asynchronously if an error occurs
				// or server returns response with an error status.
			});
		}).error(function(data, status, headers, config) {
			// called asynchronously if an error occurs
			// or server returns response with an error status.
		});
		
	};

});