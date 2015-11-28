var app = angular.module("knApp");

app.controller('ExperimentController', ['$scope', '$http', function($scope, $http) {
	$scope.data = {};
	$scope.users = [];
	$scope.canParticipate = [];
	$scope.id;
	$scope.inEditorMode = false;
	$scope.addedFiles = [];
	$scope.addedImages = [];
	
	$scope.save = function() {
		$http.post('rest/experiment/save', $scope.data).then(
				// Success
				function (resp) {
					$scope.inEditorMode = false;
				},
				// Error
				function (resp) {
					
				}
			);
	}
	
	$scope.edit = function() {
		$scope.inEditorMode = true;
	}
	
	$scope.setParticipants = function() {
		var participantsIds = [];
		$("#multiselect_to option").each(function()
		{
			participantsIds.push($(this).val());
		});
		
		$http.post('rest/experiment/set/participants', 
				{ 'entityId' : $scope.id,
				  'list' : participantsIds
				}).then(
				// Success
				function (resp) {
					$scope.init();
				},
				// Error
				function (resp) {
					
				}
			);
	}
	
	$scope.addNewUrl = function() {
		$scope.data.urls.push({'key':'', 'value':''})
	}
	
	$scope.attachmentChanged = function(element) {

	     $scope.$apply(function(scope) {
	         var file = element.files[0];
	         
	         $scope.addedFiles.push({'name':file.name, 'type': file.type, 'size': file.size, 'data':{}});
	         var index = $scope.addedFiles.length - 1;
	         
	         var reader = new FileReader();
	         reader.onload = function(e) {
	            // handle onload
	        	 $scope.addedFiles[index].data = e.target.result;
	         };
	         reader.readAsDataURL(file);
	     });
	};
	
	$scope.imageChanged = function(element) {

	     $scope.$apply(function(scope) {
	         var file = element.files[0];
	         
	         $scope.addedImages.push({'name':file.name, 'type': file.type, 'size': file.size, 'data':{}});
	         var index = $scope.addedImages.length - 1;
	         
	         var reader = new FileReader();
	         reader.onload = function(e) {
	            // handle onload
	        	 $scope.addedImages[index].data = e.target.result;
	         };
	         reader.readAsDataURL(file);
	     });
	};
	
	$scope.deleteAttachment = function(idToDelete) {
		$http.post('rest/file/delete/attachment', {'entityId': $scope.id, 'id': idToDelete}).then(
				// Success
				function (resp) {
				},
				// Error
				function (resp) {
					
				}
			);
	}
	
	$scope.deleteImage = function(idToDelete) {
		$http.post('rest/file/delete/image', {'entityId': $scope.id, 'id': idToDelete}).then(
				// Success
				function (resp) {
				},
				// Error
				function (resp) {
					
				}
			);
	}
	
	$scope.uploadAddedFiles = function() {
		$http.post('rest/file/upload/attachment', {'id': $scope.id, 'list': $scope.addedFiles}).then(
			// Success
			function (resp) {
			},
			// Error
			function (resp) {
				
			}
		);
	}
	
	$scope.uploadAddedImages = function() {
		$http.post('rest/file/upload/images', {'id': $scope.id, 'list': $scope.addedImages}).then(
			// Success
			function (resp) {
			},
			// Error
			function (resp) {
				
			}
		);
	}
	
	$scope.slideDownAdder = function() {
		$( "#add-participants-form-container" ).slideDown("slow");
	}
	
	$scope.slideUpAdder = function() {
		$( "#add-participants-form-container" ).slideUp("slow");
	}
	
	
	$scope.init = function() {
		$scope.id = $.url(window.location).param('id');
		var mode = $.url(window.location).param('mode');
		if(mode==='edit') {
			$scope.inEditorMode = true;
		}
		$http.post('rest/experiment/load', $scope.id).then(
					// Success
					function (resp) {
						$scope.data = resp.data;
						//Participants
						if($scope.data.participants) {
							var participantsData = $scope.data.participants;
							if(participantsData.constructor != Array) {
								$scope.data.participants = [];
								$scope.data.participants[0] = participantsData;
							}
						} else {
							$scope.data.participants = [];
						}
						//Urls
						if($scope.data.urls) {
							var urlsData = $scope.data.urls;
							if(urlsData.constructor != Array) {
								$scope.data.urls = [];
								$scope.data.urls[0] = urlsData;
							}
						} else {
							$scope.data.urls = [];
						}
						//Participants
						if($scope.data.attachments) {
							var attachmentsData = $scope.data.attachments;
							if(attachmentsData.constructor != Array) {
								$scope.data.attachments = [];
								$scope.data.attachments[0] = attachmentsData;
							}
						} else {
							$scope.data.attachments = [];
						}
						//Images
						if($scope.data.images) {
							var imagesData = $scope.data.images;
							if(imagesData.constructor != Array) {
								$scope.data.images = [];
								$scope.data.images[0] = imagesData;
							}
						} else {
							$scope.data.images = [];
						}
						
						$http.get('rest/user/list/user').then(
								// Success
								function (resp) {
									var userData = resp.data.user;
									if(userData.constructor == Array) {
										$scope.users = userData;
									} else {
										$scope.users[0] = userData;
									}
									
									$scope.canParticipate = jQuery.grep($scope.users, function(value) {
										   var participants = angular.element('[ng-controller=ExperimentController]').scope().data.participants;
										   var index;
										   for (index = 0; index < participants.length; ++index) {
												if(participants[index].id == value.id) {
													return false;
												}
										   }
										   return true;
										});
								},
								// Error
								function (resp) {
									
								}
							);
					},
					// Error
					function (resp) {
						
					}
				);
		
		$('#multiselect').multiselect();
	}
	
	$scope.init()
}]);