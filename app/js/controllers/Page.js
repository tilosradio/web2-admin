'use strict';

angular.module('tilosAdmin').config(['$routeProvider', function ($routeProvider) {
  $routeProvider.when('/text/:type/:id', {
    templateUrl: 'views/page.html',
    controller: 'TextCtrl',
    resolve: {
      data: function ($route, $http, API_SERVER_ENDPOINT) {
        return $http.get(API_SERVER_ENDPOINT + '/api/v1/text/' + $route.current.params.type + '/' + $route.current.params.id);
      }
    }
  });
  $routeProvider.when('/text/:type', {
    templateUrl: 'views/pages.html',
    controller: 'TextListCtrl'
  });


  $routeProvider.when('/edit/text/:type/:id', {
    templateUrl: 'views/page-form.html',
    controller: 'TextEditCtrl',
    resolve: {
      data: function ($route, $http, API_SERVER_ENDPOINT) {
        return $http.get(API_SERVER_ENDPOINT + '/api/v1/text/' + $route.current.params.type + '/' + $route.current.params.id);
      }
    }
  });

  $routeProvider.when('/new/text/:type', {
    templateUrl: 'views/page-form.html',
    controller: 'TextNewCtrl'
  });
}]);


angular.module('tilosAdmin')
  .controller('TextListCtrl', function ($http, $routeParams, API_SERVER_ENDPOINT, $scope) {
    $scope.type = $routeParams.type;
    $http.get(API_SERVER_ENDPOINT + '/api/v1/text/' + $routeParams.type).success(function (data) {
      $scope.pages = data;
    });

  }
);


angular.module('tilosAdmin')
  .controller('TextEditCtrl', function ($http, $routeParams, API_SERVER_ENDPOINT, $location, $scope, Texts, $cacheFactory, data) {
    $scope.text = data.data;
    $scope.type = $routeParams.type;

    if ($scope.text.format == 'legacy' || $scope.text.format == 'default') {
      $scope.text.content = toMarkdown($scope.text.content);
    }

    $scope.save = function () {
      $http.put(API_SERVER_ENDPOINT + '/api/v1/text/' + $routeParams.type + '/' + $scope.text.id, $scope.text).success(function (data) {
        var httpCache = $cacheFactory.get('$http');
        httpCache.remove(API_SERVER_ENDPOINT + '/api/v1/text/' + $routeParams.type + '/' + $scope.text.id);
        $location.path('/text/' + $routeParams.type + '/' + $scope.text.id);
      });
    }
  }
);

angular.module('tilosAdmin')
  .controller('TextNewCtrl', function ($http, $routeParams, API_SERVER_ENDPOINT, $location, $scope, Texts) {
    $scope.text = {};
    $scope.save = function () {
      $http.post(API_SERVER_ENDPOINT + '/api/v1/text/' + $routeParams.type, $scope.text).success(function (data) {
        $location.path('/text/' + $routeParams.type + '/' + data.id);
      });
    }
  }
);
;

angular.module('tilosAdmin').controller('TextCtrl', function ($scope, data, $routeParams) {
  $scope.type = $routeParams.type;
  $scope.page = data.data;
});


angular.module('tilosAdmin').factory('Texts', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v1/text/page/:id', null, {
    'update': {method: 'PUT'}
  });
}]);


