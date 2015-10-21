angular.module('apiService', ['ngResource'])


.factory("Candidates", function ($resource) {
    return $resource(
        "http://localhost:8080/promat/webapi/candidates/:id",
        {
            id: "@id"
        },
        {
            "update": {method: "PUT"},
            "query":  {method: "GET", isArray:true},
        }
    );
});