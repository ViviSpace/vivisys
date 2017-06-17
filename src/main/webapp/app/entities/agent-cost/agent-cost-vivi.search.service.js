(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('AgentCostSearch', AgentCostSearch);

    AgentCostSearch.$inject = ['$resource'];

    function AgentCostSearch($resource) {
        var resourceUrl =  'api/_search/agent-costs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
