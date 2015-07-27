'use strict';

angular.module('tilosAdmin').controller('AuthCtrl', function ($rootScope, $scope, API_SERVER_ENDPOINT, $http, $location, localStorageService) {
    $scope.logout = function () {
        localStorageService.unset("jwt");
    };
});