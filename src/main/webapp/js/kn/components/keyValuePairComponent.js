var app = angular.module("knApp");

app.directive('keyValuePairComponent', function() {
	function link(scope, element, attrs) {
	}
	
    return {
    	scope: {
    	    keyLabel: '=',
    	    valueLabel: '=',
    	    buttonLabel: '=',
    	    values: '=',
    	    addCallback: '&' 
    	},
        templateUrl: '/kutatasinaplo/components/keyValuePairComponent.html',
        restrict: 'E',
        link: link
    };
});