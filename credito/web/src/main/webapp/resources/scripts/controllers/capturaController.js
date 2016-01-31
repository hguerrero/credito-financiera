'use strict';

var app = angular.module('credito-financiera');

app.controller('CapturaController', ['$scope', '$http', function($scope, $http) 
{
	$scope.solicitud = { id: '', clientes : [], monto: 0, tipoRevisor: '', grupo: '', estatus: ''};
	
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

app.controller('pendientesCtrl', function($scope, $http) 
{
    $scope.solicitudes = [];

    $http.get("/credito-web/rest/credito/pendientes/analistajr")
    .then(function(response){
        $scope.solicitudes = response.data;
    });
});
