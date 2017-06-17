(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ServViviDialogController', ServViviDialogController);

    ServViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Serv', 'Resource', 'Product', 'ServiceProvider', 'Agent'];

    function ServViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Serv, Resource, Product, ServiceProvider, Agent) {
        var vm = this;

        vm.serv = entity;
        vm.clear = clear;
        vm.save = save;
        vm.resources = Resource.query();
        vm.products = Product.query();
        vm.serviceproviders = ServiceProvider.query();
        vm.agents = Agent.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.serv.id !== null) {
                Serv.update(vm.serv, onSaveSuccess, onSaveError);
            } else {
                Serv.save(vm.serv, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:servUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
