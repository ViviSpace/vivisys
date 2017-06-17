(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('ProblemSearch', ProblemSearch);

    ProblemSearch.$inject = ['$resource'];

    function ProblemSearch($resource) {
        var resourceUrl =  'api/_search/problems/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
