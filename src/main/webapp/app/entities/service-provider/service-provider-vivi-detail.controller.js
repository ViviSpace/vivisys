(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ServiceProviderViviDetailController', ServiceProviderViviDetailController);

    ServiceProviderViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ServiceProvider', 'Serv'];

    function ServiceProviderViviDetailController($scope, $rootScope, $stateParams, previousState, entity, ServiceProvider, Serv) {
        var vm = this;

        vm.serviceProvider = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:serviceProviderUpdate', function(event, result) {
            vm.serviceProvider = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
