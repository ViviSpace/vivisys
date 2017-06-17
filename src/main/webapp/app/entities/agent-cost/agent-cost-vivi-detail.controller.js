(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('AgentCostViviDetailController', AgentCostViviDetailController);

    AgentCostViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'AgentCost'];

    function AgentCostViviDetailController($scope, $rootScope, $stateParams, previousState, entity, AgentCost) {
        var vm = this;

        vm.agentCost = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:agentCostUpdate', function(event, result) {
            vm.agentCost = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
