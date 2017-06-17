(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('CustomerIncomeViviDetailController', CustomerIncomeViviDetailController);

    CustomerIncomeViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'CustomerIncome'];

    function CustomerIncomeViviDetailController($scope, $rootScope, $stateParams, previousState, entity, CustomerIncome) {
        var vm = this;

        vm.customerIncome = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:customerIncomeUpdate', function(event, result) {
            vm.customerIncome = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
