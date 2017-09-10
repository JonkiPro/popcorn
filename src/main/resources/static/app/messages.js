var app = angular.module('app', []);

app.controller('MessagesController', function ($scope, $http) {

    var selectedType = 'received';
    var givenValue = '';
    var url = '/messages/getReceivedMessages';

    init();

    function init() {
        $scope.typeOfMessages = 'Received messages';

        getAllMessages();
    }

    $scope.getReceivedMessages = function () {
        url = '/messages/getReceivedMessages';
        selectedType = 'received';
        $scope.typeOfMessages = 'Received messages';

        getAllMessages();
    };

    $scope.getSentMessages = function () {
        url = '/messages/getSentMessages';
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
                $scope.noResults = '';
            })
            .catch(function () {
                $scope.messages = null;
                $scope.numberOfMessages = '(0)';
                $scope.noResults = 'No results found!';
            });
    }

    $scope.getMessagesByText = function () {
        givenValue = angular.element('#search-value').val();

        if(selectedType === 'received') {
            url = '/messages/searchReceivedMessages';
        } else if(selectedType === 'sent') {
            url = '/messages/searchSentMessages';
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
                $scope.noResults = '';
            })
            .catch(function () {
                $scope.messages = null;
                $scope.numberOfMessages = '(0)';
                $scope.noResults = 'No results found!';
            });
    }
});