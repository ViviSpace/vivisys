(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('ResourceDeploySearch', ResourceDeploySearch);

    ResourceDeploySearch.$inject = ['$resource'];

    function ResourceDeploySearch($resource) {
        var resourceUrl =  'api/_search/resource-deploys/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
