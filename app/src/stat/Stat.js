'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('statListener', {
    url: '/stat/listener',
    templateUrl: 'stat/stat-listener.html',
    controller: 'StatListenerCtrl'
  });
});

angular.module('tilosAdmin').controller('StatListenerCtrl', function ($scope, $http, API_SERVER_ENDPOINT, $location) {
  $scope.from = new Date();
  $scope.to = new Date();
  $scope.from.setTime($scope.to.getTime() - 1000 * 60 * 60 * 24 * 7);

  $scope.calculate = function () {
    $http.get(API_SERVER_ENDPOINT + "/api/v1/stat/listener?from=" + $scope.from.getTime() + "&to=" + $scope.to.getTime()).success(function (data) {
      $scope.listeners = data;
    });
  };

});
