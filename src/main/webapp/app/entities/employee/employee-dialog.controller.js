(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('EmployeeDialogController', EmployeeDialogController);

    EmployeeDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', '$q', 'entity', 'Employee', 'VacationStock', 'VacationRequest', 'Job', 'Department'];

    function EmployeeDialogController ($timeout, $scope, $stateParams, $uibModalInstance, $q, entity, Employee, VacationStock, VacationRequest, Job, Department) {
        var vm = this;

        vm.employee = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
//        vm.vacationstockids = VacationStock.query({filter: 'employee-is-null'});
//        $q.all([vm.employee.$promise, vm.vacationstockids.$promise]).then(function() {
//            if (!vm.employee.vacationStockId || !vm.employee.vacationStockId.id) {
//                return $q.reject();
//            }
//            return VacationStock.get({id : vm.employee.vacationStockId.id}).$promise;
//        }).then(function(vacationStockId) {
//            vm.vacationstockids.push(vacationStockId);
//        });
//        vm.vacationrequests = VacationRequest.query();
        vm.jobs = Job.query();
        vm.departments = Department.query();
        vm.employees = Employee.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.employee.id !== null) {
                Employee.update(vm.employee, onSaveSuccess, onSaveError);
            } else {
                Employee.save(vm.employee, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('myHrAppConcediuApp:employeeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.hireDate = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
