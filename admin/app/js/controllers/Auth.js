'use strict';

angular.module('tilosAdmin').controller('AuthCtrl', function ($rootScope, $scope, $routeParams, API_SERVER_ENDPOINT, $http, $location, localStorageService) {
    $scope.logout = function () {
        localStorageService.unset("jwt");
    };
});