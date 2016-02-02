'use strict';

var app = angular.module('credito-financiera',['ngRoute','ngResource']);

app.config(['$routeProvider', function($routeProvider) {
    $routeProvider
      .when('/',{templateUrl:'resources/views/landing.html',controller:'LandingPageController'})
      .when('/Captura',{templateUrl:'resources/views/credito/captura.html',controller:'CapturaController'})
      .when('/Solicitud/:solicitud',{templateUrl:'resources/views/credito/solicitud.html',controller:'SolicitudController'})
      .when('/Pendientes',{templateUrl:'resources/views/credito/pendientes.html',controller:'PendientesController'})
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