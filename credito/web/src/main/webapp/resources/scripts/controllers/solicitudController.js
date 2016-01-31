'use strict';

var app = angular.module('credito-financiera');

app.controller('SolicitudController', ['$scope', '$http', function($scope, $http) 
{
	var solicitud = { id : '123456', 
			  nombreGrupo : "PRUEBA JBPM",
			  tipoRevision : "ANALISTA JR",
			  clientes : [ { id: "01", nombre: "Francisco Rocha", monto:20000 },
	                        { id: "02", nombre: "Juan Perez", monto:50000 } ],
	          monto: 70000
           };

	$scope.solicitud = solicitud;

}]);

