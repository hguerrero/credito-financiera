'use strict';

var app = angular.module('credito-financiera');

app.controller('PendientesController', ['$scope', '$http', function($scope, $http) 
{
    $scope.solicitudes = [];

    $http.get("/credito-web/rest/credito/pendientes/user1")
    .then(function(response){
        $scope.solicitudes = response.data;
    });
}]);
