(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ResourceCostViviDetailController', ResourceCostViviDetailController);

    ResourceCostViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ResourceCost'];

    function ResourceCostViviDetailController($scope, $rootScope, $stateParams, previousState, entity, ResourceCost) {
        var vm = this;

        vm.resourceCost = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:resourceCostUpdate', function(event, result) {
            vm.resourceCost = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
