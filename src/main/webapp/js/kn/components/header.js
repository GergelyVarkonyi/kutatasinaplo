var app = angular.module("knApp");

app.controller('HeaderController', ['$scope', function($scope) {
  
}]);

app.directive('header', function() {
  return {
    templateUrl: '/kutatasinaplo/components/header.html',
    restrict: 'E'
  };
});