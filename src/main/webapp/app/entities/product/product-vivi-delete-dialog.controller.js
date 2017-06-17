(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ProductViviDeleteController',ProductViviDeleteController);

    ProductViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'Product'];

    function ProductViviDeleteController($uibModalInstance, entity, Product) {
        var vm = this;

        vm.product = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Product.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
