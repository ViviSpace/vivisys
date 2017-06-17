(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('SpCostViviDetailController', SpCostViviDetailController);

    SpCostViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SpCost'];

    function SpCostViviDetailController($scope, $rootScope, $stateParams, previousState, entity, SpCost) {
        var vm = this;

        vm.spCost = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:spCostUpdate', function(event, result) {
            vm.spCost = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
