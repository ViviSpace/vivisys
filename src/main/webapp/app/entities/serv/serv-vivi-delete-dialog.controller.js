(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ServViviDeleteController',ServViviDeleteController);

    ServViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'Serv'];

    function ServViviDeleteController($uibModalInstance, entity, Serv) {
        var vm = this;

        vm.serv = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Serv.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
