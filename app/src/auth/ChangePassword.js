'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('passwordReset', {
    url: '/password_reset',
    templateUrl: 'auth/reset.html',
    controller: 'ChangePasswordCtrl'
  });
});

angular.module('tilosAdmin')
  .controller('ChangePasswordCtrl', function ($rootScope, $scope, $location, API_SERVER_ENDPOINT, $http) {
    $scope.newpassword = {};
    $scope.newpassword.token = ($location.search()).token;
    $scope.newpassword.email = ($location.search()).email;
    if (!$scope.newpassword.token || !$scope.newpassword.email) {
      $scope.error = "Az email vagy a token paraméter hiányzik";
    }
    $scope.reset = function () {
      $http.post(server + '/api/v1/auth/password_reset', $scope.newpassword).success(function (data) {
        $scope.error = "";
        $scope.message = data.message;

      }).error(function (data) {
        if (data.message) {
          $scope.error = data.message;
        } else {
          $scope.error = "Unknown error"
        }
      });
    };
  });
