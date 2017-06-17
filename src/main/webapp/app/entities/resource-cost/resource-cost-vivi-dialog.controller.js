(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ResourceCostViviDialogController', ResourceCostViviDialogController);

    ResourceCostViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ResourceCost'];

    function ResourceCostViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ResourceCost) {
        var vm = this;

        vm.resourceCost = entity;
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
            if (vm.resourceCost.id !== null) {
                ResourceCost.update(vm.resourceCost, onSaveSuccess, onSaveError);
            } else {
                ResourceCost.save(vm.resourceCost, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:resourceCostUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
