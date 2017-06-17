(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('SpDeployViviDeleteController',SpDeployViviDeleteController);

    SpDeployViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'SpDeploy'];

    function SpDeployViviDeleteController($uibModalInstance, entity, SpDeploy) {
        var vm = this;

        vm.spDeploy = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SpDeploy.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
