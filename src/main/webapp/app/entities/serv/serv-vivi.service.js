(function() {
    'use strict';
    angular
        .module('vivisysApp')
        .factory('Serv', Serv);

    Serv.$inject = ['$resource'];

    function Serv ($resource) {
        var resourceUrl =  'api/servs/:id';

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
