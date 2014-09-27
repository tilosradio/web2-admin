'use strict';

angular.module('tilosApp').config(function ($stateProvider) {
    $stateProvider.state('notfound', {
        url: '/404',
        templateUrl: 'partials/404.html',
        controller: function(){}
    });
});
