(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('SpCostSearch', SpCostSearch);

    SpCostSearch.$inject = ['$resource'];

    function SpCostSearch($resource) {
        var resourceUrl =  'api/_search/sp-costs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
