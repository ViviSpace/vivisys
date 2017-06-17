(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sp-deploy-vivi', {
            parent: 'entity',
            url: '/sp-deploy-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.spDeploy.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sp-deploy/sp-deploysVivi.html',
                    controller: 'SpDeployViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('spDeploy');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sp-deploy-vivi-detail', {
            parent: 'sp-deploy-vivi',
            url: '/sp-deploy-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.spDeploy.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sp-deploy/sp-deploy-vivi-detail.html',
                    controller: 'SpDeployViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('spDeploy');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SpDeploy', function($stateParams, SpDeploy) {
                    return SpDeploy.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sp-deploy-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sp-deploy-vivi-detail.edit', {
            parent: 'sp-deploy-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sp-deploy/sp-deploy-vivi-dialog.html',
                    controller: 'SpDeployViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SpDeploy', function(SpDeploy) {
                            return SpDeploy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sp-deploy-vivi.new', {
            parent: 'sp-deploy-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sp-deploy/sp-deploy-vivi-dialog.html',
                    controller: 'SpDeployViviDialogController',
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
                    $state.go('sp-deploy-vivi', null, { reload: 'sp-deploy-vivi' });
                }, function() {
                    $state.go('sp-deploy-vivi');
                });
            }]
        })
        .state('sp-deploy-vivi.edit', {
            parent: 'sp-deploy-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sp-deploy/sp-deploy-vivi-dialog.html',
                    controller: 'SpDeployViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SpDeploy', function(SpDeploy) {
                            return SpDeploy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sp-deploy-vivi', null, { reload: 'sp-deploy-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sp-deploy-vivi.delete', {
            parent: 'sp-deploy-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sp-deploy/sp-deploy-vivi-delete-dialog.html',
                    controller: 'SpDeployViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SpDeploy', function(SpDeploy) {
                            return SpDeploy.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sp-deploy-vivi', null, { reload: 'sp-deploy-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
