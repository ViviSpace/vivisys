(function() {
    'use strict';
    angular
        .module('vivisysApp')
        .factory('Ord', Ord);

    Ord.$inject = ['$resource', 'DateUtils'];

    function Ord ($resource, DateUtils) {
        var resourceUrl =  'api/ords/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.createdTime = DateUtils.convertDateTimeFromServer(data.createdTime);
                        data.effictiveTime = DateUtils.convertDateTimeFromServer(data.effictiveTime);
                        data.expriedTime = DateUtils.convertDateTimeFromServer(data.expriedTime);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
