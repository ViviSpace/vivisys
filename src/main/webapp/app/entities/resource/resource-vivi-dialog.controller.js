(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ResourceViviDialogController', ResourceViviDialogController);

    ResourceViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Resource', 'Serv'];

    function ResourceViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Resource, Serv) {
        var vm = this;

        vm.resource = entity;
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
            if (vm.resource.id !== null) {
                Resource.update(vm.resource, onSaveSuccess, onSaveError);
            } else {
                Resource.save(vm.resource, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:resourceUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
