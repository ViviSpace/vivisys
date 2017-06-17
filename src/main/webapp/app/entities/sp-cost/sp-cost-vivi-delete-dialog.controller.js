(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('SpCostViviDeleteController',SpCostViviDeleteController);

    SpCostViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'SpCost'];

    function SpCostViviDeleteController($uibModalInstance, entity, SpCost) {
        var vm = this;

        vm.spCost = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            SpCost.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
