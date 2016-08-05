(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('VacationStockController', VacationStockController);

    VacationStockController.$inject = ['$scope', '$state', 'VacationStock'];

    function VacationStockController ($scope, $state, VacationStock) {
        var vm = this;
        
        vm.vacationStocks = [];

        loadAll();

        function loadAll() {
            VacationStock.query(function(result) {
                vm.vacationStocks = result;
            });
        }
    }
})();
