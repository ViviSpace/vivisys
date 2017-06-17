(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ProblemViviDetailController', ProblemViviDetailController);

    ProblemViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Problem', 'ProblemOrder', 'Customer', 'Ord'];

    function ProblemViviDetailController($scope, $rootScope, $stateParams, previousState, entity, Problem, ProblemOrder, Customer, Ord) {
        var vm = this;

        vm.problem = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:problemUpdate', function(event, result) {
            vm.problem = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
