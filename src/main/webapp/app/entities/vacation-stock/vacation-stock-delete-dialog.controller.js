(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('VacationStockDeleteController',VacationStockDeleteController);

    VacationStockDeleteController.$inject = ['$uibModalInstance', 'entity', 'VacationStock'];

    function VacationStockDeleteController($uibModalInstance, entity, VacationStock) {
        var vm = this;

        vm.vacationStock = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VacationStock.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
