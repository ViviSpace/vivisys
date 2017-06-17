(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('OrdViviDetailController', OrdViviDetailController);

    OrdViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Ord', 'ResourceDeploy', 'SpDeploy', 'Serv', 'Agent', 'ServiceProvider', 'Customer'];

    function OrdViviDetailController($scope, $rootScope, $stateParams, previousState, entity, Ord, ResourceDeploy, SpDeploy, Serv, Agent, ServiceProvider, Customer) {
        var vm = this;

        vm.ord = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:ordUpdate', function(event, result) {
            vm.ord = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
