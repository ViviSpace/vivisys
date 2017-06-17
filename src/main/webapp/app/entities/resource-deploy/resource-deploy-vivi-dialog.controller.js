(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ResourceDeployViviDialogController', ResourceDeployViviDialogController);

    ResourceDeployViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ResourceDeploy', 'Resource', 'Ord'];

    function ResourceDeployViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ResourceDeploy, Resource, Ord) {
        var vm = this;

        vm.resourceDeploy = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.resources = Resource.query();
        vm.ords = Ord.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.resourceDeploy.id !== null) {
                ResourceDeploy.update(vm.resourceDeploy, onSaveSuccess, onSaveError);
            } else {
                ResourceDeploy.save(vm.resourceDeploy, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:resourceDeployUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }

        vm.datePickerOpenStatus.createdTime = false;
        vm.datePickerOpenStatus.effictiveTime = false;
        vm.datePickerOpenStatus.expriedTime = false;

        function openCalendar (date) {
            vm.datePickerOpenStatus[date] = true;
        }
    }
})();
