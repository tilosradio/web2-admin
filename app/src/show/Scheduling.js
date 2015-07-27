'use strict';

angular.module('tilosAdmin').filter('weekDayName', function () {
  return function (input) {
    return ['Hétfő', "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"][input];
  };
});
angular.module('tilosAdmin').factory('Schedulings', ['API_SERVER_ENDPOINT', '$resource', function (server, $resource) {
  return $resource(server + '/api/v1/scheduling/:id', null, {
    'update': { method: 'PUT'}
  });
}]);

