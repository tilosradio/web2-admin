'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('logout', {
    url: '/logout',
    templateUrl: 'auth/logout.html',
    controller: function () {
    }
  });
});
