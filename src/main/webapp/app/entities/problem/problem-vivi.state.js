(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('problem-vivi', {
            parent: 'entity',
            url: '/problem-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.problem.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/problem/problemsVivi.html',
                    controller: 'ProblemViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('problem');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('problem-vivi-detail', {
            parent: 'problem-vivi',
            url: '/problem-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.problem.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/problem/problem-vivi-detail.html',
                    controller: 'ProblemViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('problem');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Problem', function($stateParams, Problem) {
                    return Problem.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'problem-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('problem-vivi-detail.edit', {
            parent: 'problem-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/problem/problem-vivi-dialog.html',
                    controller: 'ProblemViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Problem', function(Problem) {
                            return Problem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('problem-vivi.new', {
            parent: 'problem-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/problem/problem-vivi-dialog.html',
                    controller: 'ProblemViviDialogController',
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
                    $state.go('problem-vivi', null, { reload: 'problem-vivi' });
                }, function() {
                    $state.go('problem-vivi');
                });
            }]
        })
        .state('problem-vivi.edit', {
            parent: 'problem-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/problem/problem-vivi-dialog.html',
                    controller: 'ProblemViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Problem', function(Problem) {
                            return Problem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('problem-vivi', null, { reload: 'problem-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('problem-vivi.delete', {
            parent: 'problem-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/problem/problem-vivi-delete-dialog.html',
                    controller: 'ProblemViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Problem', function(Problem) {
                            return Problem.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('problem-vivi', null, { reload: 'problem-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
