(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('ord-vivi', {
            parent: 'entity',
            url: '/ord-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.ord.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ord/ordsVivi.html',
                    controller: 'OrdViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ord');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('ord-vivi-detail', {
            parent: 'ord-vivi',
            url: '/ord-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.ord.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/ord/ord-vivi-detail.html',
                    controller: 'OrdViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('ord');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Ord', function($stateParams, Ord) {
                    return Ord.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'ord-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('ord-vivi-detail.edit', {
            parent: 'ord-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ord/ord-vivi-dialog.html',
                    controller: 'OrdViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ord', function(Ord) {
                            return Ord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ord-vivi.new', {
            parent: 'ord-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ord/ord-vivi-dialog.html',
                    controller: 'OrdViviDialogController',
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
                    $state.go('ord-vivi', null, { reload: 'ord-vivi' });
                }, function() {
                    $state.go('ord-vivi');
                });
            }]
        })
        .state('ord-vivi.edit', {
            parent: 'ord-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ord/ord-vivi-dialog.html',
                    controller: 'OrdViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Ord', function(Ord) {
                            return Ord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ord-vivi', null, { reload: 'ord-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('ord-vivi.delete', {
            parent: 'ord-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/ord/ord-vivi-delete-dialog.html',
                    controller: 'OrdViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Ord', function(Ord) {
                            return Ord.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('ord-vivi', null, { reload: 'ord-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
