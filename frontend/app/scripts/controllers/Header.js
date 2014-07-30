'use strict';

angular.module('tilosApp')
    .controller('HeaderCtrl', function ($scope, $location) {
        $scope.isCollapsed = false;
        $scope.searchTerm = '';
        $scope.search = function () {
            $location.path('/search/' + $scope.searchTerm);
        };
    });
