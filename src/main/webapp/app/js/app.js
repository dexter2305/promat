angular.module('PromatApp', ['apiService','ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider.
      when('/addCandidate', {
        templateUrl: 'app/views/add-candidate.html',
        controller: 'AddCandidateController'
      }).
      when('/showCandidates', {
        templateUrl: 'app/views/show-candidates.html',
        controller: 'ShowCandidatesController'
      }).
      otherwise({
        redirectTo: '/showCandidates'
      });
  }])
.controller('AddCandidateController', function($scope){
    $scope.message = 'Add candidate use case';
})
.controller('ShowCandidatesController', function($scope, Candidates){
  	this.init = function () {
  	    console.log("getting all candidates");
  		$scope.candidates = Candidates.query();
  		//this.getAllCandidates();
  		$scope.message = "show candidate use case !! "
  		console.log("invoked init()");
  	};


  	this.getAllCandidates = function() {
  		$scope.candidates = apiService.query();
  		console.log("invoked getAllCandidates()");
  	};
  	this.init();

})
;