(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ProblemOrderViviDeleteController',ProblemOrderViviDeleteController);

    ProblemOrderViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'ProblemOrder'];

    function ProblemOrderViviDeleteController($uibModalInstance, entity, ProblemOrder) {
        var vm = this;

        vm.problemOrder = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            ProblemOrder.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
