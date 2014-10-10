/**
 * Master Controller
 */
angular.module('tilosAdmin').controller('DashboardCtrl', function ($scope, $cookieStore, $http, API_SERVER_ENDPOINT, $rootScope, $location) {
    /**
     * Sidebar Toggle & Cookie Control
     *
     */
    var mobileView = 992;

    $scope.getWidth = function () {
        return window.innerWidth;
    };

    $scope.$watch($scope.getWidth, function (newValue, oldValue) {
        if (newValue >= mobileView) {
            if (angular.isDefined($cookieStore.get('toggle'))) {
                if ($cookieStore.get('toggle') == false) {
                    $scope.toggle = false;
                }
                else {
                    $scope.toggle = true;
                }
            }
            else {
                $scope.toggle = true;
            }
        }
        else {
            $scope.toggle = false;
        }

    });

    $scope.toggleSidebar = function () {
        $scope.toggle = !$scope.toggle;

        $cookieStore.put('toggle', $scope.toggle);
    };

    window.onresize = function () {
        $scope.$apply();
    };

    $scope.logout = function () {
        $http.get(API_SERVER_ENDPOINT + '/api/v0/auth/sign_out').success(function (data) {
            delete $rootScope.user;
            $location.path('/login');
        });
    };

});
