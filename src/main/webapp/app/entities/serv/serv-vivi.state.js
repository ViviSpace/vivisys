(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('serv-vivi', {
            parent: 'entity',
            url: '/serv-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.serv.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/serv/servsVivi.html',
                    controller: 'ServViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serv');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('serv-vivi-detail', {
            parent: 'serv-vivi',
            url: '/serv-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.serv.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/serv/serv-vivi-detail.html',
                    controller: 'ServViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('serv');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Serv', function($stateParams, Serv) {
                    return Serv.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'serv-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('serv-vivi-detail.edit', {
            parent: 'serv-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serv/serv-vivi-dialog.html',
                    controller: 'ServViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Serv', function(Serv) {
                            return Serv.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('serv-vivi.new', {
            parent: 'serv-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serv/serv-vivi-dialog.html',
                    controller: 'ServViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: function () {
                            return {
                                name: null,
                                descriptin: null,
                                price: null,
                                unit: null,
                                type: null,
                                status: null,
                                id: null
                            };
                        }
                    }
                }).result.then(function() {
                    $state.go('serv-vivi', null, { reload: 'serv-vivi' });
                }, function() {
                    $state.go('serv-vivi');
                });
            }]
        })
        .state('serv-vivi.edit', {
            parent: 'serv-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serv/serv-vivi-dialog.html',
                    controller: 'ServViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Serv', function(Serv) {
                            return Serv.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('serv-vivi', null, { reload: 'serv-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('serv-vivi.delete', {
            parent: 'serv-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/serv/serv-vivi-delete-dialog.html',
                    controller: 'ServViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Serv', function(Serv) {
                            return Serv.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('serv-vivi', null, { reload: 'serv-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
