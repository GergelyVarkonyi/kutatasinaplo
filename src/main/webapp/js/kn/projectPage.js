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

              }

              $scope.edit = function() {
                $scope.inEditorMode = true;
              }

              $scope.slideDownAdder = function() {
                $("#add-participants-form-container").slideDown("slow");
              }

              $scope.slideUpAdder = function() {
                $("#add-participants-form-container").slideUp("slow");
              }
              
              $scope.setParticipants = function() {
                var participantsIds = [];
                $("#multiselect_to option").each(function()
                {
                  participantsIds.push($(this).val());
                });
                
                $http.post('rest/project/set/participants', 
                    { 'entityId' : $scope.id,
                      'list' : participantsIds
                    }).then(
                    // Success
                    function (resp) {
                      init();
                    },
                    // Error
                    function (resp) {
                      
                    }
                  );
              }

              var init = function() {
                $scope.id = $.url(window.location).param('id');
                $http
                    .get('rest/project/' + $scope.id )
                    .then(
                        // Success
                        function(resp) {
                          $scope.data = resp.data;
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
                          // Urls
                          if ($scope.data.urls) {
                            var urlsData = $scope.data.urls;
                            if (urlsData.constructor != Array) {
                              $scope.data.urls = [];
                              $scope.data.urls[0] = urlsData;
                            }
                          } else {
                            $scope.data.urls = [];
                          }
                          // Participants
                          if ($scope.data.attachments) {
                            var attachmentsData = $scope.data.attachments;
                            if (attachmentsData.constructor != Array) {
                              $scope.data.attachments = [];
                              $scope.data.attachments[0] = attachmentsData;
                            }
                          } else {
                            $scope.data.attachments = [];
                          }
                          // Images
                          if ($scope.data.images) {
                            var imagesData = $scope.data.images;
                            if (imagesData.constructor != Array) {
                              $scope.data.images = [];
                              $scope.data.images[0] = imagesData;
                            }
                          } else {
                            $scope.data.images = [];
                          }

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
                                              if (participants){
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
              init();
              $('#multiselect').multiselect();
            } ]);