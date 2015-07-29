'use strict';

angular.module('tilosAdmin').config(function ($stateProvider) {
  $stateProvider.state('login', {
      url: '/login',
      templateUrl: 'auth/login.html',
      controller: 'LoginCtrl'
    }
  );
  $stateProvider.state('passwordReminder', {
      url: '/password_reminder',
      templateUrl: 'auth/reminder.html',
      controller: 'PasswordReminderCtrl'
    }
  );

});

angular.module("tilosAdmin").controller("PasswordReminderCtrl", function ($scope, $http) {
  $scope.reminderdata = {};

  $scope.reminder = function () {
    $http.post(server + '/api/v1/auth/password_reset', $scope.reminderdata).success(function (data) {
      $scope.message = data.message;
      $scope.remindererror = "";
    }).error(function (data) {
      if (data.message) {
        $scope.remindererror = data.message;
      } else {
        $scope.remindererror = "Unknown error"
      }
    });
  };
});
angular.module('tilosAdmin')
  .controller('LoginCtrl', function ($rootScope, $scope, $state, $location, API_SERVER_ENDPOINT, $http, localStorageService) {
    $scope.logindata = {};
    $scope.login = function () {
      var data = {};
      $http.post(server + '/api/v1/auth/login', $scope.logindata).success(function (data) {
        localStorageService.set("jwt", data);
        $scope.loginerror = "";
        $http.get(server + '/api/v1/user/me').success(function (data) {
          $rootScope.user = data;
          $state.go('main');
        });

      }).error(function (data) {
        $scope.loginerror = "Login error";
        localStorageService.remove('jwt');
      });

    }


  }
);
