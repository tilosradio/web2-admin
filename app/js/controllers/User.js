'use strict';

angular.module('tilosAdmin').config(function ($routeProvider) {
  $routeProvider.when('/user/:id', {
    templateUrl: 'views/user.html',
    controller: 'UserCtrl'
  });
  $routeProvider.when('/edit/user/:id', {
    templateUrl: 'views/user-form.html',
    controller: 'UserEditCtrl',
    resolve: {
      data: function ($route, Users) {
        return Users.get({id: $route.current.params.id});
      },
    }});
});


angular.module('tilosAdmin')
    .controller('UserEditCtrl', function ($location, $scope, $routeParams, API_SERVER_ENDPOINT, $http, $cacheFactory, data, $rootScope) {
      $scope.user = data;
      $scope.save = function () {
        $http.put(API_SERVER_ENDPOINT + '/api/v0/user/' + $routeParams.id, $scope.user).success(function (data) {
          var httpCache = $cacheFactory.get('$http');
          httpCache.remove(API_SERVER_ENDPOINT + '/api/v0/user/' + $scope.user.id);
          $http.get(API_SERVER_ENDPOINT + '/api/v0/user/me').success(function (data) {
            $rootScope.user = data;
            $location.path('/user/' + $scope.user.id);
          });
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
)
;

angular.module('tilosAdmin')
    .controller('UserCtrl', function ($scope) {
    });

angular.module('tilosAdmin').factory('Users', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v0/user/:id', null, {
    'update': { method: 'PUT'}
  });
}]);
