'use strict';

angular.module('tilosApp').config(function ($stateProvider) {
    $stateProvider.state('author', {
        url: '/author/:id',
        templateUrl: 'partials/author.html',
        controller: 'AuthorCtrl'
    });
});

angular.module('tilosApp').controller('AuthorCtrl', function ($scope, $rootScope, $stateParams, API_SERVER_ENDPOINT, $http) {
    $http.get(API_SERVER_ENDPOINT + '/api/v0/author/' + $stateParams.id, {cache: true}).success(function (data) {
      $scope.author = data;
      $rootScope.pageTitle = data.name;

    });
  });