angular.module("tilosAdmin").directive('ifAdmin', function ($rootScope) {
  return {
    restrict: 'A',
    link: function (scope, element, attributes) {
      if ($rootScope.user && $rootScope.user.role.name == 'admin') {
        element.removeClass('ng-hide');
      }
    }
  };
});


angular.module("tilosAdmin").directive('ifAuthorAdmin', function ($rootScope, $q) {
  return {
    restrict: 'E',
    transclude: true,
    template: '<span ng-transclude ></span>',
    scope: {
      xxx: "=author"
    },
    link: function (scope, element, attributes) {
      if ($rootScope.user && $rootScope.user.role.name == 'admin') {
        return;
      }
      scope.xxx.$promise.then(function (author) {
        if ($rootScope.user.author && $rootScope.user.author.id != author.id) {
          element.addClass('ng-hide');
        } else {

        }
      });


    }
  };
});


angular.module("tilosAdmin").directive('ifShowAdmin', function ($rootScope, $q) {
  return {
    restrict: 'E',
    transclude: true,
    template: '<span ng-transclude ></span>',
    scope: {
      xxx: "=show"
    },
    link: function (scope, element, attributes) {
      if ($rootScope.user && $rootScope.user.role.name == 'admin') {
        return;
      }
      $q.when(scope.xxx).then(function (show) {
        if ($rootScope.user.author && $rootScope.user.author.contributions) {
          var own = false;
          for (var idx in $rootScope.user.author.contributions) {
            var contribution = $rootScope.user.author.contributions[idx];
            if (contribution.show.id == show.id) {
              own = true;
              break;
            }
          }
          if (!own) {
            element.addClass('ng-hide');
          }

        } else {

        }
      });


    }
  };
});