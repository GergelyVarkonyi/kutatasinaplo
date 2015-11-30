var app = angular.module("knApp");

app.controller('AdminController', [ '$scope', '$http', '$rootScope', function($scope, $http, $rootScope) {
  $scope.users = [];

  $scope.save = function() {
    console.log($scope.users);
    $http.post('/kutatasinaplo/rest/user/saveroles', {
      users : $scope.users
    }).then(
    // Success
    function(resp) {
      $scope.inEditorMode = false;
      $rootScope.message = {'present' : true, 'message': "Successfully saved users!", 'type':'success'};
    },
    // Error
    function(resp) {

    });
  };

  $scope.edit = function() {
    $scope.inEditorMode = true;
  }

  $scope.create = function(form) {
    if (form.$valid) {
      $http.post('/kutatasinaplo/rest/project/create', $scope.edited).then(
      // Success
      function(resp) {
        $scope.projects.push(resp.data);
        $scope.edited = null;
      },
      // Error
      function(resp) {

      });
      form.$setPristine();
      form.$setUntouched();
      $scope.ok = true;
      $scope.slideUpNew();
    } else {
      $scope.ok = false;
    }
  }

  var init = function() {

    $http.get('/kutatasinaplo/rest/auth/list/registeredroles').then(
    // Success
    function(resp) {
      if (resp.data) {
        var roles = resp.data.role;
        if (roles.constructor == Array) {
          $scope.roles = roles;
        } else {
          $scope.roles[0] = roles;
        }
      }

      $http.get('/kutatasinaplo/rest/user/list/user').then(
      // Success
      function(resp) {
        if (resp.data) {
          var users = resp.data.user;
          if (users.constructor == Array) {
            $scope.users = users;
          } else {
            $scope.users[0] = users;
          }
        }
      },
      // Error
      function(resp) {

      });
    });
  }

  init()
} ]);