(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('AgentCostViviDeleteController',AgentCostViviDeleteController);

    AgentCostViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'AgentCost'];

    function AgentCostViviDeleteController($uibModalInstance, entity, AgentCost) {
        var vm = this;

        vm.agentCost = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            AgentCost.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
