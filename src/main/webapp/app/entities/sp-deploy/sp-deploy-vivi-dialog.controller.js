(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('SpDeployViviDialogController', SpDeployViviDialogController);

    SpDeployViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'SpDeploy', 'ServiceProvider', 'Ord'];

    function SpDeployViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, SpDeploy, ServiceProvider, Ord) {
        var vm = this;

        vm.spDeploy = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.serviceproviders = ServiceProvider.query();
        vm.ords = Ord.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.spDeploy.id !== null) {
                SpDeploy.update(vm.spDeploy, onSaveSuccess, onSaveError);
            } else {
                SpDeploy.save(vm.spDeploy, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:spDeployUpdate', result);
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
