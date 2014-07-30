/*global angular*/
/*jshint indent: 2, undef: true, unused: true, strict: true, trailing: true, camelcase: true, eqeqeq: true, immed: true, white: true, quotmark: single, curly: true */

'use strict';

angular.module('tilosApp')
  .factory('tilosData', ['$rootScope', '$http', 'API_SERVER_ENDPOINT', function ($root, $http, $server) {


    return {
      name: 'anonymous',
      getFacebookData: function (callback) {
        if ($root.facebook) {
          callback($root.facebook);
        } else {
          //Get Facebook follower count
          $http.get('https://graph.facebook.com/tilosradio').success(function (data) {
            $root.facebook = data;
            callback(data);
          });
        }
      },

      getText: function (id, callback) {
        if ($root.text && $root.text[id]) {
          callback($root.text[id]);
        } else {
          if (!$root.text) {
            $root.text = {};
          }
          $http.get($server + '/api/v0/text/' + id).success(function (data) {
            $root.text[id] = data;
            callback(data);
          });
        }
      }
    };
  }]);