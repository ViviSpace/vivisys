(function() {
    'use strict';
    angular
        .module('vivisysApp')
        .factory('Problem', Problem);

    Problem.$inject = ['$resource'];

    function Problem ($resource) {
        var resourceUrl =  'api/problems/:id';

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
