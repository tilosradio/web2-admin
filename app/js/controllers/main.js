'use strict';

angular.module('tilosAdmin')
    .controller('MainCtrl', function ($scope, $http, API_SERVER_ENDPOINT, $rootScope, $location) {
        $http.get(API_SERVER_ENDPOINT + '/api/v1/stat/summary', {cache: true}).success(function (data) {
            $scope.stat = data;
        });
        var now = new Date();
        var result = [];
        var promise = null;
        if ($scope.user.author && $scope.user.author.contributions) {
            for (var i = 0; i < $scope.user.author.contributions.length; i++) {
                var showId = $scope.user.author.contributions[i].show.id
                var now = new Date();
                var past = new Date();
                past.setTime(now.getTime() - 60 * 60 * 24 * 1000 * 60)
                var url = API_SERVER_ENDPOINT + '/api/v1/show/' + showId + '/episodes?start=' + past.getTime() + '&end=' + now.getTime();
                if (promise == null) {
                    promise = $http.get(url, {cache: true});
                } else {
                    promise = promise.then(function (data) {
                        result = result.concat(data.data);
                        return $http.get(url, {cache: true});
                    });
                }
            }
            promise.then(function (data) {
                result = result.concat(data.data);
                result.sort(function (a, b) {
                    return b.plannedFrom - a.plannedFrom;
                });
                $scope.episodes = result;
            });
        }
        $scope.episodes = [];
        $scope.yesterday = new Date();
        $scope.yesterday.setDate(new Date().getDate() - 1);

        $scope.newEpisode = function (episode) {
            $rootScope.newEpisode = episode;
            $rootScope.newEpisode.show = episode.show;
            $location.path('/new/episode');
        };

    });
