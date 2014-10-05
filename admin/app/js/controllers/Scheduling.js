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
angular.module('tilosAdmin')
    .controller('SchedulingEditCtrl', ['$scope', 'data', 'Schedulings', 'dateFilter', '$location', '$routeParams', function ($scope, data, resource, dateFilter, $location, $routeParams) {
      $scope.scheduling = data;

      data.$promise.then(
          function (data) {
            $scope.$watch('scheduling.validFrom', function (date) {
              $scope.validFromDate = dateFilter(new Date(date * 1000), 'yyyy-MM-dd')
            });

            $scope.$watch('validFromDate', function (dateString) {
              $scope.scheduling.validFrom = new Date(dateString).getTime() / 1000;
            });

            $scope.$watch('scheduling.validTo', function (date) {
              $scope.validToDate = dateFilter(new Date(date * 1000), 'yyyy-MM-dd')
            });

            $scope.$watch('validToDate', function (dateString) {
              $scope.scheduling.validTo = new Date(dateString).getTime() / 1000;
            });
            $scope.$watch('scheduling.base', function (date) {
              $scope.baseDate = dateFilter(new Date(date * 1000), 'yyyy-MM-dd')
            });

            $scope.$watch('baseDate', function (dateString) {
              $scope.scheduling.base = new Date(dateString).getTime() / 1000;
            });


          }
      );

      $scope.save = function () {
        resource.update({show: $routeParams.show, id: $scope.scheduling.id}, $scope.scheduling, function (data) {
          $location.path('/show/' + $routeParams.show);
        });
      }
    }]);

angular.module('tilosAdmin')
    .controller('SchedulingNewCtrl', ['$scope', 'Schedulings', 'dateFilter', '$location', '$routeParams', function ($scope, resource, dateFilter, $location, $routeParams) {
      $scope.scheduling = {};


      $scope.$watch('scheduling.validFrom', function (date) {
        $scope.validFromDate = dateFilter(new Date(date * 1000), 'yyyy-MM-dd')
      });

      $scope.$watch('validFromDate', function (dateString) {
        $scope.scheduling.validFrom = new Date(dateString).getTime() / 1000;
      });

      $scope.$watch('scheduling.validTo', function (date) {
        $scope.validToDate = dateFilter(new Date(date * 1000), 'yyyy-MM-dd')
      });

      $scope.$watch('validToDate', function (dateString) {
        $scope.scheduling.validTo = new Date(dateString).getTime() / 1000;
      });


      $scope.$watch('scheduling.base', function (date) {
        $scope.baseDate = dateFilter(new Date(date * 1000), 'yyyy-MM-dd')
      });

      $scope.$watch('baseDate', function (dateString) {
        $scope.scheduling.base = new Date(dateString).getTime() / 1000;
      });

      $scope.save = function () {
        $scope.scheduling.showId = $routeParams.show;
        resource.save({show: $routeParams.show}, $scope.scheduling, function (data) {
          $location.path('/show/' + $routeParams.show);
        });
      }
    }]);


angular.module('tilosAdmin').filter('weekDayName', function () {
  return function (input) {
    return ['Hétfő', "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"][input];
  };
});
angular.module('tilosAdmin').factory('Schedulings', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v0/scheduling/:id', null, {
    'update': { method: 'PUT'}
  });
}]);

