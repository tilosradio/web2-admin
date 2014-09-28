'use strict';
var tilos = angular.module('tilosApp', ['ngRoute', 'ngSanitize', 'configuration', 'ui.bootstrap', 'textAngular', 'ngResource', 'ui.router']);

tilos.config(function ($locationProvider) {
    $locationProvider.html5Mode(true);
});
tilos.weekStart = function (date) {
    var first = date.getDate() - date.getDay() + 1;
    date.setHours(0);
    date.setSeconds(0);
    date.setMinutes(0);
    return new Date(date.setDate(first));
};


tilos.factory('Meta', function ($rootScope) {
    return {
        setTitle: function (newTitle) {
            $rootScope.pageTitle = newTitle;
        },
        setDescription: function (newDesc) {
            var metaDesc = angular.element(document.querySelector('#desc'));
            metaDesc.attr('content', newDesc);
        }

    };
});

tilos.run(function ($rootScope, Meta) {
    $rootScope.$on('$locationChangeStart', function () {
        Meta.setTitle('');
        Meta.setDescription('');

    });

});

tilos.config(function ($routeProvider, $stateProvider, $urlRouterProvider) {

    $urlRouterProvider.otherwise(function ($injector, $location) {
        var $http = $injector.get('$http');
        var API_SERVER_ENDPOINT = $injector.get('API_SERVER_ENDPOINT');
        var path = $location.path();
        var result = '?';
        $http.get(API_SERVER_ENDPOINT + '/api/v0/text' + path, function () {
            result = '/page' + path;
        });


        var start = new Date().getTime();
        for (var i = 0; i < 1e7; i++) {
            if (result !== '?') {
                return result;
            }
            if ((new Date().getTime() - start) > 1000) {
                return '/404';
            }
        }

    });

//    .when('/news/:id', {
//        templateUrl: 'partials/news.html',
//        controller: 'NewsCtrl'
//    });

});

var server = window.location.protocol + '//' + window.location.hostname;
if (window.location.port && window.location.port !== '9000') {
    server = server + ':' + window.location.port;
}

var tilosHost = window.location.hostname;

angular.module('configuration', []).constant('API_SERVER_ENDPOINT', server);

tilos.factory('validateUrl', function ($sce) {
    return {
        getValidUrl: function (url) {
            return $sce.trustAsResourceUrl(url);
        }
    };
});
