(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('VacationRequestController', VacationRequestController);

    VacationRequestController.$inject = ['$scope', '$state', 'VacationRequest','Principal'];

    function VacationRequestController ($scope, $state, VacationRequest, Principal) {
        var vm = this;

        vm.vacationRequests = [];

        Principal.hasAuthority("ROLE_MANAGER").then(
                    function (authority){
                        vm.hasEditAuthority = authority;
                        console.log(vm.hasEditAuthority);
                     });

        loadAll();

        function loadAll() {
            VacationRequest.query(function(result) {
                vm.vacationRequests = result;
            });
        }
    }
})();
