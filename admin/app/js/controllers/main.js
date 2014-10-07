'use strict';

angular.module('tilosAdmin')
    .controller('MainCtrl', function ($scope, $http, API_SERVER_ENDPOINT) {
        $http.get(API_SERVER_ENDPOINT + '/api/v1/stat/summary', {cache: true}).success(function (data) {
            $scope.stat = data;
        });
        var now = new Date();
        var result = [];
        var promise = null;
        for (var i = 0; i < $scope.user.author.contributions.length; i++) {
            var showId = $scope.user.author.contributions[i].show.id
            var url = API_SERVER_ENDPOINT + '/api/v0/show/' + showId + '/episodes?start=1412532320&end=1412561120';
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

    });
