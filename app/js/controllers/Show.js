'use strict';

angular.module('tilosAdmin').config(['$routeProvider', function ($routeProvider) {
  $routeProvider.when('/show/:id', {
    templateUrl: 'views/show.html',
    controller: 'ShowCtrl',
    resolve: {
      data: function ($route, Shows, API_SERVER_ENDPOINT, $http) {
        return $http.get(API_SERVER_ENDPOINT + "/api/v1/show/" + $route.current.params.id);
      }
    }
  });
  $routeProvider.when('/shows', {
    templateUrl: 'views/shows.html',
    controller: 'ShowListCtrl',
    resolve: {
      list: function ($route, API_SERVER_ENDPOINT, $http) {
        return $http.get(API_SERVER_ENDPOINT + '/api/v1/show?status=all');
      }
    }
  });
  $routeProvider.when('/edit/show/:id', {
    templateUrl: 'views/show-form.html',
    controller: 'ShowEditCtrl',
    resolve: {
      data: function ($route, Shows) {
        return Shows.get({id: $route.current.params.id});
      }
    }
  });
  $routeProvider.when('/new/show', {
    templateUrl: 'views/show-form.html',
    controller: 'ShowNewCtrl',
  });
}]);

angular.module('tilosAdmin')
  .controller('ShowCtrl', function ($scope, data, API_SERVER_ENDPOINT, $http, $rootScope, $location, Schedulings, Contributions, Shows, Urls, ngDialog, $cacheFactory) {
    data = data.data;
    $scope.show = data;

    $scope.server = API_SERVER_ENDPOINT;
    $scope.now = new Date().getTime();

    $scope.currentShowPage = 0;
    $scope.deleteScheduling = function (id) {
      Schedulings.remove({'id': id});
      $http.get(API_SERVER_ENDPOINT + "/api/v1/show/" + $scope.show.id).success(function (data) {
        $scope.show = data;
      });
    }


    var to = new Date().getTime();
    var from = to - ( 6 * 30 * 24 * 3600 * 1000);
    $http.get(API_SERVER_ENDPOINT + '/api/v1/show/' + data.id + '/episodes?start=' + from + '&end=' + to).success(function (data) {
      data.sort(function (a, b) {
        return b.plannedFrom - a.plannedFrom
      });
      $scope.show.episodes = data;
    });


    $scope.prev = function () {
      $scope.currentShowPage--;
      var to = $scope.show.episodes[$scope.show.episodes.length - 1].plannedFrom - 60000;
      var from = to - 6 * 30 * 24 * 60 * 60 * 1000;
      $http.get(API_SERVER_ENDPOINT + '/api/v1/show/' + data.id + '/episodes?start=' + from + "&end=" + to).success(function (data) {
        $scope.show.episodes = data;
      });

    };
    $scope.next = function () {
      $scope.currentShowPage++;
      var from = $scope.show.episodes[0].plannedTo + 60000;
      var to = from + 6 * 30 * 24 * 60 * 60 * 1000;
      $http.get(API_SERVER_ENDPOINT + '/api/v1/show/' + data.id + '/episodes?start=' + from + "&end=" + to).success(function (data) {
        $scope.show.episodes = data;
      });
    };


    $scope.newEpisode = function (episode) {
      $rootScope.newEpisode = episode;
      $rootScope.newEpisode.show = $scope.show;
      $location.path('/new/episode');
    };

    $scope.newContribution = function () {
      ngDialog.open({
        template: 'views/contribution-form.html',
        controller: 'ContributionNewCtrl',
        scope: $scope
      });
    };

    $scope.deleteContribution = function (contribution) {
      $http.delete(API_SERVER_ENDPOINT + '/api/int/contribution?show=' + $scope.show.id + '&author=' + contribution.author.id).success(function (data) {
        $location.path('/show/' + $scope.show.id);
      });
      $http.get(API_SERVER_ENDPOINT + "/api/v1/show/" + $scope.show.id).success(function (data) {
        $scope.show.contributors = data.contributors;
      });
    };


    $scope.newUrl = function () {
      ngDialog.open({
        template: 'views/url-form.html',
        controller: 'UrlNewCtrl',
        scope: $scope
      });
    };

    $scope.deleteUrl = function (index) {
      $scope.show.urls.splice(index, 1);
      $http.put(API_SERVER_ENDPOINT + '/api/v1/show/' + $scope.show.id, $scope.show).success(function (data) {
        var httpCache = $cacheFactory.get('$http');
        httpCache.remove(server + '/api/v1/show/' + $scope.show.id);
        httpCache.remove(server + '/api/v1/show');
      });
    };

    $scope.newScheduling = function () {
      ngDialog.open({
        template: 'views/scheduling-form.html',
        controller: 'SchedulingNewCtrl',
        scope: $scope
      });
    };

    $scope.deleteScheduling = function (index) {
      $scope.show.schedulings.splice(index, 1);
      $http.put(API_SERVER_ENDPOINT + '/api/v1/show/' + $scope.show.id, $scope.show).success(function (data) {
        var httpCache = $cacheFactory.get('$http');
        httpCache.remove(server + '/api/v1/show/' + $scope.show.id);
        httpCache.remove(server + '/api/v1/show');
      });
    };
  });


angular.module('tilosAdmin')
  .controller('SchedulingNewCtrl', function ($scope, dateFilter, $location, $routeParams, $http, API_SERVER_ENDPOINT, $cacheFactory) {
    $scope.scheduling = {};


    $scope.$watch('scheduling.validFrom', function (date) {
      $scope.validFromDate = dateFilter(new Date(date), 'yyyy-MM-dd')
    });

    $scope.$watch('validFromDate', function (dateString) {
      $scope.scheduling.validFrom = new Date(dateString).getTime();
    });

    $scope.$watch('scheduling.validTo', function (date) {
      $scope.validToDate = dateFilter(new Date(date), 'yyyy-MM-dd')
    });

    $scope.$watch('validToDate', function (dateString) {
      $scope.scheduling.validTo = new Date(dateString).getTime();
    });


    $scope.$watch('scheduling.base', function (date) {
      $scope.baseDate = dateFilter(new Date(date), 'yyyy-MM-dd')
    });

    $scope.$watch('baseDate', function (dateString) {
      $scope.scheduling.base = new Date(dateString).getTime();
    });

    $scope.save = function () {
      var newShow = JSON.parse(JSON.stringify($scope.show))
      newShow.schedulings.push($scope.scheduling);
      newShow.episodes = [];
      $http.put(API_SERVER_ENDPOINT + '/api/v1/show/' + newShow.id, newShow).success(function (data) {
        var httpCache = $cacheFactory.get('$http');
        httpCache.remove(server + '/api/v1/show/' + $scope.show.id);
        httpCache.remove(server + '/api/v1/show');
        $scope.closeThisDialog();
        $http.get(API_SERVER_ENDPOINT + "/api/v1/show/" + $scope.show.id).success(function (data) {
          $scope.show.urls = data.urls;
        });
      });
    }
  });


angular.module('tilosAdmin')
  .controller('UrlNewCtrl', function ($scope, $http, API_SERVER_ENDPOINT, $cacheFactory) {
    $scope.url = {};
    $scope.save = function () {
      var newShow = JSON.parse(JSON.stringify($scope.show))
      newShow.urls.push($scope.url);
      newShow.episodes = [];
      $http.put(API_SERVER_ENDPOINT + '/api/v1/show/' + newShow.id, newShow).success(function (data) {
        var httpCache = $cacheFactory.get('$http');
        httpCache.remove(server + '/api/v1/show/' + $scope.show.id);
        httpCache.remove(server + '/api/v1/show');
        $scope.closeThisDialog();
        $http.get(API_SERVER_ENDPOINT + "/api/v1/show/" + $scope.show.id).success(function (data) {
          $scope.show.urls = data.urls;
        });
      });
    }
  });

angular.module('tilosAdmin').controller('ContributionNewCtrl', function (API_SERVER_ENDPOINT, $scope, $http, Contributions, $location, $cacheFactory) {
  $scope.contribution = {};
  $http.get(API_SERVER_ENDPOINT + "/api/v1/author").success(function (data) {
    $scope.authors = data;
  });
  $scope.save = function () {
    $scope.contribution.show = {"id": $scope.show.id};
    $http.post(server + '/api/int/contribution', $scope.contribution).success(function (data) {
      var httpCache = $cacheFactory.get('$http');
      httpCache.remove(server + '/api/v1/show/' + $scope.show.id);
      httpCache.remove(server + '/api/v1/author/' + $scope.contribution.author.id);
      httpCache.remove(server + '/api/v1/show');
      $location.path('/show/' + $scope.show.id);
      $http.get(API_SERVER_ENDPOINT + "/api/v1/show/" + $scope.show.id).success(function (data) {
        $scope.show.contributors = data.contributors;
      });
    });
  }
});

angular.module('tilosAdmin')
  .controller('ShowListCtrl', function ($scope, list) {
    $scope.shows = list.data;

  });

'use strict';

angular.module('tilosAdmin')
  .controller('ShowNewCtrl', function ($location, $scope, $routeParams, $http, $cacheFactory, Shows, API_SERVER_ENDPOINT) {
    $scope.types = [
      {id: 'SPEECH', 'name': "Beszélgetős"},
      {id: "MUSIC", 'name': "Zenés"}
    ]
    $scope.statuses = [
      {id: 'PLANNED', 'name': "Tervezett"},
      {id: 'ACTIVE', 'name': "Aktív"},
      {id: 'OLD', 'name': "Archív"},
      {id: 'LEGEND', 'name': "Legenda"}
    ]
    $scope.show = {};
    $scope.save = function () {

      Shows.save($scope.show, function (data) {
        var id = data.id;
        $location.path('/show/' + id);
      });

    };
  });

angular.module('tilosAdmin')
  .controller('ShowEditCtrl', ['$location', '$scope', '$routeParams', 'API_SERVER_ENDPOINT', '$http', '$cacheFactory', 'data',
    function ($location, $scope, $routeParams, server, $http, $cacheFactory, data) {
      $scope.types = [
        {id: 'SPEECH', 'name': "Beszélgetős"},
        {id: "MUSIC", 'name': "Zenés"}
      ]
      $scope.statuses = [
        {id: 'PLANNED', 'name': "Tervezett"},
        {id: 'ACTIVE', 'name': "Aktív"},
        {id: 'OLD', 'name': "Archív"},
        {id: 'LEGEND', 'name': "Legenda"}
      ]
      $scope.show = data;
      $scope.save = function () {
        $http.put(server + '/api/v1/show/' + $routeParams.id, $scope.show).success(function (data) {
          var httpCache = $cacheFactory.get('$http');
          httpCache.remove(server + '/api/v1/show/' + $scope.show.id);
          httpCache.remove(server + '/api/v1/show');
          $location.path('/show/' + $scope.show.id);
        });

      }
    }
  ])
;

angular.module('tilosAdmin').factory('Shows', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v1/show/:id', null, {
    'update': {method: 'PUT'}
  });
}]);

