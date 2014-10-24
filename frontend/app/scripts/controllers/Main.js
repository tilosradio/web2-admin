'use strict';

angular.module('tilosApp').config(function ($stateProvider) {
    $stateProvider.state('index', {
        url: '/',
        templateUrl: 'partials/index.html',
        controller: 'MainCtrl'
    });
    $stateProvider.state('index-i', {
        url: '/index',
        templateUrl: 'partials/index.html',
        controller: 'MainCtrl'
    });

});

angular.module('tilosApp').controller('MainCtrl', function ($scope, FeedService, $http, API_SERVER_ENDPOINT, $sce, $timeout) {
    FeedService.parseFeed('http://hirek.tilos.hu/?feed=rss2').then(function (res) {
        $scope.feeds = res.data.responseData.feed.entries;
    });
    $http.get(API_SERVER_ENDPOINT + '/api/v0/episode/next').success(function (data) {
        $scope.next = data;
    });
    $http.get(API_SERVER_ENDPOINT + '/api/v0/episode/last').success(function (data) {
        $scope.last = data;
    });
    $http.get(API_SERVER_ENDPOINT + '/api/v0/text/lead').success(function (data) {
        $scope.lead = data;
    });

    var myTimeout;
    $scope.counter = '';
    var deadline = new Date(2014, 6, 23, 11, 59, 40).getTime();
    $scope.onTimeout = function () {
        var diff = new Date().getTime() - deadline;
        var days = Math.floor(diff / (60 * 60 * 24 * 1000));
        diff = diff - days * 60 * 60 * 24 * 1000;
        var hours = Math.floor(diff / (60 * 60 * 1000));
        diff = diff - hours * 60 * 60 * 1000;
        var minutes = Math.floor(diff / (60 * 1000));
        diff = diff - minutes * 60 * 1000;
        var seconds = Math.floor(diff / 1000);
        myTimeout = $timeout($scope.onTimeout, 1000);
        $scope.counter = '' + days + ' nap, ' + hours + ' óra, ' + minutes + ' perc és ' + seconds + ' másodperc';
    };

    $scope.onTimeout();

});