'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('news', {
    url: '/news',
    templateUrl: 'news/news.html',
    controller: 'NewsCtrl'
  });
  $stateProvider.state('news-day', {
    url: '/news/:year/:month/$day',
    templateUrl: 'news/news.html',
    controller: 'NewsCtrl'
  });

});


angular.module('tilosAdmin').controller('NewsCtrl', function ($http, API_SERVER_ENDPOINT, $scope, $stateParams) {
    $scope.selectedDate = new Date();
    if ($stateParams.year) {
      $scope.selectedDate = new Date($stateParams.year, $stateParams.month - 1, $stateParams.day, 12, 0, 0);
    }

    var monthStr = ('0' + ($scope.selectedDate.getMonth() + 1)).slice(-2);
    var dayStr = ('0' + $scope.selectedDate.getDate()).slice(-2);
    var yearStr = '' + $scope.selectedDate.getFullYear();


    $http.get(API_SERVER_ENDPOINT + '/api/v1/news/file').success(function (data) {
      $scope.files = data;
    });

    $http.get(API_SERVER_ENDPOINT + '/api/v1/news/block/' + yearStr + '-' + monthStr + '-' + dayStr).success(function (data) {
      $scope.blocks = data;
    });

  }
);

