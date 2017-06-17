(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('AgentViviDeleteController',AgentViviDeleteController);

    AgentViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'Agent'];

    function AgentViviDeleteController($uibModalInstance, entity, Agent) {
        var vm = this;

        vm.agent = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Agent.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
