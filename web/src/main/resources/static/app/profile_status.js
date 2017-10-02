var app = angular.module('app', []);

app.controller('ProfileController', function ($scope, $http, $compile) {

    var url;
    var method;
    var param;

    init();

    function init() {
        initProfile();

        initBtnStatus();
    }

    function initProfile() {
        $http({
            url: '/api/v1.0/users/account/' + angular.element('#username').val(),
            method: "GET"
        })
            .then(function (response) {
                $scope.profile = response.data;
            });
    }

    function initBtnStatus() {
        $http({
            url: '/api/v1.0/relations/status/' + angular.element('#username').val(),
            method: "GET"
        })
            .then(function (result) {
                if(result.data === 'FRIEND') {
                    $scope.btnText = 'Remove from friends!';
                    url = '/api/v1.0/relations/friends/';
                    method = 'DELETE';
                } else if(result.data === 'INVITATION_FROM_YOU') {
                    $scope.btnText = 'Remove invitation!';
                    url = '/api/v1.0/relations/invitations/';
                    method = 'DELETE';
                    param = 'remove';
                } else if(result.data === 'INVITATION_TO_YOU') {
                    $scope.btnText = 'Accept the invitation!';
                    url = '/api/v1.0/relations/friends/';
                    method = 'POST';

                    /*
                        // Create an invitation rejection button
                     */
                    angular.element('.btn-group').append($compile('<button data-ng-click="rejectInvitation()" id="btnRejectInvitation" class="btn btn-danger ng-pristine ng-untouched ng-valid ng-binding ng-empty">Reject invitation!</button>')($scope));
                } else if(result.data === 'UNKNOWN') {
                    $scope.btnText = 'Add to friends!';
                    url = '/api/v1.0/relations/invitations/';
                    method = 'POST';
                }
                $scope.authorisedUser = true;
            })
            .catch(function (error) {
                angular.element('#div-auth').remove();
            });
    }

    $scope.sendAction = function () {
        if(param === 'remove') {
            sendDelete();

            return;
        }

        sendPost()
    };

    $scope.rejectInvitation = function () {
        url = '/api/v1.0/relations/invitations/';
        method = 'DELETE';
        param = 'reject';

        sendDelete();
    };

    function sendPost() {
        $http({
            url: url + angular.element('#username').val(),
            method: method
        })
            .then(function () {
                initBtnStatus();
            })
            .catch(function (error) {
                initBtnStatus();
            });

        /*
          // After accepting the invitation, hide the button rejecting the invitation
         */
        if(url === '/api/v1.0/relations/friends/' && method === 'POST') {
            angular.element('#btnRejectInvitation')[0].style.display = 'none';
            param = null;
        }
    }

    function sendDelete() {
        $http({
            url: url + angular.element('#username').val(),
            method: method,
            params: {
                action: param
            }
        })
            .then(function () {
                initBtnStatus();
            })
            .catch(function (error) {
                initBtnStatus();
            });
        /*
          // After accepting the invitation or rejection, hide the button rejecting the invitation
         */
        if(param === 'reject') {
            angular.element('#btnRejectInvitation')[0].style.display = 'none';
        }
        param = null;
    }
});