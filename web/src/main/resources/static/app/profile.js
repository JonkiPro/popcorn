var app = angular.module('app', []);

app.controller('ProfileController', function ($scope, $http, $compile) {

    init();

    function init() {
        initProfile();
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
});