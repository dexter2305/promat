angular.module('apiService', ['ngResource'])

// Booking Resource
.factory("Candidates", function ($resource) {
    return $resource(
        "http://localhost:8080/promat/webapi/candidates/:Id",
        {Id: "@Id" },
        {
            "update": {method: "PUT"}        }
    );
});