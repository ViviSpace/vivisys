(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('OrdSearch', OrdSearch);

    OrdSearch.$inject = ['$resource'];

    function OrdSearch($resource) {
        var resourceUrl =  'api/_search/ords/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
