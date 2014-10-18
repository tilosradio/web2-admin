'use strict';

angular.module('tilosApp').config(function ($stateProvider) {

    $stateProvider.state('listen', {
        url: '/hallgass',
        templateUrl: 'partials/static/hallgass.html',
    });

    $stateProvider.state('mobil', {
        url: '/mobil',
        templateUrl: 'partials/static/mobil.html',
    });

    $stateProvider.state('android', {
        url: '/android',
        templateUrl: 'partials/static/android.html',
    });

    $stateProvider.state('android-online', {
        url: '/android/online',
        templateUrl: 'partials/static/android-online.html',
    });

    $stateProvider.state('android-archive', {
        url: '/android/archive',
        templateUrl: 'partials/static/android-archive.html',
    });

    $stateProvider.state('android-podcast', {
        url: '/android/podcast',
        templateUrl: 'partials/static/android-podcast.html',
    });


    $stateProvider.state('iphone', {
        url: '/iphone',
        templateUrl: 'partials/static/android.html',
    });


});
