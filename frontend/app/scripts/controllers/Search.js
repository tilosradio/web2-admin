'use strict';

angular.module('tilosApp').config(function ($stateProvider) {
    $stateProvider.state('search', {
        url: '/search/:id',
        templateUrl: 'partials/search.html',
        controller: 'SearchCtrl'
    });
});
angular.module('tilosApp').controller('SearchCtrl', function ($scope, $rootScope, $stateParams, API_SERVER_ENDPOINT, $http) {
    $scope.types = {'page': 'Oldal', 'episode': 'Adásnapló', 'author': 'Műsorkészítő', 'show': 'Műsor', 'mix': 'Mix'};
    $http.get(API_SERVER_ENDPOINT + '/api/v1/search/query?q=' + $stateParams.id, {cache: true}).success(function (data) {
        $scope.result = data;
    });
});

angular.module('tilosApp').controller('SearchBox', function ($scope, $location) {
    $scope.search = function () {
        $location.path('search/' + $scope.term);
    };
});



