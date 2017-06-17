(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('OrdViviDeleteController',OrdViviDeleteController);

    OrdViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'Ord'];

    function OrdViviDeleteController($uibModalInstance, entity, Ord) {
        var vm = this;

        vm.ord = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Ord.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
