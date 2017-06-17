(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('problem-order-vivi', {
            parent: 'entity',
            url: '/problem-order-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.problemOrder.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/problem-order/problem-ordersVivi.html',
                    controller: 'ProblemOrderViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('problemOrder');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('problem-order-vivi-detail', {
            parent: 'problem-order-vivi',
            url: '/problem-order-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.problemOrder.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/problem-order/problem-order-vivi-detail.html',
                    controller: 'ProblemOrderViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('problemOrder');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'ProblemOrder', function($stateParams, ProblemOrder) {
                    return ProblemOrder.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'problem-order-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('problem-order-vivi-detail.edit', {
            parent: 'problem-order-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/problem-order/problem-order-vivi-dialog.html',
                    controller: 'ProblemOrderViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProblemOrder', function(ProblemOrder) {
                            return ProblemOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('problem-order-vivi.new', {
            parent: 'problem-order-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/problem-order/problem-order-vivi-dialog.html',
                    controller: 'ProblemOrderViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('problem-order-vivi', null, { reload: 'problem-order-vivi' });
                }, function() {
                    $state.go('problem-order-vivi');
                });
            }]
        })
        .state('problem-order-vivi.edit', {
            parent: 'problem-order-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/problem-order/problem-order-vivi-dialog.html',
                    controller: 'ProblemOrderViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['ProblemOrder', function(ProblemOrder) {
                            return ProblemOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('problem-order-vivi', null, { reload: 'problem-order-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('problem-order-vivi.delete', {
            parent: 'problem-order-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/problem-order/problem-order-vivi-delete-dialog.html',
                    controller: 'ProblemOrderViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['ProblemOrder', function(ProblemOrder) {
                            return ProblemOrder.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('problem-order-vivi', null, { reload: 'problem-order-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
