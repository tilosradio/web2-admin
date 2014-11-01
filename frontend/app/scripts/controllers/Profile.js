'use strict';

angular.module('tilosApp').config(function ($stateProvider) {
    $stateProvider.state('profile', {
            url: '/profile',
            templateUrl: 'partials/profile.html',
            controller: 'ProfileCtrl'
        }
    );


});


angular.module('tilosApp').controller('ProfileCtrl', function () {

});

