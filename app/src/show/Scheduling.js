'use strict';

angular.module('tilosAdmin').filter('weekDayName', function () {
  return function (input) {
    return ['Hétfő', "Kedd", "Szerda", "Csütörtök", "Péntek", "Szombat", "Vasárnap"][input];
  };
});
