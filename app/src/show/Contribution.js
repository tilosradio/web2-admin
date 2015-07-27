'use strict';

angular.module('tilosAdmin').factory('Contributions', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v0/contribution/:id', null, {
    'update': {method: 'PUT'}
  });
}]);

