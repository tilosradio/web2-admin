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
    $scope.ready = true;
    $scope.now = new Date().getTime() / 1000;
    var monthStr = ('0' + ($scope.selectedDate.getMonth() + 1)).slice(-2);
    var dayStr = ('0' + $scope.selectedDate.getDate()).slice(-2);
    var yearStr = '' + $scope.selectedDate.getFullYear();

    var dateStr = yearStr + '-' + monthStr + '-' + dayStr

    $http.get(API_SERVER_ENDPOINT + '/api/v1/news/file').success(function (data) {
      $scope.files = data;
    });

    var loadBlocks = function () {
      $http.get(API_SERVER_ENDPOINT + '/api/v1/news/block/' + dateStr).success(function (data) {
        $scope.blocks = data;
      });
    }
    loadBlocks();

    $scope.generate = function (name) {
      $scope.ready = false;
      $http.post(API_SERVER_ENDPOINT + '/api/v1/news/block/' + dateStr + '/' + name + '?generate=true').success(function (data) {
        loadBlocks();
        $scope.ready = true;
      });
    }


    $scope.draw = function (name) {
      $http.post(API_SERVER_ENDPOINT + '/api/v1/news/block/' + dateStr + '/' + name).success(function (data) {
        loadBlocks();
      });
    }

  }
);

