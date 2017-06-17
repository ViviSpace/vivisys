(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ResourceViviDeleteController',ResourceViviDeleteController);

    ResourceViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'Resource'];

    function ResourceViviDeleteController($uibModalInstance, entity, Resource) {
        var vm = this;

        vm.resource = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Resource.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
