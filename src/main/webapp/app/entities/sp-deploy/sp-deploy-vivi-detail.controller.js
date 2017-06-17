(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('SpDeployViviDetailController', SpDeployViviDetailController);

    SpDeployViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'SpDeploy', 'ServiceProvider', 'Ord'];

    function SpDeployViviDetailController($scope, $rootScope, $stateParams, previousState, entity, SpDeploy, ServiceProvider, Ord) {
        var vm = this;

        vm.spDeploy = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:spDeployUpdate', function(event, result) {
            vm.spDeploy = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
