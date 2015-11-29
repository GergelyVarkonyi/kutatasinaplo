var app = angular.module('knApp');

app.controller('RegisterController', [ '$scope', '$rootScope', '$http', function($scope, $rootScope, $http) {

	$scope.validationMap = {
			'username':{'valid': true, 'msg': ''},
			'pwd':{'valid': true, 'msg': ''},
			'email':{'valid': true, 'msg': ''},
			'knowledge':{'valid': true, 'msg': ''}};
	
	$scope.user={'knowledge':[{'key':'', 'value':''}]};
	
	$scope.addNewKnowledge = function() {
		$scope.user.knowledge.push({'key':'', 'value':''})
	}
	
	$scope.save = function() {
		var validationError = false;
		
		if(!$scope.user.name) {
			$scope.validationMap.username.valid=false;
			$scope.validationMap.username.msg='Username is requeired.';
			validationError = true;
		} else {
			$scope.validationMap.username.valid=true;
		}
		if(!$scope.user.pwd) {
			$scope.validationMap.pwd.valid=false;
			$scope.validationMap.pwd.msg='Password is requeired.';
			validationError = true;
		} else {
			$scope.validationMap.pwd.valid=true;
		}
		if(!$scope.user.email) {
			$scope.validationMap.email.valid=false;
			$scope.validationMap.email.msg='E-mail address is requeired.';
			validationError = true;
		} else if(!$scope.isEmailValid($scope.user.email)) {
			$scope.validationMap.email.valid=false;
			$scope.validationMap.email.msg='E-mail is not valid.';
			validationError = true;
		} else {
			$scope.validationMap.email.valid=true;
		}
		var i;
		var topicsAreOK = true;
		for (i=0; i<$scope.user.knowledge.length; i++) {
			if(!$scope.user.knowledge[i].key) {
				$scope.validationMap.knowledge.valid=false;
				$scope.validationMap.knowledge.msg='None of the topics below can be empty.';
				validationError = true;
				topicsAreOK = false;
				break;
			}
		}
		if(topicsAreOK) {
			$scope.validationMap.knowledge.valid=true;
		}
		
		if (validationError) {
			return;
		}
		
		$http.post('rest/user/create', $scope.user
			).then(
				// Success
				function (resp) {
					$rootScope.message = {'present' : true, 'message': $scope.user.name + " has been registered. You can sign in now.", 'type':'success'};
				},
				// Error
				function (resp) {
					$rootScope.message = {'present' : true, 'message': resp.data, 'type':'danger'};
				}
			);
	}
	
	$scope.isEmailValid = function(emailAddress) {
	    var pattern = /^([a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+(\.[a-z\d!#$%&'*+\-\/=?^_`{|}~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]+)*|"((([ \t]*\r\n)?[ \t]+)?([\x01-\x08\x0b\x0c\x0e-\x1f\x7f\x21\x23-\x5b\x5d-\x7e\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|\\[\x01-\x09\x0b\x0c\x0d-\x7f\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))*(([ \t]*\r\n)?[ \t]+)?")@(([a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\d\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.)+([a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]|[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF][a-z\d\-._~\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]*[a-z\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])\.?$/i;
	    return pattern.test(emailAddress);
	};
	
	$scope.removeKnowledge = function(index) {
	   $scope.user.knowledge.splice(index,1);
	}
	
	var init = function() {

	};

	init();
} ]);