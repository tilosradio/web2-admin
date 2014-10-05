'use strict';

angular.module('tilosAdmin').config(function ($routeProvider) {

  $routeProvider.when('/new/contribution', {
    templateUrl: 'views/contribution-form.html',
    controller: 'ContributionNewCtrl',


  });

});


angular.module('tilosAdmin').controller('ContributionNewCtrl', function (API_SERVER_ENDPOINT, $scope, $http, Contributions, $location) {
  $scope.contribution = {};
  $http.get(API_SERVER_ENDPOINT + "/api/v0/author").success(function (data) {
    $scope.authors = data;
  });
  $http.get(API_SERVER_ENDPOINT + "/api/v0/show").success(function (data) {
    $scope.shows = data;
  });
  $scope.save = function () {
    Contributions.save($scope.contribution, function (data) {
      $location.path('/show/' + $scope.contribution.show.id);
    });
  }
});


angular.module('tilosAdmin').factory('Contributions', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v0/contribution/:id', null, {
    'update': { method: 'PUT'}
  });
}]);

