var app = angular.module('app', []);

app.controller('ShowSentMessageController', function ($scope, $http) {

    var messageId = angular.element('#messageId').val();

    init();

    function init() {
        getMessage(messageId);
    }

    function getMessage(messageId) {
        $http({
            url: '/api/v1.0/messages/sent/' + messageId,
            method: "GET"
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
            url: '/api/v1.0/messages/sent/' + messageId,
            method: "DELETE"
        })
            .then(function () {
                window.location.replace('/messages');
            });
    }
});