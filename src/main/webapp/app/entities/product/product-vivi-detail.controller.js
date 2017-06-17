(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ProductViviDetailController', ProductViviDetailController);

    ProductViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Product'];

    function ProductViviDetailController($scope, $rootScope, $stateParams, previousState, entity, Product) {
        var vm = this;

        vm.product = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:productUpdate', function(event, result) {
            vm.product = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
