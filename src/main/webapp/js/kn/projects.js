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
	
	$scope.openProjectPage = function(id){
	   window.location = window.location.origin + "/kutatasinaplo/projectPage.html?id="+id; 
	}
	
	$http.get('/kutatasinaplo/rest/auth/current').then(
      // Success
      function (resp) {
        $scope.currentUser = resp.data;
      }
  );
	
	$scope.create = function(form) {
	  
		if (form.$valid){
			$http.post('/kutatasinaplo/rest/project/create',
					$scope.edited
			).then(
					// Success
					function (resp) {
						$scope.projects.push(resp.data);
						$scope.edited = null;
					},
					// Error
					function (resp) {
						
					}
			);
			form.$setPristine();
		    form.$setUntouched();
		    $scope.ok = true;
		    $scope.slideUpNew(); 
		} else {
			$scope.ok = false;
		}
	}
	
	$scope.delete = function(item){
		$http.delete('/kutatasinaplo/rest/project/delete/' + item.id)
		.then(
				// Success
				function (resp) {
					var itemIndex=$scope.projects.lastIndexOf(item);
					$scope.projects.splice(itemIndex, 1);
				},
				// Error
				function (resp) {
					
				}
			);
		
	} 
	
	function init() {
		$http.get('/kutatasinaplo/rest/project/list').then(
					// Success
					function (resp) {
					  if (resp.data){
					    $scope.projects = resp.data.project;
					  }
					},
					
					// Error
					function (resp) {
						
					}
				);
	}
	
	init()
}]);