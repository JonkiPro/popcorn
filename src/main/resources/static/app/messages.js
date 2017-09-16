var app = angular.module('app', []);

app.controller('MessagesController', function ($scope, $http) {

    var selectedType = 'received';
    var givenValue = '';
    var url = '/api/v1.0/messages/received';

    init();

    function init() {
        $scope.typeOfMessages = 'Received messages';

        getAllMessages();
    }

    $scope.getReceivedMessages = function () {
        url = '/api/v1.0/messages/received';
        selectedType = 'received';
        $scope.typeOfMessages = 'Received messages';

        getAllMessages();
    };

    $scope.getSentMessages = function () {
        url = '/api/v1.0/messages/sent';
        selectedType = 'sent';
        $scope.typeOfMessages = 'Sent messages';

        getAllMessages();
    };

    function getAllMessages() {
        if(givenValue.length > 0) {
            $scope.getMessagesByText();
            return;
        }

        $http({
            url: url,
            method: "GET"
        })
            .then(function (response) {
                $scope.messages = response.data;
                $scope.numberOfMessages = '(' + response.data.length + ')';

                if(response.data.length === 0) {
                    $scope.noResults = 'No results found!';
                } else {
                    $scope.noResults = '';
                }
            });
    }

    $scope.getMessagesByText = function () {
        givenValue = angular.element('#search-value').val();

        if(selectedType === 'received') {
            url = '/api/v1.0/messages/received';
        } else if(selectedType === 'sent') {
            url = '/api/v1.0/messages/sent';
        }

        getMessagesByText();
    };

    function getMessagesByText() {
        $http({
            url: url,
            method: "GET",
            params: {
                q: givenValue
            }
        })
            .then(function (response) {
                $scope.messages = response.data;
                $scope.numberOfMessages = '(' + response.data.length + ')';

                if(response.data.length === 0) {
                    $scope.noResults = 'No results found!';
                } else {
                    $scope.noResults = '';
                }
            });
    }
});