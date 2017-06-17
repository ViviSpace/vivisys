(function() {
    'use strict';
    angular
        .module('vivisysApp')
        .factory('SpCost', SpCost);

    SpCost.$inject = ['$resource'];

    function SpCost ($resource) {
        var resourceUrl =  'api/sp-costs/:id';

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
