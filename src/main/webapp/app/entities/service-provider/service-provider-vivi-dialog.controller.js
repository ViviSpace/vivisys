(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ServiceProviderViviDialogController', ServiceProviderViviDialogController);

    ServiceProviderViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ServiceProvider', 'Serv'];

    function ServiceProviderViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ServiceProvider, Serv) {
        var vm = this;

        vm.serviceProvider = entity;
        vm.clear = clear;
        vm.save = save;
        vm.servs = Serv.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serviceProvider.id !== null) {
                ServiceProvider.update(vm.serviceProvider, onSaveSuccess, onSaveError);
            } else {
                ServiceProvider.save(vm.serviceProvider, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:serviceProviderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
