'use strict';
angular.module('tilosApp')
    .controller('SocialCtrl', function ($scope) {
        $scope.Math = window.Math;
        var server = 'http://tilos.hu';
        //Share a show or article on Facebook, we need the name and definition.
        $scope.share = function (name, definition, alias) {
            window.open(
                    'http://www.facebook.com/sharer.php?s=100&p[title]=' + encodeURIComponent(name) + '&p[summary]=' + encodeURIComponent(definition) + '&p[url]=' + encodeURIComponent(server + '/#/show/' + alias) + '',
                '',
                'width=500, height=300'
            );
        };

    }
);
