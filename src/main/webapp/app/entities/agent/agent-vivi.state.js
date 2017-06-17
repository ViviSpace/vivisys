(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('agent-vivi', {
            parent: 'entity',
            url: '/agent-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.agent.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agent/agentsVivi.html',
                    controller: 'AgentViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agent');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('agent-vivi-detail', {
            parent: 'agent-vivi',
            url: '/agent-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.agent.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/agent/agent-vivi-detail.html',
                    controller: 'AgentViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('agent');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Agent', function($stateParams, Agent) {
                    return Agent.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'agent-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('agent-vivi-detail.edit', {
            parent: 'agent-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent/agent-vivi-dialog.html',
                    controller: 'AgentViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Agent', function(Agent) {
                            return Agent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agent-vivi.new', {
            parent: 'agent-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent/agent-vivi-dialog.html',
                    controller: 'AgentViviDialogController',
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
                    $state.go('agent-vivi', null, { reload: 'agent-vivi' });
                }, function() {
                    $state.go('agent-vivi');
                });
            }]
        })
        .state('agent-vivi.edit', {
            parent: 'agent-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent/agent-vivi-dialog.html',
                    controller: 'AgentViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Agent', function(Agent) {
                            return Agent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agent-vivi', null, { reload: 'agent-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('agent-vivi.delete', {
            parent: 'agent-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/agent/agent-vivi-delete-dialog.html',
                    controller: 'AgentViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Agent', function(Agent) {
                            return Agent.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('agent-vivi', null, { reload: 'agent-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
