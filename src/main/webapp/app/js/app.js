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
      when('/home', {
              templateUrl: 'app/views/home-view.html',
              controller: 'HomeViewController'
            }).
      otherwise({
        redirectTo: '/home'
      });
  }])
.controller('AddCandidateController', function($scope, Candidates){

    $scope.addCandidate = function(candidate){
        $scope.message = 'User ' + candidate.name + ' added successfully';
        Candidates.save(candidate);
        $scope.message = candidate.name + " created successfully."
        reset();
    };

    $scope.reset = function(){
        console.log("reset of 'AddNewCandidate' form called ");
        $scope.candidate = new Object();
    };
})
.controller('ShowCandidatesController', function($scope, Candidates){
  	this.init = function () {
  		this.getAllCandidates();
  	};

  	this.getAllCandidates = function() {
  		$scope.candidates = Candidates.query();
  	};

    $scope.deleteCandidate = function(candidateId){

    };

  	this.init();

})
.controller('HomeViewController', function(){

})
;