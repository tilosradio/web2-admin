'use strict';

angular.module('tilosAdmin').config(function ($routeProvider) {
  $routeProvider.when('/comments', {
    templateUrl: 'views/comments.html',
    controller: 'CommentsCtrl'
  });
});

angular.module('tilosAdmin').controller('CommentsCtrl', function ($http, API_SERVER_ENDPOINT, $scope) {
  var status = "NEW";
  $http.get(API_SERVER_ENDPOINT + '/api/v1/comment?status=' + status).success(function (data) {
    $scope.comments = data;
  });
  $scope.approve = function (id) {
    $http.post(API_SERVER_ENDPOINT + '/api/v1/comment/' + id + '/approve', {}).success(function (data) {
      $http.get(API_SERVER_ENDPOINT + '/api/v1/comment?status=' + status).success(function (data) {
        $scope.comments = data;
      });
    });
  }

  $scope.delete = function (id) {
    $http.delete(API_SERVER_ENDPOINT + '/api/v1/comment/' + id + '/approve', {}).success(function (data) {
      $http.get(API_SERVER_ENDPOINT + '/api/v1/comment?status=' + status).success(function (data) {
        $scope.comments = data;
      });
    });
  }
});
