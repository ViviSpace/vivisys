(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ProblemViviDialogController', ProblemViviDialogController);

    ProblemViviDialogController.$inject = ['$timeout', '$scope', '$stateParams', '$uibModalInstance', 'entity', 'Problem', 'ProblemOrder', 'Customer', 'Ord'];

    function ProblemViviDialogController ($timeout, $scope, $stateParams, $uibModalInstance, entity, Problem, ProblemOrder, Customer, Ord) {
        var vm = this;

        vm.problem = entity;
        vm.clear = clear;
        vm.save = save;
        vm.problemorders = ProblemOrder.query();
        vm.customers = Customer.query();
        vm.ords = Ord.query();

        $timeout(function (){
            angular.element('.form-group:eq(1)>input').focus();
        });

        function clear () {
            $uibModalInstance.dismiss('cancel');
        }

        function save () {
            vm.isSaving = true;
            if (vm.problem.id !== null) {
                Problem.update(vm.problem, onSaveSuccess, onSaveError);
            } else {
                Problem.save(vm.problem, onSaveSuccess, onSaveError);
            }
        }

        function onSaveSuccess (result) {
            $scope.$emit('vivisysApp:problemUpdate', result);
            $uibModalInstance.close(result);
            vm.isSaving = false;
        }

        function onSaveError () {
            vm.isSaving = false;
        }


    }
})();
