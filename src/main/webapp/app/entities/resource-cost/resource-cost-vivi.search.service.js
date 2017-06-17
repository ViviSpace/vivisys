(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('ResourceCostSearch', ResourceCostSearch);

    ResourceCostSearch.$inject = ['$resource'];

    function ResourceCostSearch($resource) {
        var resourceUrl =  'api/_search/resource-costs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
