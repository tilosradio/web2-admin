'use strict';

angular.module('tilosAdmin', [
    'ui.bootstrap',
    'ngCookies',
    'ngResource',
    'ngSanitize',
    'ui.router',
    'configuration',
    'textAngular',
    'LocalStorageModule',
    'ngDialog'
])
    .config(function ($locationProvider, $httpProvider) {
        $httpProvider.interceptors.push(function ($q, localStorageService) {
            return {
                'request': function (config) {
                    var jwt = localStorageService.get("jwt");
                    if (jwt) {
                        config.headers.Authorization = "Bearer " + jwt.access_token;
                    }
                    return config;
                }
            }
        });

        $locationProvider.html5Mode(true);
        //$routeProvider
        //    .when('/', {
        //        templateUrl: 'views/main.html',
        //        controller: 'MainCtrl'
        //    })
        //    .otherwise({
        //        redirectTo: '/'
        //    });
    });


angular.module('tilosAdmin').config(['$provide', function ($provide) {
    // this demonstrates how to register a new tool and add it to the default toolbar
    $provide.decorator('taOptions', ['$delegate', function (taOptions) {
        // $delegate is the taOptions we are decorating
        // here we override the default toolbars and classes specified in taOptions.
        taOptions.toolbar = [
            ['h2', 'h3', 'p'],
            ['bold', 'italics'],
            ['ol', 'ul'],
            ['insertLink', 'insertImage'],
            ['html']
        ];
        return taOptions; // whatever you return will be the taOptions
    }]);

    $provide.decorator('taTools', ['$delegate', function (taTools) {
        taTools.h2.buttontext = 'Cím';
        taTools.h3.buttontext = 'Alcím';
        taTools.p.buttontext = 'Normál';
        return taTools;
    }]);

}]);

angular.module('tilosAdmin').run(function ($rootScope, $state, $location, $http, API_SERVER_ENDPOINT, localStorageService) {

  $rootScope.$on('$stateChangeStart', function (event, toState, toParams) {

    if($rootScope.stateChangeBypass || toState.name === 'login') {
      $rootScope.stateChangeBypass = false;
      return;
    }

    if (!$rootScope.user) {
      event.preventDefault();

      $http.get(API_SERVER_ENDPOINT + "/api/v1/user/me").success(function (data) {
        if (data.role == 'ADMIN' || data.role == 'AUTHOR') {
          $rootScope.user = data;
          $rootScope.stateChangeBypass = true;
          $state.go(toState, toParams);
        }
      });
    } else {
      if ($rootScope.user.role != 'ADMIN' && $rootScope.user.role != 'AUTHOR') {
        $rootScope.user = null;
      }
    }

  });

    //var endsWith = function (str, suffix) {
    //    return str.indexOf(suffix, str.length - suffix.length) !== -1;
    //};

    //var freeAccess = function (url) {
    //    return (/.*password_reset(\?.*)?/g.exec(url) || endsWith(url, '/password_reminder') || endsWith(url, '/login')  || endsWith(url, '/logout'));
    //};

    //$rootScope.access = function(permission) {
    //  var perms = $rootScope.user.permissions;
    //  for (var i = 0; i < perms.length; i++) {
    //    if (permission.search(perms[i]) == 0) {
    //      return true;
    //    }
    //  }
    //  return false;
    //}

    //$rootScope.$on('$locationChangeStart', function (evt, next) {
    //    if (!('user' in $rootScope)) {
    //        if (!freeAccess(next)) {
    //            //evt.preventDefault();
    //            $location.path("/login");
    //
    //        }
    //    }
    //
    //});

    //$rootScope.$on("$routeChangeStart", function (event, next, current) {
    //    if ('user' in $rootScope) {
    //      if ($rootScope.user.role == 'ADMIN' || $rootScope.user.role == 'AUTHOR') {
    //        //hi friends
    //        return;
    //      } else {
    //        localStorageService.remove('jwt');
    //        window.location.href = 'http://tilos.hu';
    //      }
    //    }
    //
    //        // no logged user, we should be going to #login
    //        if (next.templateUrl == "views/login.html" ||
    //          next.templateUrl == "views/reset.html" ||
    //          next.templateUrl == "views/reminder.html" ||
    //          next.templateUrl == "views/logout.html") {
    //            // already going to #login, no redirect needed
    //        } else {
    //            // not going to #login, we should redirect now
    //            $location.path("/login");
    //        }
    //
    //});

    //$rootScope.initialPath = $location.path();
    //$http.get(API_SERVER_ENDPOINT + "/api/v1/user/me").success(function (data) {
    //    if (data && data.username) {
    //        $rootScope.user = data;
    //        if ($rootScope.initialPath) {
    //            var redirectTo = $rootScope.initialPath;
    //            $rootScope.initialPath = null;
    //            $location.path(redirectTo);
    //
    //        }
    //    } else {
    //        if (!freeAccess($location.path())) {
    //            $location.path("/login");
    //        }
    //    }
    //});
});
var server = window.location.protocol + '//' + window.location.hostname;
if (window.location.port && window.location.port !== '9000') {
    server = server + ':' + window.location.port;
}

var tilosHost = window.location.hostname;

angular.module('configuration', []).constant('API_SERVER_ENDPOINT', server);
