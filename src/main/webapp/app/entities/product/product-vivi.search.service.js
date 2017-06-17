(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('ProductSearch', ProductSearch);

    ProductSearch.$inject = ['$resource'];

    function ProductSearch($resource) {
        var resourceUrl =  'api/_search/products/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
