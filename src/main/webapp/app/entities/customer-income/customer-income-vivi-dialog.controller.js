(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('CustomerIncomeViviDialogController', CustomerIncomeViviDialogController);

    CustomerIncomeViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'CustomerIncome'];

    function CustomerIncomeViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, CustomerIncome) {
        var vm = this;

        vm.customerIncome = entity;
        vm.clear = clear;
        vm.save = save;

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.customerIncome.id !== null) {
                CustomerIncome.update(vm.customerIncome, onSaveSuccess, onSaveError);
            } else {
                CustomerIncome.save(vm.customerIncome, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:customerIncomeUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
