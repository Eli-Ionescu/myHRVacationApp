(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vacation-request', {
            parent: 'entity',
            url: '/vacation-request',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'VacationRequests'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vacation-request/vacation-requests.html',
                    controller: 'VacationRequestController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('vacation-request-detail', {
            parent: 'entity',
            url: '/vacation-request/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'VacationRequest'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vacation-request/vacation-request-detail.html',
                    controller: 'VacationRequestDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'VacationRequest', function($stateParams, VacationRequest) {
                    return VacationRequest.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vacation-request',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vacation-request-detail.edit', {
            parent: 'vacation-request-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation-request/vacation-request-dialog.html',
                    controller: 'VacationRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VacationRequest', function(VacationRequest) {
                            return VacationRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vacation-request.new', {
            parent: 'vacation-request',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation-request/vacation-request-dialog.html',
                    controller: 'VacationRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                vacationDays: null,
                                status: null,
                                period: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vacation-request', null, { reload: true });
                }, function() {
                    $state.go('vacation-request');
                });
            }]
        })
        .state('vacation-request.edit', {
            parent: 'vacation-request',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation-request/vacation-request-dialog.html',
                    controller: 'VacationRequestDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VacationRequest', function(VacationRequest) {
                            return VacationRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vacation-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vacation-request.delete', {
            parent: 'vacation-request',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation-request/vacation-request-delete-dialog.html',
                    controller: 'VacationRequestDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VacationRequest', function(VacationRequest) {
                            return VacationRequest.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vacation-request', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
