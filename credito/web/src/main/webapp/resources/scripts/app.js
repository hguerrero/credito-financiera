'use strict';

var app = angular.module('credito-financiera',['ngRoute','ngResource']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'resources/views/landing.html',controller:'LandingPageController'})
      .when('/Captura',{templateUrl:'resources/views/credito/captura.html',controller:'CapturaController'})
      .when('/Members',{templateUrl:'resources/views/Member/search.html',controller:'SearchMemberController'})
      .when('/Members/new',{templateUrl:'resources/views/Member/detail.html',controller:'NewMemberController'})
      .when('/Members/edit/:MemberId',{templateUrl:'resources/views/Member/detail.html',controller:'EditMemberController'})
      .otherwise({
        redirectTo: '/'
      });
}]);

app.controller('LandingPageController', function LandingPageController() {
});

app.controller('NavController', function NavController($scope, $location) {
    $scope.matchesRoute = function(route) {
        var path = $location.path();
        return (path === ("/" + route) || path.indexOf("/" + route + "/") == 0);
    };
});