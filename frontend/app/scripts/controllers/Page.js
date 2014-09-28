'use strict';

angular.module('tilosApp').config(function ($stateProvider) {
    $stateProvider.state('page', {
        url: '/page/:id',
        templateUrl: 'partials/page.html',
        controller: 'PageCtrl'
    });
});

angular.module('tilosApp').controller('PageCtrl', function ($scope, API_SERVER_ENDPOINT, $stateParams, $http) {
    $http.get(API_SERVER_ENDPOINT + '/api/v0/text/' + $stateParams.id).success(function (data) {
        $scope.page = data;
    });
});