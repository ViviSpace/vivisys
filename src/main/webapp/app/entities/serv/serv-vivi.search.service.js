(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('ServSearch', ServSearch);

    ServSearch.$inject = ['$resource'];

    function ServSearch($resource) {
        var resourceUrl =  'api/_search/servs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
