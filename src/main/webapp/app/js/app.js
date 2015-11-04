angular.module('PromatApp', ['apiService', 'ngRoute'])

.config(['$routeProvider', function ($routeProvider) {
		$routeProvider
			.when('/addCandidate', {
				templateUrl: 'app/views/candidate-profile.html',
				controller: 'AddCandidateController'
			})
			.when('/candidates', {
				templateUrl: 'app/views/show-candidates.html',
				controller: 'ShowCandidatesController'
			})
			.when('/candidate/:candidateId', {
				templateUrl: 'app/views/candidate-profile-sp.html',
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
	.controller('AddCandidateController', function ($scope, Candidates) {
		$scope.addCandidate = function (candidate) {
			Candidates.save(candidate);
			$scope.message = candidate.name + " created successfully.";

		};

		$scope.reset = function () {
			$scope.candidate = new Object();
		};
		$scope.isNewProfile = true;
	})
	.controller('ShowCandidatesController', function ($scope, Candidates) {
		this.init = function () {
			this.getAllCandidates();
		};
		this.getAllCandidates = function () {
			$scope.candidates = Candidates.query();
		};
		$scope.deleteCandidate = function (candidate) {
			candidate.$delete(function () {
				$scope.candidates = Candidates.query();
			});
		};
		this.init();
	})
	.controller('ShowCandidateDetailsController', function ($scope, $routeParams, Candidates) {
		this.init = function (candidateId) {
			Candidates.get({
				id: candidateId
			}, function (candidate) {
				$scope.candidate = candidate;
			});
		};

		this.init($routeParams.candidateId);

		$scope.updateCandidate = function (candidate) {
			candidate.$update();
			$scope.message = candidate.name + " updated successfully.";
		};

		$scope.addNewEducation = function () {
			console.log("Add new education invoked... ");
		};
		$scope.removeThisEducation = function () {
			console.log("delete education invoked... ");
		};
	})
	.controller('HomeViewController', function () {

	})
	.controller('TabNavigationController', function ($scope) {
		// Init tab index
		this.tab = 0;
		// setting tab index
		this.setTab = function (tabIdx) {
			this.tab = tabIdx;
		};
		// checking tab index
		this.isTab = function (tabIdx) {
			return this.tab === tabIdx;
		};
	})
	.controller('SkinToneOptionsController', function ($scope) {
		$scope.skinTones = [
        "DARK",
        "MODERATE",
        "FAIR"
    ];
	})
	.controller('BodyTypeOptionsController', function ($scope) {
		$scope.bodyTypes = [
        "NORMAL",
        "SLIM",
        "ATHLETIC",
        "HEAVY"
    ];
	})
	.controller('BloodGroupOptionsController', function ($scope) {
		$scope.bloodGroups = [
        "OPOSITIVE",
        "ONEGATIVE",
        "APOSITIVE",
        "ANEGATIVE",
        "BPOSITIVE",
        "BNEGATIVE",
        "ABPOSITIVE",
        "ABNEGATIVE"
    ];
	});