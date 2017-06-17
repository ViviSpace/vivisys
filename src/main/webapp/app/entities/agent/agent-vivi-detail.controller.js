(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .controller('AgentViviDetailController', AgentViviDetailController);

    AgentViviDetailController.$inject = ['$scope', '$rootScope', '$stateParams', 'previousState', 'entity', 'Agent', 'Serv'];

    function AgentViviDetailController($scope, $rootScope, $stateParams, previousState, entity, Agent, Serv) {
        var vm = this;

        vm.agent = entity;
        vm.previousState = previousState.name;

        var unsubscribe = $rootScope.$on('vivisysApp:agentUpdate', function(event, result) {
            vm.agent = result;
        });
        $scope.$on('$destroy', unsubscribe);
    }
})();
