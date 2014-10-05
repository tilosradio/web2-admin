'use strict';

angular.module('tilosAdmin').factory('enumMixType', function () {
    return {"MUSIC": 'Zenés', "SPEECH": 'Beszélgetős'};
});

angular.module('tilosAdmin').factory('enumMixCategory', function () {
    return {"DJ": 'Tilos DJ Mix', "GUESTDJ": 'Guest DJ Mix', 'SHOW': 'Műsor', 'TALE': "Tilos Mese"};
});