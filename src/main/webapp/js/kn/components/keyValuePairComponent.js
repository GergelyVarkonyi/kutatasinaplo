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
    	    type: '=',
    	    addCallback: '&',
    	    deleteCallback: '='
    	},
        templateUrl: '/kutatasinaplo/components/keyValuePairComponent.html',
        restrict: 'E',
        link: link
    };
});