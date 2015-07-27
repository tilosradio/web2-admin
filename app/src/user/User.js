'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('user', {
    url: '/user/:id',
    templateUrl: 'user/user.html',
    controller: 'UserCtrl'
  });
  $stateProvider.state('userEdit', {
    url: '/edit/user/:id',
    templateUrl: 'user/user-form.html',
    controller: 'UserEditCtrl',
    resolve: {
      data: function ($stateParams, Users) {
        return Users.get({id: $stateParams.id});
      },
    }
  });
  $stateProvider.state('users', {
    url: '/userlist',
    templateUrl: 'user/userlist.html',
    controller: 'UserListCtrl'
  });
});


angular.module('tilosAdmin')
  .controller('UserListCtrl', function ($scope, $http, API_SERVER_ENDPOINT) {
    $http.get(API_SERVER_ENDPOINT + '/api/int/user').success(function (data) {
      $scope.users = data
    })

  });

angular.module('tilosAdmin')
  .controller('UserEditCtrl', function ($location, $scope, $stateParams, API_SERVER_ENDPOINT, $http, $cacheFactory, data, $rootScope) {
    $scope.user = data;
    $scope.save = function () {
      $http.put(API_SERVER_ENDPOINT + '/api/v0/user/' + $stateParams.id, $scope.user).success(function (data) {
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
    'update': {method: 'PUT'}
  });
}]);
