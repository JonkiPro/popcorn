var app = angular.module('app', []);

app.controller('ShowReceivedMessageController', function ($scope, $http) {

    var messageId = angular.element('#messageId').val();

    init();

    function init() {
        getMessage(messageId);
    }

    function getMessage(messageId) {
        $http({
            url: '/api/v1.0/messages/received/' + messageId,
            method: "GET"
        })
            .then(function (response) {
                $scope.message = response.data;
            })
            .catch(function () {
                window.location.replace('/messages');
            });
    }

    $scope.removeMessage = function () {
        $http({
            url: '/api/v1.0/messages/received/' + messageId,
            method: "DELETE"
        })
            .then(function () {
                window.location.replace('/messages');
            });
    };
});