'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('news', {
    url: '/news',
    templateUrl: 'news/news.html',
    controller: 'NewsCtrl'
  });
  $stateProvider.state('news-day', {
    url: '/news/:year/:month/:day',
    templateUrl: 'news/news.html',
    controller: 'NewsCtrl'
  });
  $stateProvider.state('block', {
    url: '/news/block/:year/:month/:day/:name',
    templateUrl: 'news/block.html',
    controller: 'NewsBlockCtrl'
  });

});


angular.module('tilosAdmin').directive('fileModel', ['$parse', function ($parse) {
  return {
    restrict: 'A',
    link: function (scope, element, attrs) {
      var model = $parse(attrs.fileModel);
      var modelSetter = model.assign;

      element.bind('change', function () {
        scope.$apply(function () {
          modelSetter(scope, element[0].files[0]);
        });
      });
    }
  };
}]);


angular.module('tilosAdmin').controller('NewsBlockCtrl', function ($http, API_SERVER_ENDPOINT, $scope, $stateParams, ngAudio) {
  $scope.now = new Date().getTime() / 1000;
  $scope.Math = window.Math;

  $scope.ready = true;
  var dateStr = $stateParams.year + '-' + $stateParams.month + '-' + $stateParams.day;
  var name = $stateParams.name;
  var load = function () {
    $http.get(API_SERVER_ENDPOINT + '/api/v1/news/block/' + dateStr + '/' + $stateParams.name).success(function (data) {
      $scope.block = data;
      if ($scope.block.path) {
        $scope.sound = ngAudio.load("http://hir.tilos.hu/kesz/" + $scope.block.path);
      }
    });
  };

  $scope.playing = false;
  $scope.play = function (real) {
    if (!$scope.playing) {
      $scope.sound.play();
      $scope.playing = true;
      if (real) {
        $http.post(API_SERVER_ENDPOINT + '/api/v1/news/block/' + dateStr + '/' + name + '/play').success(function (data) {
          $scope.block = data;
        });
      }
    } else {
      $scope.sound.stop();
      $scope.playing = false;
    }
  };
  load();

  $scope.generate = function (name) {
    $scope.ready = false;
    $http.post(API_SERVER_ENDPOINT + '/api/v1/news/block/' + dateStr + '/' + name + '?generate=true').success(function (data) {
      load();
      $scope.ready = true;
    });
  };


  $scope.draw = function (name) {
    $http.post(API_SERVER_ENDPOINT + '/api/v1/news/block/' + dateStr + '/' + name).success(function (data) {
      $scope.sound = null;
      load();
    });
  }
});
angular.module('tilosAdmin').controller('NewsCtrl', function ($http, API_SERVER_ENDPOINT, $scope, $stateParams) {
    $scope.selectedDate = new Date();
    if ($stateParams.year) {
      $scope.selectedDate = new Date($stateParams.year, $stateParams.month - 1, $stateParams.day, 12, 0, 0);
    }
    $scope.ready = true;
    $scope.now = new Date().getTime() / 1000;

    $scope.current = $scope.selectedDate.getTime();

    var dateToStr = function (dt) {
      var monthStr = ('0' + (dt.getMonth() + 1)).slice(-2);
      var dayStr = ('0' + dt.getDate()).slice(-2);
      var yearStr = '' + dt.getFullYear();

      return yearStr + '-' + monthStr + '-' + dayStr
    }
    var dateStr = dateToStr($scope.selectedDate);

    var loadFiles = function () {
      $http.get(API_SERVER_ENDPOINT + '/api/v1/news/file').success(function (data) {
        $scope.files = data;
      });
    }
    loadFiles();

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

    $scope.uploadFile = function () {
      var fd = new FormData();
      fd.append('newsfile', $scope.myFile);
      fd.append('category', $scope.category);
      $http.post(API_SERVER_ENDPOINT + "/api/v1/news/upload", fd, {
        transformRequest: angular.identity,
        headers: {
          'Content-Type': undefined
        }
      }).success(function () {
        alert("File uploaded successfully");
        loadFiles();
      }).error(function () {
        alert("Upload error")
      });
    }
  }
);

