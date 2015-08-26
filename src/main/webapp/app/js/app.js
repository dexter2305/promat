angular.module('PromatApp', ['apiService','ngRoute'])

.config(['$routeProvider', function($routeProvider) {
    $routeProvider
    .when('/addCandidate', {
        templateUrl: 'app/views/add-candidate.html',
        controller: 'AddCandidateController'
    })
    .when('/candidates', {
        templateUrl: 'app/views/show-candidates.html',
        controller: 'ShowCandidatesController'
    })
    .when('/candidate/:candidateId', {
            templateUrl: 'app/views/show-candidate-details.html',
            controller: 'ShowCandidateDetailsController'
    })
    .when('/home', {
              templateUrl: 'app/views/home-view.html',
              controller: 'HomeViewController'
    })
    .otherwise({
        redirectTo: '/home'
    });
  }])
.controller('AddCandidateController', function($scope, Candidates){
    $scope.addCandidate = function(candidate){
        Candidates.save(candidate);
        $scope.message = candidate.name + " created successfully."

    };

    $scope.reset = function(){
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
    $scope.deleteCandidate = function(candidate){
        candidate.$delete(function(){
            $scope.candidates = Candidates.query();
        });
    };
  	this.init();
})
.controller('ShowCandidateDetailsController', function($scope, $routeParams, Candidates){
    this.init = function(candidateId){
        console.log('ShowCandidateDetailsController.init() invoked for id:' + candidateId);
        Candidates.get({id:candidateId},function(candidate){
            $scope.candidate = candidate;
            console.log("retrieved " + candidate.name + " for " + candidateId);
        });
        console.log("ShowCandidateDetailsController.init() completed for id:" + candidateId);
    };

    this.init($routeParams.candidateId);

    $scope.updateCandidate = function(candidate){
        //Candidates.save(candidate);
        candidate.$update();
        $scope.message = candidate.name + " updated successfully."
    };
})
.controller('HomeViewController', function(){

})
;