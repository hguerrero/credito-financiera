'use strict';

var app = angular.module('credito-financiera');

app.controller('CapturaController', ['$scope', '$http', function($scope, $http) 
{
	$scope.solicitud = { id: '', solicitud: '', clientes : [], monto: 0, tipoRevisor: '', grupo: '', estatus: 'NEW'};
	
    $scope.cliente = {};

    $scope.add = function() {
        var object = $scope.cliente;
        $scope.solicitud.monto += parseFloat(object.monto);
        $scope.solicitud.clientes.push( _.clone(object) );
        $scope.cliente = {};
    };

    $scope.initProcess = function() {
        $http.post("/credito-web/rest/credito/", $scope.solicitud)
        .then(function(response){
        });
    };
}]);

