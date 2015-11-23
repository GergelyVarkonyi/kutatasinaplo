var app = angular.module('baseApp', []);
// var rootURL = "/ugykezelo/"

app.factory('translationService', [ '$http', function($http) {
	return {
		get : function(part, language, callback) {
			$http.get(rootURL + "rest/language/" + part + "?l=" + language).success(function(data, status, headers, config) {
				callback(data);
			}).error(function(data, status, headers, config) {
			});
		}
	};
} ]);

app.factory('animationService', [ function() {
	return {
		success : function(element) {
			element.addClass("animate-background-success");
			element.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e) {
				element.removeClass('animate-background-success');
			});
		},
		fail : function(element) {
			element.addClass("animate-background-fail");
			element.one('webkitAnimationEnd oanimationend msAnimationEnd animationend', function(e) {
				element.removeClass('animate-background-fail');
			});
		}
	};
} ]);