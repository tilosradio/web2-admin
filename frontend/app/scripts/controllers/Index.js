'use strict';

angular.module('tilosApp').controller('IndexCtrl', function ($scope, $routeParams, API_SERVER_ENDPOINT, $http, validateUrl) {
        $scope.Math = window.Math;
        $http.get(API_SERVER_ENDPOINT + '/api/v0/text/news/list', {cache: true}).success(function (data) {
            $scope.news = data;
            for (var i = 0; i < $scope.news.length; i++) {
                $scope.news[i].likeURL = validateUrl.getValidUrl('http://www.facebook.com/plugins/like.php?href=http%3A%2F%2F' + API_SERVER_ENDPOINT + '%2F%23%2Fnews%2F' + $scope.news[i].id + '&width&layout=standard&action=like&show_faces=true&share=true');
            }


        });
    }
);

angular.module('tilosApp').controller('TagCloudCtrl', function (API_SERVER_ENDPOINT, $http, $scope) {
    $http.get(API_SERVER_ENDPOINT + '/api/v0/tag?limit=10', {cache: true}).success(function (data) {
        $scope.tags = data;
    });
});
