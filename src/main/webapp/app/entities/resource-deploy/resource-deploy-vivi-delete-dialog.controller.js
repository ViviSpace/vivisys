(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ResourceDeployViviDeleteController',ResourceDeployViviDeleteController);

    ResourceDeployViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'ResourceDeploy'];

    function ResourceDeployViviDeleteController($uibModalInstance, entity, ResourceDeploy) {
        var vm = this;

        vm.resourceDeploy = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ResourceDeploy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
