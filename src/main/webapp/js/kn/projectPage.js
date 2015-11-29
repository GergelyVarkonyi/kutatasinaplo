var app = angular.module("knApp");

app
    .controller(
        'ProjectController',
        [
            '$scope',
            '$http',
            '$rootScope',
            function($scope, $http, $rootScope) {

              $scope.isAdmin = true;
              $scope.data = {};

              $scope.edit = function() {
                $scope.inEditorMode = true;
                $scope.dataSnapshot = angular.copy($scope.data);
              }
              
              $scope.cancel = function(){
                $scope.data = $scope.dataSnapshot;
                $scope.inEditorMode = false;
              }

              $scope.slideDownParticipantsAdder = function() {
                $("#add-participants-form-container").slideDown("slow");
              }

              $scope.slideUpParticipantsAdder = function() {
                $("#add-participants-form-container").slideUp("slow");
              }
              
              $scope.loadExperiment = function(id) {
                window.location = window.location.origin + "/kutatasinaplo/experimentPage.html?id="+id; 
              }

              $scope.setParticipants = function() {
                var participantsIds = [];
                $("#multiselect_to option").each(function() {
                  participantsIds.push($(this).val());
                });

                $http.post('rest/project/set/participants', {
                  'entityId' : $scope.id,
                  'list' : participantsIds
                }).then(
                // Success
                function(resp) {
                  init();
                },
                // Error
                function(resp) {

                });
              }

              var init = function() {
                $scope.inEditorMode = false;
                $scope.id = $.url(window.location).param('id');
                $http
                    .get('rest/project/' + $scope.id)
                    .then(
                        // Success
                        function(resp) {
                          $scope.data = resp.data;
                          if ($scope.data.experiments && $scope.data.experiments.constructor !== Array) {
                            $scope.data.experiments = [$scope.data.experiments];
                          }
                          // Participants
                          if ($scope.data.participants) {
                            var participantsData = $scope.data.participants;
                            if (participantsData.constructor != Array) {
                              $scope.data.participants = [];
                              $scope.data.participants[0] = participantsData;
                            }
                          } else {
                            $scope.data.participants = [];
                          }

                          $http
                              .get('rest/experiment/list/experiment')
                              .then(
                                  // Success
                                  function(resp) {
                                    if (resp.data) {
                                      var experimentsData = resp.data.experiment;
                                      if (experimentsData.constructor == Array) {
                                        $scope.allExperiments = resp.data.experiment;
                                      } else {
                                        $scope.allExperiments[0] = experimentsData;
                                      }
                                      $scope.allExperimentsById = [];
                                      for (i = 0; i < $scope.allExperiments.length; i++) {
                                        var exp = $scope.allExperiments[i];
                                        $scope.allExperimentsById[exp.id] = exp;
                                      }

                                      $scope.possibleExperiments = jQuery
                                          .grep(
                                              $scope.allExperiments,
                                              function(value) {
                                                var experiments = $scope.data.experiments;
                                                var index;
                                                if (experiments) {
                                                  for (index = 0; index < experiments.length; ++index) {
                                                    if (experiments[index].id == value.id) {
                                                      return false;
                                                    }
                                                  }
                                                }
                                                return true;
                                              });
                                    }
                                  });

                          $http
                              .get('rest/user/list/user')
                              .then(
                                  // Success
                                  function(resp) {
                                    var userData = resp.data.user;
                                    if (userData.constructor == Array) {
                                      $scope.users = userData;
                                    } else {
                                      $scope.users[0] = userData;
                                    }
                                    $scope.canParticipate = jQuery
                                        .grep(
                                            $scope.users,
                                            function(value) {
                                              var participants = $scope.data.participants;
                                              var index;
                                              if (participants) {
                                                for (index = 0; index < participants.length; ++index) {
                                                  if (participants[index].id == value.id) {
                                                    return false;
                                                  }
                                                }
                                              }
                                              return true;
                                            });

                                  },
                                  // Error
                                  function(resp) {

                                  });
                        },
                        // Error
                        function(resp) {

                        });

              }

              $scope.save = function() {
                var experimentIds = [];
                $("#multiselect2_to option").each(function() {
                  experimentIds.push($(this).val());
                });

                var experimentsInProject = [];

                for (i = 0; i < experimentIds.length; i++) {
                  var anID = experimentIds[i];
                  var experiment = $scope.allExperimentsById[anID];
                  experimentsInProject.push(experiment);
                }

                $scope.data.experiments = experimentsInProject;

                $http.put('rest/project/', $scope.data).then(
                // Success
                function(resp) {
                  $scope.slideUpParticipantsAdder()
                  init();
                },
                // Error
                function(resp) {

                });
              }

              init();
              $('#multiselect').multiselect();
              $('#multiselect2').multiselect();
            } ]);