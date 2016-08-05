(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('VacationRequestDeleteController',VacationRequestDeleteController);

    VacationRequestDeleteController.$inject = ['$uibModalInstance', 'entity', 'VacationRequest'];

    function VacationRequestDeleteController($uibModalInstance, entity, VacationRequest) {
        var vm = this;

        vm.vacationRequest = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;
        
        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            VacationRequest.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
