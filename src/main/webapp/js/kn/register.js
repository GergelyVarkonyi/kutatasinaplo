var app = angular.module('knApp');

app.controller('RegisterController', [ '$scope', '$rootScope', '$http', function($scope, $rootScope, $http) {

	$scope.user={'knowledge':[{'key':'', 'value':''}]};
	
	$scope.addNewKnowledge = function() {
		$scope.user.knowledge.push({'key':'', 'value':''})
	}
	
	$scope.save = function() {
		$http.post('rest/user/create', $scope.user
			).then(
				// Success
				function (resp) {
				},
				// Error
				function (resp) {
					
				}
			);
	}
	
	var init = function() {

	};

	init();
} ]);