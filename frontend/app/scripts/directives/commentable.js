'use strict';

angular.module('tilosApp').directive('commentable', [function () {

    return {

        templateUrl: '/partials/commentable.html', //HTML template.. see below

        scope: {  //isolate the scope
            commentable: '@',
            commentableType: '@',
            src: '=',
            loggedIn: '&'
        },

        controller: function ($scope, $http, API_SERVER_ENDPOINT) {

            var restEndpoint = API_SERVER_ENDPOINT + '/api/v1/comment/' + $scope.commentableType + '/' + $scope.commentable;
            $scope.comments = [];
            $scope.formComment = {};

            $http.get(restEndpoint).success(function (res) {
                $scope.comments = res;
            });

            var commentable = {};
//            $scope.commentEdit = function(comment) {
//                comment.interact = comment.body;
//                comment.editing = true;
//                comment.replying = false;
//            };

            $scope.commentReply = function (comment) {
                comment.interact = '';
                comment.replying = true;
                comment.editing = false;
            };

            $scope.cancelComment = function (comment) {
                _commentResetState(comment);
            };

            $scope.createComment = function () {
                if ($scope.formComment.comment) {
                    return $http.post(restEndpoint, $scope.formComment).success(function () {
                        $http.get(restEndpoint).success(function (res) {
                            $scope.comments = res;
                        });
                    });
                }
            };

//            $scope.updateComment = function(comment) {
//                commentable.updateComment(comment.id, comment.interact)
//                    .success(function(response) {
//                        _commentResetState(comment);
//                        Object.merge(comment, response.comment);
//                    });
//            };

            $scope.replyComment = function (comment) {

                if (comment.interact) {
                    var newComment = {};
                    newComment.comment = comment.interact;
                    newComment.parentId = comment.id;

                    return $http.post(restEndpoint, newComment).success(function () {
                        $http.get(restEndpoint).success(function (res) {
                            $scope.comments = res;
                        });
                    });
                }

            };

            /// PRIVATE

            //helpers
            function _resetFormComment() {
                $scope.formComment = null;
                $scope.formComment = {};
            }

            function _commentResetState(comment) {
                comment.replying = false;
                comment.editing = false;
            }

        }
    };
}]);