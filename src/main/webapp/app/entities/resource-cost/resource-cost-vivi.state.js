(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('resource-cost-vivi', {
            parent: 'entity',
            url: '/resource-cost-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.resourceCost.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resource-cost/resource-costsVivi.html',
                    controller: 'ResourceCostViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resourceCost');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('resource-cost-vivi-detail', {
            parent: 'resource-cost-vivi',
            url: '/resource-cost-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.resourceCost.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resource-cost/resource-cost-vivi-detail.html',
                    controller: 'ResourceCostViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resourceCost');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ResourceCost', function($stateParams, ResourceCost) {
                    return ResourceCost.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'resource-cost-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('resource-cost-vivi-detail.edit', {
            parent: 'resource-cost-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource-cost/resource-cost-vivi-dialog.html',
                    controller: 'ResourceCostViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ResourceCost', function(ResourceCost) {
                            return ResourceCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resource-cost-vivi.new', {
            parent: 'resource-cost-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource-cost/resource-cost-vivi-dialog.html',
                    controller: 'ResourceCostViviDialogController',
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
                    $state.go('resource-cost-vivi', null, { reload: 'resource-cost-vivi' });
                }, function() {
                    $state.go('resource-cost-vivi');
                });
            }]
        })
        .state('resource-cost-vivi.edit', {
            parent: 'resource-cost-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource-cost/resource-cost-vivi-dialog.html',
                    controller: 'ResourceCostViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ResourceCost', function(ResourceCost) {
                            return ResourceCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resource-cost-vivi', null, { reload: 'resource-cost-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resource-cost-vivi.delete', {
            parent: 'resource-cost-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource-cost/resource-cost-vivi-delete-dialog.html',
                    controller: 'ResourceCostViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ResourceCost', function(ResourceCost) {
                            return ResourceCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resource-cost-vivi', null, { reload: 'resource-cost-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
