'use strict';

angular.module('tilosAdmin').config(function ($routeProvider) {
  $routeProvider.when('/scheduling/:id', {
    templateUrl: 'views/scheduling.html',
    controller: 'SchedulingCtrl',
    resolve: {
      scheduling: function ($route, Schedulings) {
        return Schedulings.get({show: $route.current.params.show, id: $route.current.params.id});

      }
    }});
  $routeProvider.when('/edit/scheduling/:show/:id', {
    templateUrl: 'views/scheduling-form.html',
    controller: 'SchedulingEditCtrl',
    resolve: {
      data: function ($route, Schedulings) {
        return Schedulings.get({id: $route.current.params.id});
      }
    }});
  $routeProvider.when('/new/scheduling/:show', {
    templateUrl: 'views/scheduling-form.html',
    controller: 'SchedulingNewCtrl',
  });
});
angular.module('tilosAdmin')
    .controller('SchedulingCtrl', ['$scope', 'scheduling', function ($scope, scheduling) {
      $scope.scheduling = scheduling;

    }]);

angular.module('tilosAdmin').filter('weekDayName', function () {
  return function (input) {
    return ['Hétfő', "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"][input];
  };
});
angular.module('tilosAdmin').factory('Schedulings', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v1/scheduling/:id', null, {
    'update': { method: 'PUT'}
  });
}]);

