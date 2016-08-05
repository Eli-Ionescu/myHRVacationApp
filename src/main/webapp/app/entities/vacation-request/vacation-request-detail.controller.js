(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('VacationRequestDetailController', VacationRequestDetailController);

    VacationRequestDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'VacationRequest', 'Employee'];

    function VacationRequestDetailController($scope, $rootScope, $stateParams, previousState, entity, VacationRequest, Employee) {
        var vm = this;

        vm.vacationRequest = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('myHrAppConcediuApp:vacationRequestUpdate', function(event, result) {
            vm.vacationRequest = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
