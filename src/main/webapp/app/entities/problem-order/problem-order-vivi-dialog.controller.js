(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ProblemOrderViviDialogController', ProblemOrderViviDialogController);

    ProblemOrderViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'ProblemOrder', 'Problem'];

    function ProblemOrderViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, ProblemOrder, Problem) {
        var vm = this;

        vm.problemOrder = entity;
        vm.clear = clear;
        vm.save = save;
        vm.problems = Problem.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.problemOrder.id !== null) {
                ProblemOrder.update(vm.problemOrder, onSaveSuccess, onSaveError);
            } else {
                ProblemOrder.save(vm.problemOrder, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:problemOrderUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
