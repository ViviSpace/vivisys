(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ResourceViviDetailController', ResourceViviDetailController);

    ResourceViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Resource', 'Serv'];

    function ResourceViviDetailController($scope, $rootScope, $stateParams, previousState, entity, Resource, Serv) {
        var vm = this;

        vm.resource = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:resourceUpdate', function(event, result) {
            vm.resource = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
