'use strict';

angular.module('tilosApp').config(function ($routeProvider) {
    $routeProvider.when('/search/:id', {
        templateUrl: 'partials/search.html',
        controller: 'SearchCtrl'
    });
});
angular.module('tilosApp')
    .controller('SearchCtrl', function ($scope, $rootScope, $routeParams, API_SERVER_ENDPOINT, $http) {
        $http.get(API_SERVER_ENDPOINT + '/api/v1/search/query?q=' + $routeParams.id, {cache: true}).success(function (data) {
            $scope.result = data;
        });
    });

angular.module("tilosApp").controller("SearchBox", function ($scope, $location) {
    $scope.search = function () {
        $location.path('search/' + $scope.term);
    };
});



