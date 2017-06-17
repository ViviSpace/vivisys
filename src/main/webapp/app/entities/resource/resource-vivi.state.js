(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('resource-vivi', {
            parent: 'entity',
            url: '/resource-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.resource.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resource/resourcesVivi.html',
                    controller: 'ResourceViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resource');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('resource-vivi-detail', {
            parent: 'resource-vivi',
            url: '/resource-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.resource.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/resource/resource-vivi-detail.html',
                    controller: 'ResourceViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('resource');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Resource', function($stateParams, Resource) {
                    return Resource.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'resource-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('resource-vivi-detail.edit', {
            parent: 'resource-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource/resource-vivi-dialog.html',
                    controller: 'ResourceViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resource', function(Resource) {
                            return Resource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resource-vivi.new', {
            parent: 'resource-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource/resource-vivi-dialog.html',
                    controller: 'ResourceViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                description: null,
                                cost: null,
                                unit: null,
                                type: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('resource-vivi', null, { reload: 'resource-vivi' });
                }, function() {
                    $state.go('resource-vivi');
                });
            }]
        })
        .state('resource-vivi.edit', {
            parent: 'resource-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource/resource-vivi-dialog.html',
                    controller: 'ResourceViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Resource', function(Resource) {
                            return Resource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resource-vivi', null, { reload: 'resource-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('resource-vivi.delete', {
            parent: 'resource-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/resource/resource-vivi-delete-dialog.html',
                    controller: 'ResourceViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Resource', function(Resource) {
                            return Resource.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('resource-vivi', null, { reload: 'resource-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
