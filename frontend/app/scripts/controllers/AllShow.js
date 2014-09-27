'use strict';

angular.module('tilosApp').config(function ($stateProvider) {
    $stateProvider.state('shows', {
        url: '/shows',
        templateUrl: 'partials/shows.html',
        controller: 'AllshowCtrl'
    });
});

angular.module('tilosApp').controller('AllshowCtrl', function ($scope, $routeParams, API_SERVER_ENDPOINT, $http) {
    $http.get(API_SERVER_ENDPOINT + '/api/v0/show', {cache: true}).success(function (data) {
        var res = {
            talk: [],
            sound: []
        };
        for (var i = 0; i < data.length; i++) {
            var show = data[i];
            if (show.type) {
                res.talk.push(data[i]);
            } else {
                res.sound.push(data[i]);
            }
        }
        $scope.shows = res;
    });
});
