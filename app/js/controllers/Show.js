'use strict';

angular.module('tilosAdmin').config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/show/:id', {
        templateUrl: 'views/show.html',
        controller: 'ShowCtrl',
        resolve: {
            data: function ($route, Shows, API_SERVER_ENDPOINT, $http) {
                return $http.get(API_SERVER_ENDPOINT + "/api/v1/show/" + $route.current.params.id);
            },
            schedulingList: function ($route, Schedulings, API_SERVER_ENDPOINT, $http) {
                return $http.get(API_SERVER_ENDPOINT + "/api/v0/show/" + $route.current.params.id + "/schedulings");
            },
        }});
    $routeProvider.when('/shows', {
        templateUrl: 'views/shows.html',
        controller: 'ShowListCtrl',
        resolve: {
            list: function ($route, API_SERVER_ENDPOINT, $http) {
              return $http.get(API_SERVER_ENDPOINT + '/api/v1/show?status=all');
            }
        }});
    $routeProvider.when('/edit/show/:id', {
        templateUrl: 'views/show-form.html',
        controller: 'ShowEditCtrl',
        resolve: {
            data: function ($route, Shows) {
                return Shows.get({id: $route.current.params.id});
            }
        }});
    $routeProvider.when('/new/show', {
        templateUrl: 'views/show-form.html',
        controller: 'ShowNewCtrl',
    });
}]);
angular.module('tilosAdmin')
    .controller('ShowCtrl', function ($scope, data, API_SERVER_ENDPOINT, $http, $rootScope, $location, schedulingList, Schedulings, Contributions, Shows, Urls) {
        data = data.data;
        $scope.show = data;

        $scope.server = API_SERVER_ENDPOINT;
        $scope.schedulings = schedulingList.data;
        $scope.now = new Date().getTime();

        $scope.currentShowPage = 0;
        $scope.deleteScheduling = function (id) {
            Schedulings.remove({'id': id});
            $http.get(API_SERVER_ENDPOINT + "/api/v1/show/" + $scope.show.id + "/schedulings").success(function (data) {
                $scope.schedulings = data;
            });
        }
        $scope.deleteUrl = function (id) {
            Urls.remove({'id': id, 'showId': $scope.show.id});
            $scope.show = Shows.get({id: $scope.show.id});
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

        $scope.deleteContribution = function (id) {
            Contributions.remove({id: id}, function () {
                $scope.show = Shows.get({id: $scope.show.id});

            });
        };

    });

angular.module('tilosAdmin')
    .controller('ShowListCtrl', function ($scope, list) {
        $scope.shows = list.data;

    });

'use strict';

angular.module('tilosAdmin')
    .controller('ShowNewCtrl', function ($location, $scope, $routeParams, $http, $cacheFactory, Shows, API_SERVER_ENDPOINT) {
        $scope.types = [
            {id: 1, 'name': "Beszélgetős"},
            {id: 0, 'name': "Zenés"}
        ]
        $scope.statuses = [
            {id: 0, 'name': "Tervezett"},
            {id: 1, 'name': "Aktív"},
            {id: 2, 'name': "Archív"},
            {id: 3, 'name': "Legenda"}
        ]
        $scope.show = {};
        $scope.save = function () {

            Shows.save($scope.show, function (data) {
                var id = data.data.id;
                $location.path('/show/' + id);
            });

        };
    });

angular.module('tilosAdmin')
    .controller('ShowEditCtrl', ['$location', '$scope', '$routeParams', 'API_SERVER_ENDPOINT', '$http', '$cacheFactory', 'data',
        function ($location, $scope, $routeParams, server, $http, $cacheFactory, data) {
            $scope.types = [
                {id: 1, 'name': "Beszélgetős"},
                {id: 0, 'name': "Zenés"}
            ]
            $scope.statuses = [
                {id: 0, 'name': "Tervezett"},
                {id: 1, 'name': "Aktív"},
                {id: 2, 'name': "Archív"},
                {id: 3, 'name': "Legenda"}
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
        'update': { method: 'PUT'}
    });
}]);
