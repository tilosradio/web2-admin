'use strict';


angular.module('tilosApp')
		.controller('NewsCtrl', ['$scope', '$routeParams', 'API_SERVER_ENDPOINT', '$http', 'validateUrl', function ($scope, $routeParams, $server, $http, validateUrl) {
		$http.get($server + '/api/v0/text/' + $routeParams.id, {cache: true}).success(function (data) {
			$scope.news = data;
			$scope.server = $server;
			$scope.likeURL = validateUrl.getValidUrl('http://www.facebook.com/plugins/like.php?href=http%3A%2F%2F' + $server + '%2F%23%2Fnews%2F' + $scope.news.id + '&width&layout=standard&action=like&show_faces=true&share=true');
		});

	}]);
