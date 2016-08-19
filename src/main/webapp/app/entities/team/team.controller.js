(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .controller('TeamController', TeamController);

    TeamController.$inject = ['$scope', '$state', 'Team'];

    function TeamController ($scope, $state, Team) {
        var vm = this;

        vm.teams = [];

        loadAll();

        function loadAll() {
            Team.query(function(result) {
                vm.teams = result;
                console.log(vm.teams);
            });
        }
    }
})();
