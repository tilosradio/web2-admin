'use strict';

angular.module('tilosAdmin').config(['$routeProvider', function ($routeProvider) {
  $routeProvider.when('/author/:id', {
    templateUrl: 'views/author.html',
    controller: 'AuthorCtrl',
    resolve: {
      data: function ($route, Authors) {
        return Authors.get({id: $route.current.params.id});
      },
    }});
  $routeProvider.when('/authors', {
    templateUrl: 'views/authors.html',
    controller: 'AuthorListCtrl',
    resolve: {
      list: function ($route, Authors) {
        return Authors.query();
      },
    }});
  $routeProvider.when('/edit/author/:id', {
    templateUrl: 'views/author-form.html',
    controller: 'AuthorEditCtrl',
    resolve: {
      data: function ($route, Authors) {
        return Authors.get({id: $route.current.params.id});
      },
    }});
  $routeProvider.when('/new/author', {
    templateUrl: 'views/author-form.html',
    controller: 'AuthorNewCtrl',

  });
}]);

angular.module('tilosAdmin')
    .controller('AuthorCtrl', function ($scope, data) {
      $scope.author = data;
    });

angular.module('tilosAdmin')
    .controller('AuthorListCtrl', ['$scope', 'list', function ($scope, list) {
      $scope.authors = list;

    }]);

'use strict';

angular.module('tilosAdmin')
    .controller('AuthorNewCtrl', function ($location, $scope, $routeParams, $http, $cacheFactory, Authors, API_SERVER_ENDPOINT) {
      $scope.author = {};
      $scope.author.introduction = "";
      $scope.save = function () {

        Authors.save($scope.author, function (data) {
          var id = data.data.id;
          var httpCache = $cacheFactory.get('$http');
          httpCache.remove(API_SERVER_ENDPOINT + '/api/v0/author/' + id);
          $location.path('/author/' + id);
        });

      };
    });

angular.module('tilosAdmin')
    .controller('AuthorEditCtrl', ['$location', '$scope', '$routeParams', 'API_SERVER_ENDPOINT', '$http', '$cacheFactory', 'data',
      function ($location, $scope, $routeParams, server, $http, $cacheFactory, data) {
        $scope.author = data;
        $scope.save = function () {
          $http.put(server + '/api/v0/author/' + $routeParams.id, $scope.author).success(function (data) {
            var httpCache = $cacheFactory.get('$http');
            httpCache.remove(server + '/api/v0/author/' + $scope.author.id);
            $location.path('/author/' + $scope.author.id);
            $scope.error = "";
          }).error(function (data) {
                if (data.error) {
                  $scope.error = data.error;
                } else {
                  $scope.error = "Unknown server error";
                }
              });

        }
      }
    ])
;

angular.module('tilosAdmin').factory('Authors', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v0/author/:id', null, {
    'update': { method: 'PUT'}
  });
}]);

