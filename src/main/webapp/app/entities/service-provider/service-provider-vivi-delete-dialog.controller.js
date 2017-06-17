(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ServiceProviderViviDeleteController',ServiceProviderViviDeleteController);

    ServiceProviderViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'ServiceProvider'];

    function ServiceProviderViviDeleteController($uibModalInstance, entity, ServiceProvider) {
        var vm = this;

        vm.serviceProvider = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ServiceProvider.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
