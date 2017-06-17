(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('SpCostViviDialogController', SpCostViviDialogController);

    SpCostViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SpCost'];

    function SpCostViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SpCost) {
        var vm = this;

        vm.spCost = entity;
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
            if (vm.spCost.id !== null) {
                SpCost.update(vm.spCost, onSaveSuccess, onSaveError);
            } else {
                SpCost.save(vm.spCost, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:spCostUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
