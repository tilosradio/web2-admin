'use strict';

angular.module('tilosAdmin').config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/mix/:id', {
        templateUrl: 'views/mix.html',
        controller: 'MixesCtrl',
        resolve: {
            data: function ($route, Mixes) {
                return Mixes.get({id: $route.current.params.id});
            },
        }});
    $routeProvider.when('/mixes', {
        templateUrl: 'views/mixes.html',
        controller: 'MixListCtrl'
    });

    $routeProvider.when('/edit/mix/:id', {
        templateUrl: 'views/mix-form.html',
        controller: 'MixEditCtrl',
        resolve: {
            data: function ($route, Mixes) {
                return Mixes.get({id: $route.current.params.id});
            },
        }});

    $routeProvider.when('/new/mix', {
        templateUrl: 'views/mix-form.html',
        controller: 'MixNewCtrl'
    });
}]);


angular.module('tilosAdmin')
    .controller('MixListCtrl', function ($http, $routeParams, API_SERVER_ENDPOINT, $scope) {
        $http.get(API_SERVER_ENDPOINT + '/api/v1/mix').success(function (data) {
            $scope.mixes = data;
        });

    }
);


angular.module('tilosAdmin')
    .controller('MixEditCtrl', function ($http, $routeParams, API_SERVER_ENDPOINT, $location, $scope, Mixes, $cacheFactory, data, enumMixType, enumMixCategory) {
        $scope.mix = data;
        $scope.mixType = enumMixType;
        $scope.mixCategory = enumMixCategory;
        //if (!$scope.mix.show.length == 0) {
//        $scope.mix.$promise.then(function (a) {
//            if (a.show.length == 0) {
//                $scope.mix.show = {};
//            }
//        });
//        //}
//        $http.get(API_SERVER_ENDPOINT + '/api/v0/show', {'cache': true}).success(function (data) {
//            $scope.shows = data;
//        });
        $scope.save = function () {
            $http.put(API_SERVER_ENDPOINT + '/api/v1/mix/' + $scope.mix.id, $scope.mix).success(function (data) {
                var httpCache = $cacheFactory.get('$http');
                httpCache.remove(API_SERVER_ENDPOINT + '/api/v1/mix/' + $scope.mix.id);
                httpCache.remove(API_SERVER_ENDPOINT + '/api/v1/mixes');
                $location.path('/mix/' + $scope.mix.id);
            });
        }
    }
);


angular.module('tilosAdmin')
    .controller('MixNewCtrl', function ($http, $routeParams, API_SERVER_ENDPOINT, $location, $scope, Mixes, enumMixType, enumMixCategory) {
        $scope.mixType = enumMixType;
        $scope.mixCategory = enumMixCategory;
        $scope.mix = {};
        $http.get(API_SERVER_ENDPOINT + '/api/v0/show', {'cache': true}).success(function (data) {
            $scope.shows = data;
        });
        $scope.types = [
            {id: 1, 'name': "Beszélgetős"},
            {id: 0, 'name': "Zenés"}
        ]
        $scope.save = function () {
            $http.post(API_SERVER_ENDPOINT + '/api/v1/mix', $scope.mix).success(function (data) {
                $location.path('/mix/' + data.id);
            });
        }
    }
);


angular.module('tilosAdmin')
    .controller('MixesCtrl', function ($scope, data, $http, API_SERVER_ENDPOINT, $location) {
        $scope.mix = data;
        $scope.delete = function() {
            var id = $scope.mix.id;
            $http.delete(API_SERVER_ENDPOINT + '/api/v1/mix/' + id).success(function (data) {
                $location.path('/mixes');
            });
        }

    });


angular.module('tilosAdmin').factory('Mixes', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
    return $resource(server + '/api/v1/mix/:id', null, {
        'update': { method: 'PUT'}
    });
}]);


