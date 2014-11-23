'use strict';

angular.module('tilosAdmin').config(function ($routeProvider) {
  $routeProvider.when('/stat/listener', {
    templateUrl: 'views/stat-listener.html',
    controller: 'StatListenerCtrl'
  });
});

angular.module('tilosAdmin').controller('StatListenerCtrl', function ($scope, $http, API_SERVER_ENDPOINT) {
  $http.get(API_SERVER_ENDPOINT + "/api/v1/stat/listener").success(function (data) {
    $scope.listeners = data;
  });
});
