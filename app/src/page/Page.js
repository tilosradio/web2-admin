'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('text', {
    url: '/text/:type/:id',
    templateUrl: 'page/page.html',
    controller: 'TextCtrl',
    resolve: {
      data: function ($stateParams, $http, API_SERVER_ENDPOINT) {
        return $http.get(API_SERVER_ENDPOINT + '/api/v1/text/' + $stateParams.type + '/' + $stateParams.id);
      }
    }
  });
  $stateProvider.state('texts', {
    url: '/text/:type',
    templateUrl: 'page/pages.html',
    controller: 'TextListCtrl'
  });


  $stateProvider.state('textEdit', {
    url: '/edit/text/:type/:id',
    templateUrl: 'page/page-form.html',
    controller: 'TextEditCtrl',
    resolve: {
      data: function ($stateParams, $http, API_SERVER_ENDPOINT) {
        return $http.get(API_SERVER_ENDPOINT + '/api/v1/text/' + $stateParams.type + '/' + $stateParams.id);
      }
    }
  });

  $stateProvider.state('textNew', {
    url: '/new/text/:type',
    templateUrl: 'page/page-form.html',
    controller: 'TextNewCtrl'
  });
});


angular.module('tilosAdmin')
  .controller('TextListCtrl', function ($http, $stateParams, API_SERVER_ENDPOINT, $scope) {
    $scope.type = $stateParams.type;
    $http.get(API_SERVER_ENDPOINT + '/api/v1/text/' + $stateParams.type).success(function (data) {
      $scope.pages = data;
    });

    $scope.delete = function (type, alias) {
      $http.delete(`${API_SERVER_ENDPOINT}/api/v1/text/${type}/${alias}`).success(function (data) {
        alert("Text is deleted");
        $http.get(API_SERVER_ENDPOINT + '/api/v1/text/' + $stateParams.type).success(function (data) {
          $scope.pages = data;
        });
      });
    }
  }
);


angular.module('tilosAdmin')
  .controller('TextEditCtrl', function ($http, $stateParams, API_SERVER_ENDPOINT, $location, $scope, Texts, $cacheFactory, data) {
    $scope.text = data.data;
    $scope.type = $stateParams.type;

    if ($scope.text.format == 'legacy' || $scope.text.format == 'default') {
      $scope.text.content = toMarkdown($scope.text.content);
    }

    $scope.save = function () {
      $http.put(API_SERVER_ENDPOINT + '/api/v1/text/' + $stateParams.type + '/' + $scope.text.id, $scope.text).success(function (data) {
        var httpCache = $cacheFactory.get('$http');
        httpCache.remove(API_SERVER_ENDPOINT + '/api/v1/text/' + $stateParams.type + '/' + $scope.text.id);
        $location.path('/text/' + $stateParams.type + '/' + $scope.text.id);
      });
    }
  }
);

angular.module('tilosAdmin')
  .controller('TextNewCtrl', function ($http, $stateParams, API_SERVER_ENDPOINT, $location, $scope) {
    $scope.text = {};
    $scope.save = function () {
      $http.post(`${API_SERVER_ENDPOINT}/api/v1/text/${$stateParams.type}/${$stateParams.alias}`, $scope.text).success(function (data) {
        $location.path('/text/' + $stateParams.type + '/' + data.id);
      });
    }
  }
);
;

angular.module('tilosAdmin').controller('TextCtrl', function ($scope, data, $stateParams) {
  $scope.type = $stateParams.type;
  $scope.page = data.data;
});


angular.module('tilosAdmin').factory('Texts', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v1/text/page/:id', null, {
    'update': {method: 'PUT'}
  });
}]);
