'use strict';

angular.module('tilosApp').config(function ($stateProvider) {
    $stateProvider.state('login', {
            url: '/login',
            templateUrl: 'partials/login.html',
            controller: 'LoginCtrl'
        }
    );
    $stateProvider.state('password_reminder', {
            url: '/password_reminder',
            templateUrl: 'partials/reminder.html',
            controller: 'PasswordReminderCtrl'
        }
    );

});

angular.module('tilosApp').run(function ($rootScope, localStorageService) {
    $rootScope.logout = function () {
        $rootScope.user = null;
        localStorageService.remove('jwt');
    };
    $rootScope.isLoggedIn = function () {
        return $rootScope.user;
    };
});


angular.module('tilosApp').controller('PasswordReminderCtrl', function ($scope, $http, API_SERVER_ENDPOINT) {
    $scope.reminderdata = {};
    $scope.reminder = function () {
        $http.post(API_SERVER_ENDPOINT + '/api/v0/auth/password_reset', $scope.reminderdata).success(function (data) {
            if (data.success) {
                $scope.message = data.message;
            } else {
                $scope.remindererror = 'Password reset error';
            }
        }).error(function (data) {
            if (data.error) {
                $scope.remindererror = data.error;
            } else {
                $scope.reminderror = 'Unknown error';
            }
        });
    };
});

angular.module('tilosApp')
    .controller('LoginCtrl', function ($rootScope, $scope, $location, API_SERVER_ENDPOINT, $http, localStorageService) {
        $scope.logindata = {};
        $scope.loginerror = '';
        $scope.login = function () {
            $http.post(API_SERVER_ENDPOINT + '/api/v1/auth/login', $scope.logindata).success(function (data) {
                localStorageService.set('jwt', data);

                $http.get(API_SERVER_ENDPOINT + '/api/v1/user/me').success(function (data) {
                    $rootScope.user = data;
                    $location.path('/');
                });

            }).error(function () {
                localStorageService.remove('jwt');
                $scope.loginerror = 'Login error';
            });

        };


    }
);