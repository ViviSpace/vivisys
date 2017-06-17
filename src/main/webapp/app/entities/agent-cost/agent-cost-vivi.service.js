(function() {
    'use strict';
    angular
        .module('vivisysApp')
        .factory('AgentCost', AgentCost);

    AgentCost.$inject = ['$resource'];

    function AgentCost ($resource) {
        var resourceUrl =  'api/agent-costs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
