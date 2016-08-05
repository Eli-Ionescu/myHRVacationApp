(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('VacationStockDetailController', VacationStockDetailController);

    VacationStockDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VacationStock'];

    function VacationStockDetailController($scope, $rootScope, $stateParams, previousState, entity, VacationStock) {
        var vm = this;

        vm.vacationStock = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myHrAppConcediuApp:vacationStockUpdate', function(event, result) {
            vm.vacationStock = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
