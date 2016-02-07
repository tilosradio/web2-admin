'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('news', {
    url: '/news',
    templateUrl: 'news/news.html',
    controller: 'NewsCtrl'
  });
  $stateProvider.state('news-dayview-today', {
    url: '/news/day',
    templateUrl: 'news/news-today.html',
    controller: 'NewsTodayCtrl'
  });
  $stateProvider.state('news-day', {
    url: '/news/:year/:month/:day',
    templateUrl: 'news/news.html',
    controller: 'NewsCtrl'
  });
  $stateProvider.state('news-dayview', {
    url: '/news/day/:year/:month/:day',
    templateUrl: 'news/news-today.html',
    controller: 'NewsTodayCtrl'
  });
  $stateProvider.state('news-file', {
    url: '/news/file/:id',
    templateUrl: 'news/file.html',
    controller: 'NewsFileCtrl'
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


angular.module('tilosAdmin').controller('NewsFileCtrl', function ($http, API_SERVER_ENDPOINT, $scope, $stateParams, ngAudio, $state) {
  var load = function () {
    $http.get(API_SERVER_ENDPOINT + '/api/v1/news/file/' + $stateParams.id).success(function (data) {
      $scope.file = data;
    });
  };

  $scope.modifyDate = function (fieldName, no) {
    $scope.file[fieldName] = $scope.file[fieldName] + 24 * 60 * 60 * no;
    $http.put(API_SERVER_ENDPOINT + '/api/v1/news/file/' + $stateParams.id, $scope.file).success(function (data) {
      $scope.file = data;

    });
  };

  $scope.delete = function () {
    $http.delete(API_SERVER_ENDPOINT + '/api/v1/news/file/' + $scope.file.id).success(function () {
      $state.go("news");
    });
  };
  load();

});
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

angular.module('tilosAdmin').controller('NewsTodayCtrl', function ($http, API_SERVER_ENDPOINT, $scope, $stateParams) {
  $scope.selectedDate = new Date();
  if ($stateParams.year) {
    $scope.selectedDate = new Date($stateParams.year, $stateParams.month - 1, $stateParams.day, 0, 0, 0);
  } else {
    $scope.selectedDate.setHours(0);
    $scope.selectedDate.setMinutes(0);
    $scope.selectedDate.setSeconds(0);
  }

  $scope.current = $scope.selectedDate.getTime();

  var zeroDate = new Date($scope.selectedDate.getTime());
  zeroDate.setHours(5);
  zeroDate.setMinutes(0);
  zeroDate.setSeconds(0);
  zeroDate.setMilliseconds(0);


  var dateToStr = function (dt) {
    var monthStr = ('0' + (dt.getMonth() + 1)).slice(-2);
    var dayStr = ('0' + dt.getDate()).slice(-2);
    var yearStr = '' + dt.getFullYear();
    return yearStr + '-' + monthStr + '-' + dayStr
  };
  var dateStr = dateToStr($scope.selectedDate);
  var loadBlocks = function () {
    $http.get(API_SERVER_ENDPOINT + '/api/v1/news/block/' + dateStr).success(function (data) {
      $scope.blocks = data;
      for (var i = 0; i < data.length; i++) {
        var offset = ($scope.blocks[i].date * 1000 - zeroDate.getTime()) / (30 * 60 * 1000);
        var t = Math.round(offset) - offset;

        var offset = Math.round(offset) - t * 3;

        $scope.blocks[i].top = offset * 60;
        $scope.blocks[i].height = ($scope.blocks[i].expectedDuration / 60 / 15 * 30) * 3;
        if ($scope.blocks[i].name.indexOf("havolt") > 0) {
          $scope.blocks[i].left = 700;
        } else {
          $scope.blocks[i].left = 270;
        }
      }
    });
  };
  var start = $scope.selectedDate.getTime() + 5 * 60 * 60 * 1000;
  var end = start + 19 * 60 * 60 * 1000;
  $http.get(API_SERVER_ENDPOINT + '/api/v1/episode?start=' + start + "&end=" + end).success(function (data) {
    $scope.episodes = data;
    $scope.episodes.forEach(function (episode) {
      var hourOffset = (episode.plannedFrom - zeroDate.getTime()) / (1000.0 * 60 * 60 )

      episode.top = hourOffset * 120;
    });
  });
  $scope.hours = [];
  for (var hour = 5; hour < 24; hour++) {
    var h = {};
    h.label = "" + hour + ":00";
    h.position = (hour - 5) * 4 * 30;
    $scope.hours.push(h);
  }
  loadBlocks();

});

angular.module('tilosAdmin').controller('NewsCtrl', function ($http, API_SERVER_ENDPOINT, $scope, $stateParams) {
    $scope.selectedDate = new Date();
    if ($stateParams.year) {
      $scope.selectedDate = new Date($stateParams.year, $stateParams.month - 1, $stateParams.day, 12, 0, 0);
    }
    $scope.ready = true;
    $scope.now = new Date().getTime() / 1000;

    $scope.current = $scope.selectedDate.getTime();


    $scope.delete = function (id) {
      $http.delete(API_SERVER_ENDPOINT + '/api/v1/news/file/' + id).success(function () {
        loadFiles();
      });
    };

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

