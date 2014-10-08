'use strict';

angular.module('tilosAdmin').config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/episode/:id', {
        templateUrl: 'views/episode.html',
        controller: 'EpisodeCtrl',
        resolve: {
            data: function ($route, Episodes) {
                return Episodes.get({id: $route.current.params.id});
            },
        }

    });
    $routeProvider.when('/new/episode', {
        templateUrl: 'views/episode-form.html',
        controller: 'EpisodeNewCtrl'
    });
    $routeProvider.when('/edit/episode/:id', {
        templateUrl: 'views/episode-form.html',
        controller: 'EpisodeEditCtrl'
    });
}]);
angular.module('tilosAdmin')
    .controller('EpisodeCtrl', function ($scope, Episodes, $routeParams, data, $sce) {
        $scope.episode = data;
        data.$promise.then(function (x) {
            $scope.episode.text.formatted = $sce.trustAsHtml(x.text.formatted);
        });
    });

angular.module('tilosAdmin').factory('dateUtil', function () {
    return {
        toHourMin: function (epoch) {
            var d = new Date();
            d.setTime(epoch * 1000);
            var result = "" + (d.getHours() < 10 ? "0" : "" ) + d.getHours() + ':' + (d.getMinutes() < 10 ? "0" : "") + d.getMinutes() + ":" + (d.getSeconds() < 10 ? "0" : "") + d.getSeconds();
            return result;
        },
        setDate: function (dateEpoch, dateStr) {
            var date = new Date();
            date.setTime(dateEpoch * 1000);
            var parts = dateStr.split(':');
            date.setHours(parseInt(parts[0], 10));
            date.setMinutes(parseInt(parts[1], 10));
            if (parts.length > 2) {
                date.setSeconds(parseInt(parts[2], 10));
            }
            return date.getTime() / 1000;
        }
    };
})

angular.module('tilosAdmin')
    .controller('EpisodeEditCtrl', function ($location, $scope, $routeParams, API_SERVER_ENDPOINT, $http, $cacheFactory, $rootScope, dateUtil) {


        var id = $routeParams.id;
        $scope.now = new Date().getTime();


        $http.get(server + '/api/v0/episode/' + id).success(function (data) {
            $scope.episode = data;
            $scope.episode.id = id;
            $scope.show = data['show'];
            $scope.episode.show = null;
            if (!$scope.episode.text || $scope.episode.text.length == 0) {
               $scope.episode.text = {};
            }

            $scope.realTo = dateUtil.toHourMin($scope.episode.realTo);
            $scope.realFrom = dateUtil.toHourMin($scope.episode.realFrom);

        });

        $scope.save = function () {
            $scope.episode.realFrom = dateUtil.setDate($scope.episode.plannedFrom, $scope.realFrom);
            $scope.episode.realTo = dateUtil.setDate($scope.episode.plannedTo, $scope.realTo);
            $http.put(API_SERVER_ENDPOINT + '/api/v0/episode/' + $scope.episode.id, $scope.episode).success(function (data) {

                var httpCache = $cacheFactory.get('$http');
                httpCache.remove(server + '/api/v0/episode/' + $scope.episode.id);
                httpCache.remove(server + '/api/v0/show/' + $scope.show.id);
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
        $scope.show = $scope.episode.show;
        $scope.episode.text = {};
        $scope.episode.show = {id: $scope.episode.show.id}
        $scope.now = new Date().getTime();

        $scope.realTo = dateUtil.toHourMin($scope.episode.plannedTo);
        $scope.realFrom = dateUtil.toHourMin($scope.episode.plannedFrom)
        $scope.save = function () {
            $scope.episode.realFrom = dateUtil.setDate($scope.episode.plannedFrom, $scope.realFrom);
            $scope.episode.realTo = dateUtil.setDate($scope.episode.plannedTo, $scope.realTo);
            $http.post(API_SERVER_ENDPOINT + '/api/v0/episode', $scope.episode).success(function (data) {
                var httpCache = $cacheFactory.get('$http');
                httpCache.remove(server + '/api/v0/show/' + $scope.show.id);
                $location.path('/episode/' + data.data.id);
                $location.path('/show/' + $scope.show.id);
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
        'update': { method: 'PUT'}
    });
}]);
