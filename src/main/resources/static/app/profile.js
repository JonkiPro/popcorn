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
            url: '/users/getProfile',
            method: "GET",
            params: {
                username: angular.element('#username').val()
            }
        })
            .then(function (response) {
                $scope.profile = response.data;
            });
    }

    function initBtnStatus() {
        $http({
            url: '/relations/getStatus',
            method: "GET",
            params: {
                username: angular.element('#username').val()
            }
        })
            .then(function (result) {
                if(result.data.status === 'isFriend') {
                    $scope.btnText = 'Remove from friends!';
                    url = '/relations/removeFriend';
                    method = 'DELETE';
                } else if(result.data.status === 'existsInvitation') {
                    $scope.btnText = 'Remove invitation!';
                    url = '/relations/removeInvitation';
                    method = 'DELETE';
                } else if(result.data.status === 'existsInvitationToYou') {
                    $scope.btnText = 'Accept the invitation!';
                    url = '/relations/addFriend';
                    method = 'POST';

                    /*
                        // Create an invitation rejection button
                     */
                    angular.element('.btn-group').append($compile('<button data-ng-click="rejectInvitation()" id="btnRejectInvitation" class="btn btn-danger ng-pristine ng-untouched ng-valid ng-binding ng-empty">Reject invitation!</button>')($scope));
                } else if(result.data.status === 'unknown') {
                    $scope.btnText = 'Add to friends!';
                    url = '/relations/sendInvitation';
                    method = 'POST';
                }
            });

    }

    $scope.sendAction = function () {
        send()
    };

    $scope.rejectInvitation = function () {
        url = '/relations/rejectInvitation';
        method = 'DELETE';

        send()
    };

    function send() {
        $http({
            url: url,
            method: method,
            params: {
                username: angular.element('#username').val()
            }
        })
            .then(function () {
                initBtnStatus();

                /*
                    // After accepting the invitation or rejection, hide the button rejecting the invitation
                 */
                if(url === '/relations/addFriend' || url === '/relations/rejectInvitation') {
                    angular.element('#btnRejectInvitation')[0].style.display = 'none';
                }
            })
            .catch(function (error) {

            });
    }
});