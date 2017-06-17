(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ServViviDetailController', ServViviDetailController);

    ServViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Serv', 'Resource', 'Product', 'ServiceProvider', 'Agent'];

    function ServViviDetailController($scope, $rootScope, $stateParams, previousState, entity, Serv, Resource, Product, ServiceProvider, Agent) {
        var vm = this;

        vm.serv = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:servUpdate', function(event, result) {
            vm.serv = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
