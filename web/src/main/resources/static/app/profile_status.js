var app = angular.module('app', []);

app.controller('ProfileController', function ($scope, $http, $compile) {

    var url;
    var method;

    init();

    function init() {
        initProfile();

        initBtnStatus();
    }

    function initProfile() {
        $http({
            url: '/api/v1.0/users/' + angular.element('#username').val(),
            method: "GET"
        })
            .then(function (response) {
                $scope.profile = response.data;
            });
    }

    function initBtnStatus() {
        $http({
            url: '/api/v1.0/users/' + angular.element('#username').val() + '/status',
            method: "GET"
        })
            .then(function (result) {
                if(result.data.status === 'FRIEND') {
                    $scope.btnText = 'Remove from friends!';
                    url = '/api/v1.0/relations/friends/';
                    method = 'DELETE';
                } else if(result.data.status === 'INVITATION_FROM_YOU') {
                    $scope.btnText = 'Remove invitation!';
                    url = '/api/v1.0/relations/invitations/';
                    method = 'DELETE';
                } else if(result.data.status === 'INVITATION_TO_YOU') {
                    $scope.btnText = 'Accept the invitation!';
                    url = '/api/v1.0/relations/friends/';
                    method = 'POST';

                    /*
                        // Create an invitation rejection button
                     */
                    angular.element('.btn-group').append($compile('<button data-ng-click="rejectInvitation()" id="btnRejectInvitation" class="btn btn-danger ng-pristine ng-untouched ng-valid ng-binding ng-empty">Reject invitation!</button>')($scope));
                } else if(result.data.status === 'UNKNOWN') {
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
        send()
    };

    $scope.rejectInvitation = function () {
        rejectInvitation();
    };

    function send() {
        $http({
            url: url + angular.element('#username').val(),
            method: method
        })
            .then(function () {
                /*
                  // After accepting the invitation, hide the button rejecting the invitation
                 */
                if(url === '/api/v1.0/relations/friends/' && method === 'POST') {
                    angular.element('#btnRejectInvitation')[0].style.display = 'none';
                }
                initBtnStatus();
            })
            .catch(function (error) {
                initBtnStatus();
            });
    }

    function rejectInvitation() {
        $http({
            url: '/api/v1.0/relations/invitations/' + angular.element('#username').val() + '/reject',
            method: 'DELETE'
        })
            .then(function () {
                /*
                  // After rejection the invitation, hide the button rejecting the invitation
                 */
                angular.element('#btnRejectInvitation')[0].style.display = 'none';
                initBtnStatus();
            })
            .catch(function (error) {
                initBtnStatus();
            });
    }
});