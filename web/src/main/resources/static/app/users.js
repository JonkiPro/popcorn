var app = angular.module('app', []);

app.controller('UsersController', function ($scope, $http) {

    $scope.page = angular.element('#page').val();
    $scope.size = angular.element('#size').val();

    $scope.btnPrev = false;

    if(angular.element('#search-value').val() === '') {
        angular.element('#search-value').val('');
        $scope.size = 1;
        $scope.page = 1;
    }

    init();

    function init() {
        getAllByUsername();
    }

    function getAllByUsername() {
        $http({
            url: '/api/v1.0/users',
            method: "GET",
            params: {
                q: angular.element('#search-value').val(),
                size: parseInt($scope.size),
                page: (parseInt($scope.page)-1)
            }
        })
            .then(function (response) {
                $scope.users = (response.data._embedded || []).userSearchResults;
                $scope.numberOfUsers = response.data.page.totalElements;
                $scope.numberOfPages = response.data.page.totalPages;
                $scope.page = response.data.page.number+1;

                if(parseInt($scope.numberOfUsers) > 0) {
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
                    $scope.noResults = 'No results found!';
                    $scope.btnPrev = false;
                    $scope.btnNext = false;
                }
            });
    }

    $scope.getAllByUsernameByPageSize = function () {
        if (parseInt($scope.size) < 1 || !$scope.size) {
            $scope.size = 1;
        }
        $scope.page = 1;

        getAllByUsername();
    };

    $scope.getAllByUsernameByPage = function () {
        if (parseInt($scope.page) < 1 || !$scope.page) {
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