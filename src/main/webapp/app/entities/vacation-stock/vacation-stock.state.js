(function() {
    'use strict';

    angular
        .module('myHrAppConcediuApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('vacation-stock', {
            parent: 'entity',
            url: '/vacation-stock',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'VacationStocks'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vacation-stock/vacation-stocks.html',
                    controller: 'VacationStockController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
            }
        })
        .state('vacation-stock-detail', {
            parent: 'entity',
            url: '/vacation-stock/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'VacationStock'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/vacation-stock/vacation-stock-detail.html',
                    controller: 'VacationStockDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                entity: ['$stateParams', 'VacationStock', function($stateParams, VacationStock) {
                    return VacationStock.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'vacation-stock',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('vacation-stock-detail.edit', {
            parent: 'vacation-stock-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation-stock/vacation-stock-dialog.html',
                    controller: 'VacationStockDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VacationStock', function(VacationStock) {
                            return VacationStock.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vacation-stock.new', {
            parent: 'vacation-stock',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation-stock/vacation-stock-dialog.html',
                    controller: 'VacationStockDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                freeDays: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('vacation-stock', null, { reload: true });
                }, function() {
                    $state.go('vacation-stock');
                });
            }]
        })
        .state('vacation-stock.edit', {
            parent: 'vacation-stock',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation-stock/vacation-stock-dialog.html',
                    controller: 'VacationStockDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['VacationStock', function(VacationStock) {
                            return VacationStock.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vacation-stock', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('vacation-stock.delete', {
            parent: 'vacation-stock',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/vacation-stock/vacation-stock-delete-dialog.html',
                    controller: 'VacationStockDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['VacationStock', function(VacationStock) {
                            return VacationStock.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('vacation-stock', null, { reload: true });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
