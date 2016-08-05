(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('EmployeeDetailController', EmployeeDetailController);

    EmployeeDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Employee', 'VacationStock', 'VacationRequest', 'Job', 'Department'];

    function EmployeeDetailController($scope, $rootScope, $stateParams, previousState, entity, Employee, VacationStock, VacationRequest, Job, Department) {
        var vm = this;

        vm.employee = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myHrAppConcediuApp:employeeUpdate', function(event, result) {
            vm.employee = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
