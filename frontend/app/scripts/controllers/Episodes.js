'use strict';

angular.module('tilosApp').controller('EpisodesCtrl', function ($scope, $stateParams, API_SERVER_ENDPOINT, $http) {
        $scope.now = new Date();

        var nowDate = new Date();
        var start = Math.round((nowDate / 1000 - 60 * 60 * 3) / 10) * 10;
        var now = nowDate.getTime() / 1000;
        $http.get(API_SERVER_ENDPOINT + '/api/v0/episode?start=' + start + '&end=' + (start + 8 * 60 * 60), {cache: true}).success(function (data) {
            for (var i = 0; i < data.length; i++) {
                if (data[i].plannedFrom <= now && data[i].plannedTo > now) {
                    $scope.current = data[i];
                }
            }
            $http.get(API_SERVER_ENDPOINT + '/api/v0/show/' + $scope.current.show.id, {cache: true}).success(function (sd) {
                $scope.current.show = sd;
                $scope.episodes = data;
            });
        });

    }
);
