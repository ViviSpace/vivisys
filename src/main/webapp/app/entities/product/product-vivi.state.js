(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .config(stateConfig);

    stateConfig.$inject = ['$stateProvider'];

    function stateConfig($stateProvider) {
        $stateProvider
        .state('product-vivi', {
            parent: 'entity',
            url: '/product-vivi',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.product.home.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product/productsVivi.html',
                    controller: 'ProductViviController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('product');
                    $translatePartialLoader.addPart('global');
                    return $translate.refresh();
                }]
            }
        })
        .state('product-vivi-detail', {
            parent: 'product-vivi',
            url: '/product-vivi/{id}',
            data: {
                authorities: ['ROLE_USER'],
                pageTitle: 'vivisysApp.product.detail.title'
            },
            views: {
                'content@': {
                    templateUrl: 'app/entities/product/product-vivi-detail.html',
                    controller: 'ProductViviDetailController',
                    controllerAs: 'vm'
                }
            },
            resolve: {
                translatePartialLoader: ['$translate', '$translatePartialLoader', function ($translate, $translatePartialLoader) {
                    $translatePartialLoader.addPart('product');
                    return $translate.refresh();
                }],
                entity: ['$stateParams', 'Product', function($stateParams, Product) {
                    return Product.get({id : $stateParams.id}).$promise;
                }],
                previousState: ["$state", function ($state) {
                    var currentStateData = {
                        name: $state.current.name || 'product-vivi',
                        params: $state.params,
                        url: $state.href($state.current.name, $state.params)
                    };
                    return currentStateData;
                }]
            }
        })
        .state('product-vivi-detail.edit', {
            parent: 'product-vivi-detail',
            url: '/detail/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/product-vivi-dialog.html',
                    controller: 'ProductViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Product', function(Product) {
                            return Product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('^', {}, { reload: false });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-vivi.new', {
            parent: 'product-vivi',
            url: '/new',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/product-vivi-dialog.html',
                    controller: 'ProductViviDialogController',
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
                    $state.go('product-vivi', null, { reload: 'product-vivi' });
                }, function() {
                    $state.go('product-vivi');
                });
            }]
        })
        .state('product-vivi.edit', {
            parent: 'product-vivi',
            url: '/{id}/edit',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/product-vivi-dialog.html',
                    controller: 'ProductViviDialogController',
                    controllerAs: 'vm',
                    backdrop: 'static',
                    size: 'lg',
                    resolve: {
                        entity: ['Product', function(Product) {
                            return Product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-vivi', null, { reload: 'product-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        })
        .state('product-vivi.delete', {
            parent: 'product-vivi',
            url: '/{id}/delete',
            data: {
                authorities: ['ROLE_USER']
            },
            onEnter: ['$stateParams', '$state', '$uibModal', function($stateParams, $state, $uibModal) {
                $uibModal.open({
                    templateUrl: 'app/entities/product/product-vivi-delete-dialog.html',
                    controller: 'ProductViviDeleteController',
                    controllerAs: 'vm',
                    size: 'md',
                    resolve: {
                        entity: ['Product', function(Product) {
                            return Product.get({id : $stateParams.id}).$promise;
                        }]
                    }
                }).result.then(function() {
                    $state.go('product-vivi', null, { reload: 'product-vivi' });
                }, function() {
                    $state.go('^');
                });
            }]
        });
    }

})();
