(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ProblemOrderViviDetailController', ProblemOrderViviDetailController);

    ProblemOrderViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ProblemOrder', 'Problem'];

    function ProblemOrderViviDetailController($scope, $rootScope, $stateParams, previousState, entity, ProblemOrder, Problem) {
        var vm = this;

        vm.problemOrder = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:problemOrderUpdate', function(event, result) {
            vm.problemOrder = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
