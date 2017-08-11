var app = angular.module('app', []);

app.controller('ShowSentMessageController', function ($scope, $http) {

    var messageId = angular.element('#messageId').val();

    init();

    function init() {
        getMessage(messageId);
    }

    function getMessage(messageId) {
        $http({
            url: '/messages/getSentMessage',
            method: "GET",
            params: {
                id: messageId
            }
        })
            .then(function (response) {
                $scope.message = response.data;
            })
            .catch(function () {
                console.log('failed');
            });
    }

    $scope.removeMessage = function () {
        $http({
            url: '/messages/removeSentMessage',
            method: "DELETE",
            params: {
                id: messageId
            }
        })
            .then(function () {
                window.location.replace('/messages');
            });
    }
});