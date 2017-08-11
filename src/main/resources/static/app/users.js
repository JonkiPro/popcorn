var app = angular.module('app', []);

app.controller('UsersController', function ($scope, $http) {

    $scope.page = angular.element('#page').val();
    $scope.pageSize = angular.element('#page-size').val();

    $scope.btnPrev = false;

    if(angular.element('#search-value').val() === '') {
        angular.element('#search-value').val('');
        $scope.pageSize = 1;
        $scope.page=1;

        console.log('he');
    }
    console.log('he');

    init();

    function init() {
        getAllByUsername();
    }

    function getAllByUsername() {
        $http({
            url: '/users/getUsers',
            method: "GET",
            params: {
                q: angular.element('#search-value').val(),
                pageSize: parseInt($scope.pageSize),
                page: parseInt($scope.page)
            }
        })
            .then(function (response) {
                $scope.users = response.data;
            });

        $http({
            url: '/users/getNumberOfUsersByUsername',
            method: "GET",
            params: {
                username: angular.element('#search-value').val()
            }
        })
            .then(function (response) {
                $scope.numberOfUsers = response.data;

                if(parseInt($scope.numberOfUsers) > 0) {
                    $scope.numberOfPages = window.Math.ceil((parseInt($scope.numberOfUsers)) / parseInt($scope.pageSize));

                    if (parseInt($scope.page) > parseInt($scope.numberOfPages)) {
                        $scope.page = $scope.numberOfPages;
                        getUser();
                    }

                    if (isLastPage()) {
                        $scope.btnNext = false;
                    } else {
                        $scope.btnNext = true;
                    }
                    if (isFirstPage()) {
                        $scope.btnPrev = true;
                    } else {
                        $scope.btnPrev = false;
                    }

                    $scope.noResults = '';
                } else {
                    $scope.numberOfUsers = 0;
                    $scope.numberOfPages = 1;
                    $scope.noResults = 'No results found!';
                }
            });
    }

    function getUser() {
        return $http({
            url: '/users/getUsers',
            method: "GET",
            params: {
                q: angular.element('#search-value').val(),
                pageSize: parseInt($scope.pageSize),
                page: parseInt($scope.page)
            }
        })
            .then(function (response) {
                $scope.users = response.data;
            });
    }

    $scope.getAllByUsernameByPageSize = function () {
        if (parseInt($scope.pageSize) < 1) {
            $scope.pageSize = 1;
        }
        getAllByUsername();
    };

    $scope.getAllByUsernameByPage = function () {
        if (parseInt($scope.page) < 1) {
            $scope.page = 1;
        } else if (parseInt($scope.page) > parseInt($scope.numberOfPages)) {
            $scope.page = $scope.numberOfPages;
        }
        getAllByUsername();
    };

    $scope.getAllByUsernameWithInput = function () {
        $scope.page = 1;

        getAllByUsername();
    };

    $scope.getNextUsers = function () {
        $scope.page = parseInt($scope.page) + 1;

        getAllByUsername();
    };

    $scope.getPrevUsers = function () {
        $scope.page = parseInt($scope.page) - 1;

        getAllByUsername();
    };


    function isLastPage() {
        return parseFloat($scope.page) === parseFloat($scope.numberOfPages);
    }

    function isFirstPage() {
        return parseFloat($scope.page) > 1;
    }
});

app.directive('numbersOnly', function () {
    return {
        require: 'ngModel',
        link: function (scope, element, attr, ngModelCtrl) {
            function fromUser(text) {
                if (text) {
                    var transformedInput = text.replace(/[^0-9]/g, '');

                    if (transformedInput !== text) {
                        ngModelCtrl.$setViewValue(transformedInput);
                        ngModelCtrl.$render();
                    }
                    return transformedInput;
                }
                return undefined;
            }

            ngModelCtrl.$parsers.push(fromUser);
        }
    };
});