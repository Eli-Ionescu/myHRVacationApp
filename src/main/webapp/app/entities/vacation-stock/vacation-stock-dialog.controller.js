(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('VacationStockDialogController', VacationStockDialogController);

    VacationStockDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VacationStock'];

    function VacationStockDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VacationStock) {
        var vm = this;

        vm.vacationStock = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vacationStock.id !== null) {
                VacationStock.update(vm.vacationStock, onSaveSuccess, onSaveError);
            } else {
                VacationStock.save(vm.vacationStock, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myHrAppConcediuApp:vacationStockUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
