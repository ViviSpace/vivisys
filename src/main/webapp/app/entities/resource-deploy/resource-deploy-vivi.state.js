(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('resource-deploy-vivi', {
            parent: 'entity',
            url: '/resource-deploy-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.resourceDeploy.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resource-deploy/resource-deploysVivi.html',
                    controller: 'ResourceDeployViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resourceDeploy');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('resource-deploy-vivi-detail', {
            parent: 'resource-deploy-vivi',
            url: '/resource-deploy-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.resourceDeploy.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resource-deploy/resource-deploy-vivi-detail.html',
                    controller: 'ResourceDeployViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resourceDeploy');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ResourceDeploy', function($stateParams, ResourceDeploy) {
                    return ResourceDeploy.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'resource-deploy-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('resource-deploy-vivi-detail.edit', {
            parent: 'resource-deploy-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource-deploy/resource-deploy-vivi-dialog.html',
                    controller: 'ResourceDeployViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ResourceDeploy', function(ResourceDeploy) {
                            return ResourceDeploy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resource-deploy-vivi.new', {
            parent: 'resource-deploy-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource-deploy/resource-deploy-vivi-dialog.html',
                    controller: 'ResourceDeployViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                quantity: null,
                                price: null,
                                createdTime: null,
                                effictiveTime: null,
                                expriedTime: null,
                                type: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('resource-deploy-vivi', null, { reload: 'resource-deploy-vivi' });
                }, function() {
                    $state.go('resource-deploy-vivi');
                });
            }]
        })
        .state('resource-deploy-vivi.edit', {
            parent: 'resource-deploy-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource-deploy/resource-deploy-vivi-dialog.html',
                    controller: 'ResourceDeployViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ResourceDeploy', function(ResourceDeploy) {
                            return ResourceDeploy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resource-deploy-vivi', null, { reload: 'resource-deploy-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resource-deploy-vivi.delete', {
            parent: 'resource-deploy-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource-deploy/resource-deploy-vivi-delete-dialog.html',
                    controller: 'ResourceDeployViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ResourceDeploy', function(ResourceDeploy) {
                            return ResourceDeploy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resource-deploy-vivi', null, { reload: 'resource-deploy-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
