'use strict';

angular.module('tilosApp').config(['$routeProvider', function ($routeProvider) {
    $routeProvider.when('/mixes', {
        templateUrl: 'partials/mixes.html',
        controller: 'MixListCtrl'
    });
}]);


angular.module('tilosApp')
    .controller('MixListCtrl', function ($http, $routeParams, API_SERVER_ENDPOINT, $scope, enumMixType) {
        $http.get(API_SERVER_ENDPOINT + '/api/v1/mix').success(function (data) {
            $scope.mixes = data;
        });
        $scope.mixType = enumMixType;

    }
);





