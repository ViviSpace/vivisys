(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('service-provider-vivi', {
            parent: 'entity',
            url: '/service-provider-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.serviceProvider.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-provider/service-providersVivi.html',
                    controller: 'ServiceProviderViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceProvider');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('service-provider-vivi-detail', {
            parent: 'service-provider-vivi',
            url: '/service-provider-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.serviceProvider.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/service-provider/service-provider-vivi-detail.html',
                    controller: 'ServiceProviderViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serviceProvider');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ServiceProvider', function($stateParams, ServiceProvider) {
                    return ServiceProvider.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'service-provider-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('service-provider-vivi-detail.edit', {
            parent: 'service-provider-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-provider/service-provider-vivi-dialog.html',
                    controller: 'ServiceProviderViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceProvider', function(ServiceProvider) {
                            return ServiceProvider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-provider-vivi.new', {
            parent: 'service-provider-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-provider/service-provider-vivi-dialog.html',
                    controller: 'ServiceProviderViviDialogController',
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
                    $state.go('service-provider-vivi', null, { reload: 'service-provider-vivi' });
                }, function() {
                    $state.go('service-provider-vivi');
                });
            }]
        })
        .state('service-provider-vivi.edit', {
            parent: 'service-provider-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-provider/service-provider-vivi-dialog.html',
                    controller: 'ServiceProviderViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ServiceProvider', function(ServiceProvider) {
                            return ServiceProvider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-provider-vivi', null, { reload: 'service-provider-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('service-provider-vivi.delete', {
            parent: 'service-provider-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/service-provider/service-provider-vivi-delete-dialog.html',
                    controller: 'ServiceProviderViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ServiceProvider', function(ServiceProvider) {
                            return ServiceProvider.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('service-provider-vivi', null, { reload: 'service-provider-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
