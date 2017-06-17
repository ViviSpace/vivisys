(function() {
    'use strict';
    angular
        .module('vivisysApp')
        .factory('ServiceProvider', ServiceProvider);

    ServiceProvider.$inject = ['$resource'];

    function ServiceProvider ($resource) {
        var resourceUrl =  'api/service-providers/:id';

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
