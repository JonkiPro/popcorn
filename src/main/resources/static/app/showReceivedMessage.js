var app = angular.module('app', []);

app.controller('ShowReceivedMessageController', function ($scope, $http) {

    var messageId = angular.element('#messageId').val();

    init();

    function init() {
        getMessage(messageId);
    }

    function getMessage(messageId) {
        $http({
            url: '/messages/getReceivedMessage',
            method: "GET",
            params: {
                id: messageId
            }
        })
            .then(function (response) {
                $scope.message = response.data;

                if(response.data.date_of_read === null) {
                    setDateOfRead();
                }
            })
            .catch(function () {
                console.log('failed');
            });
    }

    $scope.removeMessage = function () {
        $http({
            url: '/messages/removeReceivedMessage',
            method: "DELETE",
            params: {
                id: messageId
            }
        })
            .then(function () {
                window.location.replace('/messages');
            });
    };

    function setDateOfRead() {
        $http({
            url: '/messages/setDateOfRead',
            method: "PUT",
            params: {
                id: messageId
            }
        })
            .then(function (response) {
                $scope.message.date_of_read = response.data;
            });
    }
});