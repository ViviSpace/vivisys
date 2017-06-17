(function() {
    'use strict';
    angular
        .module('vivisysApp')
        .factory('ResourceCost', ResourceCost);

    ResourceCost.$inject = ['$resource'];

    function ResourceCost ($resource) {
        var resourceUrl =  'api/resource-costs/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
