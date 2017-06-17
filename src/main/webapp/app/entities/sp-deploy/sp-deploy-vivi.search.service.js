(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('SpDeploySearch', SpDeploySearch);

    SpDeploySearch.$inject = ['$resource'];

    function SpDeploySearch($resource) {
        var resourceUrl =  'api/_search/sp-deploys/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
