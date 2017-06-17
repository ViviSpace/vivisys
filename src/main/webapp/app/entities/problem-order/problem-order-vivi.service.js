(function() {
    'use strict';
    angular
        .module('vivisysApp')
        .factory('ProblemOrder', ProblemOrder);

    ProblemOrder.$inject = ['$resource'];

    function ProblemOrder ($resource) {
        var resourceUrl =  'api/problem-orders/:id';

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
