(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('VacationRequestDialogController', VacationRequestDialogController);

    VacationRequestDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'VacationRequest', 'Employee', 'Principal'];

    function VacationRequestDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, VacationRequest, Employee, Principal) {
        var vm = this;

        vm.vacationRequest = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.employees = Employee.query();

        Principal.hasAuthority("ROLE_MANAGER").then(
            function (authority){
                vm.hasSaveAuthority = authority;
                vm.hasStatusAuthority = !authority;
                console.log(vm.hasStatusAuthority);
             });



        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.vacationRequest.id !== null) {
                VacationRequest.update(vm.vacationRequest, onSaveSuccess, onSaveError);
            } else {
                VacationRequest.save(vm.vacationRequest, onSaveSuccess, onSaveError);
            }
        }

        function approve () {
            VacationRequest.approve(vm.vacationRequest, onSaveSuccess, onSaveError);

        }

        function reject () {
            VacationRequest.reject(vm.vacationRequest, onSaveSuccess, onSaveError);
        }


        function onSaveSuccess (result) {
            $scope.$emit('myHrAppConcediuApp:vacationRequestUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.period = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }


    }
})();
