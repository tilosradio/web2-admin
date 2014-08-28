'use strict';

angular.module('tilosApp')
    .controller('404Ctrl', function ($http, API_SERVER_ENDPOINT, $location) {
        var name = $location.path();
        if (name[0] === '/') {
            name = name.substr(1);
        }
        $http.get(API_SERVER_ENDPOINT + '/api/v0/text/' + name).success(function (data) {
            $location.path('/page/' + name);
        }).error(function (data) {
            $http.get(API_SERVER_ENDPOINT + '/api/v0/show/' + name).success(function (data) {
                $location.path('/show/' + name);
            }).error(function (data) {
                $http.get(API_SERVER_ENDPOINT + '/api/v0/author/' + name).success(function (data) {
                    $location.path('/author/' + name);
                });
            });
        });
    });
