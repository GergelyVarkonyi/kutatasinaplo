var app = angular.module("knApp");

app.controller('ExperimentController', ['$scope', '$http', '$rootScope', function($scope, $http,$rootScope) {
	$scope.data = {};
	$scope.users = [];
	$scope.canParticipate = [];
	$scope.id;
	$scope.inEditorMode = false;
	$scope.addedFiles = [];
	$scope.addedImages = [];
	
	$scope.validationMap = {
			'name':{'valid': true, 'msg': ''},
			'description':{'valid': true, 'msg': ''},
			'urls':{'valid': true, 'msg': ''}};
	
	$scope.save = function() {
		var validationError = false;
		
		if(!$scope.data.description) {
			$scope.validationMap.description.valid=false;
			$scope.validationMap.description.msg='Description is requeired.';
			validationError = true;
		} else {
			$scope.validationMap.description.valid=true;
		}
		var i;
		var urlLabelsAreOK = true;
		for (i=0; i<$scope.data.urls.length; i++) {
			if(!$scope.data.urls[i].key || !$scope.data.urls[i].value) {
				$scope.validationMap.urls.valid=false;
				$scope.validationMap.urls.msg='None of the labels or urls below can be empty.';
				validationError = true;
				urlLabelsAreOK = false;
				break;
			} else if(!$scope.isValidUrl($scope.data.urls[i].value)) {
				$scope.validationMap.urls.valid=false;
				$scope.validationMap.urls.msg='One of the urls is not valid.';
				validationError = true;
				urlLabelsAreOK = false;
				break;			
			}
		}
		if(urlLabelsAreOK) {
			$scope.validationMap.urls.valid=true;
		}
		
		if (validationError) {
			return;
		}
		
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
	
	$scope.isValidUrl = function(url) {
	    return url.match(/(http|ftp|https):\/\/[\w\-_]+(\.[\w\-_]+)+([\w\-\.,@?^=%&amp;:/~\+#]*[\w\-\@?^=%&amp;/~\+#])?/);
	}
	
	$scope.edit = function() {
		$scope.inEditorMode = true;
	}
	
	$scope.hasPermissionTo = function(action, entity) {
		var current = $rootScope.current;
		if(current) {
			if (current.role === 'ADMIN') {
				return true;
			}
			
			if(action==='EDIT_EXPERIMENT' || action==='SAVE_EXPERIMENT' || action==='UPLOAD' || action==='ADD_URL') {
				if ($scope.data.owner && $scope.data.owner.id == current.id) {
					return true;
				} else if ($scope.data.participants) {
					var i;
					for (i=0; i<$scope.data.participants.length; i++) {
						if ($scope.data.participants[i].id == current.id) {
							return true;
						}
					}
				}
			} else if(action==='ADD_PARTICIPANT') {
				if($scope.data.owner) {
					return $scope.data.owner.id == current.id;
				}
			}
		}
		return false;
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
	
	$scope.removeUrl = function(index) {
	   $scope.data.urls.splice(index,1);
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
					$scope.init();
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
					$scope.init();
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
				$scope.init();
				$scope.addedFiles = [];
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
				$scope.init();
				$scope.addedImages = [];
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
									
//									$scope.users = jQuery.grep($scope.users, function(value) {
//										var ownerId = angular.element('[ng-controller=ExperimentController]').scope().data.owner.id;
//										return value.id != ownerId;
//									});

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