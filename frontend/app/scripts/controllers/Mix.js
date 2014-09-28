'use strict';

angular.module('tilosApp').config(function ($stateProvider) {
    $stateProvider.state('mixlist', {
        url: '/mixes/:category',
        templateUrl: 'partials/mixes.html',
        controller: 'MixListCtrl'
    });
    $stateProvider.state('mix', {
        url: '/mix/:id',
        templateUrl: 'partials/mix.html',
        controller: 'MixCtrl'
    });
});

angular.module('tilosApp')
    .controller('MixListCtrl', function ($http, $stateParams, API_SERVER_ENDPOINT, $scope, enumMixType) {
        $scope.tab = $stateParams.category;
        var category = $stateParams.category.toUpperCase();
        $http.get(API_SERVER_ENDPOINT + '/api/v1/mix?category=' + category).success(function (data) {
            $scope.mixes = data;
        });
        $scope.mixType = enumMixType;

    }
);

angular.module('tilosApp')
    .controller('MixCtrl', function ($http, $stateParams, API_SERVER_ENDPOINT, $scope, enumMixType) {
        $scope.tab = $stateParams.category;
        $http.get(API_SERVER_ENDPOINT + '/api/v1/mix/' + $stateParams.id).success(function (data) {
            $scope.mix = data;
        });
        $scope.mixType = enumMixType;

    }
);







