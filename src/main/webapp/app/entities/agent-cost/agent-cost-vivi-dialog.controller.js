(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('AgentCostViviDialogController', AgentCostViviDialogController);

    AgentCostViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'AgentCost'];

    function AgentCostViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, AgentCost) {
        var vm = this;

        vm.agentCost = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.agentCost.id !== null) {
                AgentCost.update(vm.agentCost, onSaveSuccess, onSaveError);
            } else {
                AgentCost.save(vm.agentCost, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:agentCostUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
