(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('OrdViviDialogController', OrdViviDialogController);

    OrdViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Ord', 'ResourceDeploy', 'SpDeploy', 'Serv', 'Agent', 'ServiceProvider', 'Customer'];

    function OrdViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Ord, ResourceDeploy, SpDeploy, Serv, Agent, ServiceProvider, Customer) {
        var vm = this;

        vm.ord = entity;
        vm.clear = clear;
        vm.datePickerOpenStatus = {};
        vm.openCalendar = openCalendar;
        vm.save = save;
        vm.resourcedeploys = ResourceDeploy.query();
        vm.spdeploys = SpDeploy.query();
        vm.servs = Serv.query();
        vm.agents = Agent.query();
        vm.serviceproviders = ServiceProvider.query();
        vm.customers = Customer.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.ord.id !== null) {
                Ord.update(vm.ord, onSaveSuccess, onSaveError);
            } else {
                Ord.save(vm.ord, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:ordUpdate', result);
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
