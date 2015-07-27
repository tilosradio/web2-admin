'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('author', {
    url: '/author/:id',
    templateUrl: 'author/author.html',
    controller: 'AuthorCtrl',
    resolve: {
      data: function ($stateParams, Authors) {
        return Authors.get({id: $stateParams.id});
      },
    }});

  $stateProvider.state('authors',{
    url:'/authors',
    templateUrl: 'author/authors.html',
    controller: 'AuthorListCtrl',
    resolve: {
      list: function (Authors) {
        return Authors.query();
      },
    }});

  $stateProvider.state('authorEdit',{
    url:'/edit/author/:id',
    templateUrl: 'author/author-form.html',
    controller: 'AuthorEditCtrl',
    resolve: {
      data: function ($stateParams, Authors) {
        return Authors.get({id: $stateParams.id});
      },
    }});

  $stateProvider.state('authorNew',{
    url:'/new/author',
    templateUrl: 'author/author-form.html',
    controller: 'AuthorNewCtrl',
  });
});

angular.module('tilosAdmin').controller('AuthorCtrl', function ($scope, data) {
      $scope.author = data;
    });

angular.module('tilosAdmin').controller('AuthorListCtrl', ['$scope', 'list', function ($scope, list) {
      $scope.authors = list;
    }]);

angular.module('tilosAdmin')
    .controller('AuthorNewCtrl', function ($location, $scope, $stateParams, $http, $cacheFactory, Authors, API_SERVER_ENDPOINT) {
      $scope.author = {};
      $scope.author.introduction = "";
      $scope.save = function () {

        Authors.save($scope.author, function (data) {
          var id = data.id;
          var httpCache = $cacheFactory.get('$http');
          httpCache.remove(API_SERVER_ENDPOINT + '/api/v1/author/' + id);
          $location.path('/author/' + id);
        });

      };
    });

angular.module('tilosAdmin')
    .controller('AuthorEditCtrl', function ($location, $scope, $stateParams, API_SERVER_ENDPOINT, $http, $cacheFactory, data) {
        $scope.edit = true;
        $scope.author = data;
        $scope.save = function () {
          $http.put(API_SERVER_ENDPOINT + '/api/v1/author/' + $stateParams.id, $scope.author).success(function (data) {
            var httpCache = $cacheFactory.get('$http');
            httpCache.remove(server + '/api/v1/author/' + $scope.author.id);
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
    );

angular.module('tilosAdmin').factory('Authors', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v1/author/:id', null, {
    'update': { method: 'PUT'}
  });
}]);

