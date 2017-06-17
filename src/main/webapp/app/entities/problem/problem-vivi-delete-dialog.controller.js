(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ProblemViviDeleteController',ProblemViviDeleteController);

    ProblemViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'Problem'];

    function ProblemViviDeleteController($uibModalInstance, entity, Problem) {
        var vm = this;

        vm.problem = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            Problem.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
