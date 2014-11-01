'use strict';

angular.module('tilosAdmin').config(['$routeProvider', function ($routeProvider) {
  $routeProvider.when('/password_reset', {
    templateUrl: 'views/reset.html',
    controller: 'ChangePasswordCtrl' });
}]);
angular.module('tilosAdmin')
    .controller('ChangePasswordCtrl', ['$rootScope', '$scope', '$location', 'API_SERVER_ENDPOINT', '$http', function ($rootScope, $scope, $location, server, $http) {
      $scope.newpassword = {};
      $scope.newpassword.token = ($location.search()).token;
      $scope.newpassword.email = ($location.search()).email;
      if (!$scope.newpassword.token || !$scope.newpassword.email) {
        $scope.error = "Az email vagy a token paraméter hiányzik";
      }
      $scope.reset = function () {
        $http.post(server + '/api/v0/auth/password_reset', $scope.newpassword).success(function (data) {
          if (data.success) {
            $scope.error = "";
            $scope.message = data.message;
          } else {
            $scope.error = "Password reset error";
          }
        }).error(function (data) {
              $scope.error = data.error;
            });
      };
    }]);
