angular.module('PromatApp', ['apiService'])

.controller('candidatesController', function ($scope, Candidates) {

	//$scope.candidates = [];



	this.init = function () {
	    console.log("getting all candidates");
		$scope.candidates = Candidates.query();
		//this.getAllCandidates();
		console.log("invoked init()");
	};


	this.getAllCandidates = function() {
		$scope.candidates = apiService.query();
		console.log("invoked getAllCandidates()");
	};


	this.init();
});