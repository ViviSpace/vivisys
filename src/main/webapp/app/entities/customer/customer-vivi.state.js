(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('customer-vivi', {
            parent: 'entity',
            url: '/customer-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.customer.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer/customersVivi.html',
                    controller: 'CustomerViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customer');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('customer-vivi-detail', {
            parent: 'customer-vivi',
            url: '/customer-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.customer.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/customer/customer-vivi-detail.html',
                    controller: 'CustomerViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('customer');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Customer', function($stateParams, Customer) {
                    return Customer.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'customer-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('customer-vivi-detail.edit', {
            parent: 'customer-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-vivi-dialog.html',
                    controller: 'CustomerViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Customer', function(Customer) {
                            return Customer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-vivi.new', {
            parent: 'customer-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-vivi-dialog.html',
                    controller: 'CustomerViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                type: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('customer-vivi', null, { reload: 'customer-vivi' });
                }, function() {
                    $state.go('customer-vivi');
                });
            }]
        })
        .state('customer-vivi.edit', {
            parent: 'customer-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-vivi-dialog.html',
                    controller: 'CustomerViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Customer', function(Customer) {
                            return Customer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-vivi', null, { reload: 'customer-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('customer-vivi.delete', {
            parent: 'customer-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/customer/customer-vivi-delete-dialog.html',
                    controller: 'CustomerViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Customer', function(Customer) {
                            return Customer.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('customer-vivi', null, { reload: 'customer-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
