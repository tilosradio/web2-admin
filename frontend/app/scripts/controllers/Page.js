'use strict';


angular.module('tilosApp').config(function ($stateProvider) {
    $stateProvider.state('page', {
        url: '/page/:id',
        templateUrl: 'partials/page.html',
        controller: 'PageCtrl'
    });
});

angular.module('tilosApp').controller('PageCtrl', function ($scope, $stateParams, tilosData) {
    tilosData.getText($stateParams.id, function (data) {
        $scope.page = data;
    });
});