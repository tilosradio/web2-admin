'use strict';

angular.module('tilosAdmin').config(function ($routeProvider) {
    $routeProvider.when('/logout', {
        templateUrl: 'views/logout.html',
        controller: function () {
        }
    });
});