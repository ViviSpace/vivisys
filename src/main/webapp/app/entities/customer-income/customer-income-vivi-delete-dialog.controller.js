(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('CustomerIncomeViviDeleteController',CustomerIncomeViviDeleteController);

    CustomerIncomeViviDeleteController.$inject = ['$uibModalInstance', 'entity', 'CustomerIncome'];

    function CustomerIncomeViviDeleteController($uibModalInstance, entity, CustomerIncome) {
        var vm = this;

        vm.customerIncome = entity;
        vm.clear = clear;
        vm.confirmDelete = confirmDelete;

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function confirmDelete (id) {
            CustomerIncome.delete({id: id},
                function () {
                    $uibModalInstance.close(true);
                });
        }
    }
})();
