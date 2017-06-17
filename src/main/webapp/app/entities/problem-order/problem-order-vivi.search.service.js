(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('ProblemOrderSearch', ProblemOrderSearch);

    ProblemOrderSearch.$inject = ['$resource'];

    function ProblemOrderSearch($resource) {
        var resourceUrl =  'api/_search/problem-orders/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
