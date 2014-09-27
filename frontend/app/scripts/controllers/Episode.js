'use strict';


angular.module('tilosApp').config(function($stateProvider)
{
    $stateProvider.state('episode-id', {
        url: '/episode/:id',
        templateUrl: 'partials/episode.html',
        controller: 'EpisodeCtrl',
        resolve: {
            data: function ($route, $http, API_SERVER_ENDPOINT) {
                return $http.get(API_SERVER_ENDPOINT + '/api/v1/episode/' + $route.current.params.id, {cache: true});
            },
            show: function ($route, $http, API_SERVER_ENDPOINT) {
                return $http.get(API_SERVER_ENDPOINT + '/api/v1/show/' + $route.current.params.show);
            }
        }
    }).state('episode-date', {
        url: '/episode/:show/:year/:month/:day',
        templateUrl: 'partials/episode.html',
        controller: 'EpisodeCtrl',
        resolve: {
            data: function ($stateParams, $http, API_SERVER_ENDPOINT) {
                return $http.get(API_SERVER_ENDPOINT + '/api/v1/episode/' + $stateParams.show + '/' + $stateParams.year + '/' + $stateParams.month + '/' + $stateParams.day);
            },
            show: function ($stateParams, $http, API_SERVER_ENDPOINT) {
                return $http.get(API_SERVER_ENDPOINT + '/api/v1/show/' + $stateParams.show);
            }
        }
    });
});


/*global angular*/
angular.module('tilosApp').controller('EpisodeCtrl', function ($scope, data, show, $sce, Meta) {
        $scope.episode = data.data;
        if (data.data.text && data.data.text.formatted) {
            $scope.episode.text.formatted = $sce.trustAsHtml(data.data.text.formatted);
        }
        $scope.currentShow = show.data;
        var start = new Date();
        start.setTime($scope.episode.plannedFrom * 1000);
        var dateStr = start.format('yyyy.mm.dd');
        if ($scope.episode.text && $scope.episode.text.title) {
            Meta.setDescription(dateStr + ' - ' + $scope.episode.text.title);
        } else {
            Meta.setDescription(dateStr + ' - ' + $scope.currentShow.name + ' adás');
        }
        Meta.setTitle($scope.currentShow.name + ' adásnapló');
    }
);
