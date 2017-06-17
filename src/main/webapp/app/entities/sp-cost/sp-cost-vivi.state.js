(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('sp-cost-vivi', {
            parent: 'entity',
            url: '/sp-cost-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.spCost.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sp-cost/sp-costsVivi.html',
                    controller: 'SpCostViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('spCost');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('sp-cost-vivi-detail', {
            parent: 'sp-cost-vivi',
            url: '/sp-cost-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.spCost.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/sp-cost/sp-cost-vivi-detail.html',
                    controller: 'SpCostViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('spCost');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'SpCost', function($stateParams, SpCost) {
                    return SpCost.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'sp-cost-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('sp-cost-vivi-detail.edit', {
            parent: 'sp-cost-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sp-cost/sp-cost-vivi-dialog.html',
                    controller: 'SpCostViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SpCost', function(SpCost) {
                            return SpCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sp-cost-vivi.new', {
            parent: 'sp-cost-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sp-cost/sp-cost-vivi-dialog.html',
                    controller: 'SpCostViviDialogController',
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
                    $state.go('sp-cost-vivi', null, { reload: 'sp-cost-vivi' });
                }, function() {
                    $state.go('sp-cost-vivi');
                });
            }]
        })
        .state('sp-cost-vivi.edit', {
            parent: 'sp-cost-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sp-cost/sp-cost-vivi-dialog.html',
                    controller: 'SpCostViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['SpCost', function(SpCost) {
                            return SpCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sp-cost-vivi', null, { reload: 'sp-cost-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('sp-cost-vivi.delete', {
            parent: 'sp-cost-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/sp-cost/sp-cost-vivi-delete-dialog.html',
                    controller: 'SpCostViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['SpCost', function(SpCost) {
                            return SpCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('sp-cost-vivi', null, { reload: 'sp-cost-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
