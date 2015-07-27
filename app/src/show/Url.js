'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('url', {
    url: '/url/:id',
    templateUrl: 'show/url.html',
    controller: 'UrlCtrl',
    resolve: {
      url: function ($stateParams, Urls) {
        return Urls.get({show: $stateParams.show, id: $stateParams.id});

      }
    }
  });
  $stateProvider.state('urlEdit', {
    url: '/edit/url/:show/:id',
    templateUrl: 'show/url-form.html',
    controller: 'UrlEditCtrl',
    resolve: {
      data: function ($stateParams, Urls) {
        return Urls.get({id: $stateParams.id});
      }
    }
  });
  $stateProvider.state('urlNew', {
    url: '/new/url/:show',
    templateUrl: 'show/url-form.html',
    controller: 'UrlNewCtrl',
  });
});

angular.module('tilosAdmin')
  .controller('UrlCtrl', ['$scope', 'url', function ($scope, url) {
    $scope.url = url;

  }]);
angular.module('tilosAdmin')
  .controller('UrlEditCtrl', function ($scope, data, resource, dateFilter, $location, $stateParams) {
    $scope.url = data;
    $scope.save = function () {
      resource.update({show: $stateParams.show, id: $scope.url.id}, $scope.url, function (data) {
        $location.path('/show/' + $stateParams.show);
      });
    }
  }
);


angular.module('tilosAdmin').factory('Urls', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v1/url/:id', null, {
    'update': {method: 'PUT'}
  });
}]);

