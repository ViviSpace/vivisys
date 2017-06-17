(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('agent-cost-vivi', {
            parent: 'entity',
            url: '/agent-cost-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.agentCost.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agent-cost/agent-costsVivi.html',
                    controller: 'AgentCostViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agentCost');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('agent-cost-vivi-detail', {
            parent: 'agent-cost-vivi',
            url: '/agent-cost-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.agentCost.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agent-cost/agent-cost-vivi-detail.html',
                    controller: 'AgentCostViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agentCost');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'AgentCost', function($stateParams, AgentCost) {
                    return AgentCost.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'agent-cost-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('agent-cost-vivi-detail.edit', {
            parent: 'agent-cost-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-cost/agent-cost-vivi-dialog.html',
                    controller: 'AgentCostViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgentCost', function(AgentCost) {
                            return AgentCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agent-cost-vivi.new', {
            parent: 'agent-cost-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-cost/agent-cost-vivi-dialog.html',
                    controller: 'AgentCostViviDialogController',
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
                    $state.go('agent-cost-vivi', null, { reload: 'agent-cost-vivi' });
                }, function() {
                    $state.go('agent-cost-vivi');
                });
            }]
        })
        .state('agent-cost-vivi.edit', {
            parent: 'agent-cost-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-cost/agent-cost-vivi-dialog.html',
                    controller: 'AgentCostViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['AgentCost', function(AgentCost) {
                            return AgentCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agent-cost-vivi', null, { reload: 'agent-cost-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agent-cost-vivi.delete', {
            parent: 'agent-cost-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent-cost/agent-cost-vivi-delete-dialog.html',
                    controller: 'AgentCostViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['AgentCost', function(AgentCost) {
                            return AgentCost.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agent-cost-vivi', null, { reload: 'agent-cost-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
