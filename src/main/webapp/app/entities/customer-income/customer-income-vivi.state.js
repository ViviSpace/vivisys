(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-income-vivi', {
            parent: 'entity',
            url: '/customer-income-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.customerIncome.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-income/customer-incomesVivi.html',
                    controller: 'CustomerIncomeViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerIncome');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('customer-income-vivi-detail', {
            parent: 'customer-income-vivi',
            url: '/customer-income-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.customerIncome.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer-income/customer-income-vivi-detail.html',
                    controller: 'CustomerIncomeViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customerIncome');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'CustomerIncome', function($stateParams, CustomerIncome) {
                    return CustomerIncome.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'customer-income-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('customer-income-vivi-detail.edit', {
            parent: 'customer-income-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-income/customer-income-vivi-dialog.html',
                    controller: 'CustomerIncomeViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerIncome', function(CustomerIncome) {
                            return CustomerIncome.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-income-vivi.new', {
            parent: 'customer-income-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-income/customer-income-vivi-dialog.html',
                    controller: 'CustomerIncomeViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customer-income-vivi', null, { reload: 'customer-income-vivi' });
                }, function() {
                    $state.go('customer-income-vivi');
                });
            }]
        })
        .state('customer-income-vivi.edit', {
            parent: 'customer-income-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-income/customer-income-vivi-dialog.html',
                    controller: 'CustomerIncomeViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['CustomerIncome', function(CustomerIncome) {
                            return CustomerIncome.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-income-vivi', null, { reload: 'customer-income-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-income-vivi.delete', {
            parent: 'customer-income-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer-income/customer-income-vivi-delete-dialog.html',
                    controller: 'CustomerIncomeViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['CustomerIncome', function(CustomerIncome) {
                            return CustomerIncome.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-income-vivi', null, { reload: 'customer-income-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
