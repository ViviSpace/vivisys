(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('AgentViviDialogController', AgentViviDialogController);

    AgentViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Agent', 'Serv'];

    function AgentViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Agent, Serv) {
        var vm = this;

        vm.agent = entity;
        vm.clear = clear;
        vm.save = save;
        vm.servs = Serv.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.agent.id !== null) {
                Agent.update(vm.agent, onSaveSuccess, onSaveError);
            } else {
                Agent.save(vm.agent, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:agentUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
