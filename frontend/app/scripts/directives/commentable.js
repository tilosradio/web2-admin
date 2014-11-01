'use strict';

angular.module('tilosApp').directive('commentable', [function () {

    return {

        templateUrl: '/partials/commentable.html', //HTML template.. see below

        scope: {  //isolate the scope
            commentable: '=',
            commentableType: '@',
            src: '=',
            loggedIn: '&'
        },

        controller: function ($scope, $http, API_SERVER_ENDPOINT) {
            var commentable = {};

            $scope.comments = [];
            $scope.formComment = {};

            $http.get(API_SERVER_ENDPOINT + "/api/v1/comment/EPISODE/47071").success(function (res) {
                $scope.comments = res;
            });

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
                    return $http.post(API_SERVER_ENDPOINT + '/api/v1/comment/EPISODE/47071', $scope.formComment).success(function () {
                        $http.get(API_SERVER_ENDPOINT + "/api/v1/comment/EPISODE/47071").success(function (res) {
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

                    return $http.post(API_SERVER_ENDPOINT + '/api/v1/comment/EPISODE/47071', newComment).success(function () {
                        $http.get(API_SERVER_ENDPOINT + '/api/v1/comment/EPISODE/47071').success(function (res) {
                            $scope.comments = res;
                        });
                    });
                }

            };

            $scope.deleteComment = function (comment) {
                commentable.deleteComment(comment.id)
                    .success(function () {
                        _commentResetState(comment);
                        $scope.comments.remove(comment);
                    });
            };

            //inline Commentable service

            commentable.getComments = function (path) {
                return $http.get(path);
            };

            commentable.createComment = function (commentableType, commentableObj, body) {

                var data = {
                    comment: {
                        commentable_type: commentableType,
                        commentable_id: commentableObj.id,
                        body: body
                    }
                };

                return $http.post(Routes.comments_path(), data);
            };

            commentable.updateComment = function (commentId, commentBody) {
                var data = {
                    comment: {
                        body: commentBody
                    }
                };

                return $http.put(Routes.comment_path(commentId), data);
            };

            commentable.replyComment = function (parentCommentId, commentBody) {
                var data = {
                    comment: {
                        body: commentBody
                    }
                };

                return $http.post(Routes.reply_comment_path(parentCommentId), data);
            };

            commentable.deleteComment = function (commentId) {
                return $http.delete(Routes.comment_path(commentId, {method: 'delete'}));
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