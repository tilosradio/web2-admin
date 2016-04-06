'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {

  $stateProvider.state('episode', {
    url: '/episode/:id',
    templateUrl: 'episode/episode.html',
    controller: 'EpisodeCtrl',
    resolve: {
      data: function ($stateParams, Episodes) {
        return Episodes.get({id: $stateParams.id});
      }
    }
  });

  $stateProvider.state('episodeNew', {
    url: '/new/episode',
    templateUrl: 'episode/episode-form.html',
    controller: 'EpisodeNewCtrl'
  });

  $stateProvider.state('episodeEdit', {
    url: '/edit/episode/:id',
    templateUrl: 'episode/episode-form.html',
    controller: 'EpisodeEditCtrl'
  });
});

angular.module('tilosAdmin')
  .controller('EpisodeCtrl', function ($scope, Episodes, data, $sce, $state, API_SERVER_ENDPOINT, $http) {
    $scope.episode = data;
    $scope.delete = function (id) {
      $http.delete(API_SERVER_ENDPOINT + '/api/v1/episode/' + id).success(function (data) {
        $state.go('show.episodes', {id: $scope.episode.show.id});
      });
    };
    data.$promise.then(function (x) {
      $scope.episode.text.formatted = $sce.trustAsHtml(x.text.formatted);
    });
  });

angular.module('tilosAdmin').factory('dateUtil', function () {
  return {
    toHourMin: function (epoch) {
      var d = new Date();
      d.setTime(epoch);
      var result = "" + (d.getHours() < 10 ? "0" : "" ) + d.getHours() + ':' + (d.getMinutes() < 10 ? "0" : "") + d.getMinutes() + ":" + (d.getSeconds() < 10 ? "0" : "") + d.getSeconds();
      return result;
    },
    setDate: function (dateEpoch, dateStr) {
      var date = new Date();
      date.setTime(dateEpoch);
      var parts = dateStr.split(':');
      date.setHours(parseInt(parts[0], 10));
      date.setMinutes(parseInt(parts[1], 10));
      if (parts.length > 2) {
        date.setSeconds(parseInt(parts[2], 10));
      }
      return date.getTime();
    }
  };
})

angular.module('tilosAdmin')
  .controller('EpisodeEditCtrl', function ($location, $scope, $stateParams, API_SERVER_ENDPOINT, $http, $cacheFactory, $rootScope, dateUtil, $state) {


    var id = $stateParams.id;
    $scope.now = new Date().getTime();

    $http.get(API_SERVER_ENDPOINT + '/api/v1/show', {'cache': true}).success(function (data) {
      $scope.shows = data;//
    });

    $http.get(server + '/api/v1/episode/' + id).success(function (data) {
      $scope.episode = data;
      if (data.text && (data.text.format == 'legacy' || data.text.format == 'default')) {
        data.text.content = toMarkdown(data.text.content);
      }
      $scope.episode.show = {id: $scope.episode.show.id}
      $scope.episode.id = id;
      if (!$scope.episode.text || $scope.episode.text.length == 0) {
        $scope.episode.text = {};
      }

      if ($scope.episode.realTo - $scope.episode.plannedTo == 30 * 60 * 1000) {
        $scope.episode.realTo = $scope.episode.plannedTo;
      }
      $scope.realTo = dateUtil.toHourMin($scope.episode.realTo);
      $scope.realFrom = dateUtil.toHourMin($scope.episode.realFrom);


    });

    $scope.save = function () {
      $scope.episode.realFrom = dateUtil.setDate($scope.episode.plannedFrom, $scope.realFrom);
      $scope.episode.realTo = dateUtil.setDate($scope.episode.plannedTo, $scope.realTo);
      $http.put(API_SERVER_ENDPOINT + '/api/v1/episode/' + $scope.episode.id, $scope.episode).success(function (data) {
        var httpCache = $cacheFactory.get('$http');
        httpCache.remove(server + '/api/v1/episode/' + $scope.episode.id);
        $location.path('/episode/' + $scope.episode.id);
      }).error(function (data) {
        if (data.error) {
          $scope.error = data.error;
        } else {
          $scope.error = "Unknown.error";
        }
      });


    };


  })
;

angular.module('tilosAdmin')
  .controller('EpisodeNewCtrl', function ($location, $scope, API_SERVER_ENDPOINT, $http, $cacheFactory, $rootScope, dateUtil) {

    $scope.episode = $rootScope.newEpisode;
    $scope.episode.text = {};
    $scope.episode.show = {id: $scope.episode.show.id}
    $scope.now = new Date().getTime();

    if ($scope.episode.realTo - $scope.episode.plannedTo == 30 * 60 * 1000) {
      $scope.episode.realTo = $scope.episode.plannedTo;
    }
    $scope.realTo = dateUtil.toHourMin($scope.episode.plannedTo);
    $scope.realFrom = dateUtil.toHourMin($scope.episode.plannedFrom)
    $scope.save = function () {
      $scope.episode.realFrom = dateUtil.setDate($scope.episode.plannedFrom, $scope.realFrom);
      $scope.episode.realTo = dateUtil.setDate($scope.episode.plannedTo, $scope.realTo);
      $http.post(API_SERVER_ENDPOINT + '/api/v1/episode', $scope.episode).success(function (data) {
        $location.path('/episode/' + data.id);
      }).error(function (data) {
        if (data.error) {
          $scope.error = data.error;
        } else {
          $scope.error = "Unknown.error";
        }
      });

    };


  });
;

angular.module('tilosAdmin').factory('Episodes', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v1/episode/:id', null, {
    'update': {method: 'PUT'}
  });
}]);
