(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('ResourceDeployViviDetailController', ResourceDeployViviDetailController);

    ResourceDeployViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'ResourceDeploy', 'Resource', 'Ord'];

    function ResourceDeployViviDetailController($scope, $rootScope, $stateParams, previousState, entity, ResourceDeploy, Resource, Ord) {
        var vm = this;

        vm.resourceDeploy = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:resourceDeployUpdate', function(event, result) {
            vm.resourceDeploy = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
