(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ResourceCostViviDeleteController',ResourceCostViviDeleteController);

    ResourceCostViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'ResourceCost'];

    function ResourceCostViviDeleteController($uibModalInstance, entity, ResourceCost) {
        var vm = this;

        vm.resourceCost = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ResourceCost.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
