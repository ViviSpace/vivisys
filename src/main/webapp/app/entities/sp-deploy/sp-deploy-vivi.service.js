(function() {
    'use strict';
    angular
        .module('vivisysApp')
        .factory('SpDeploy', SpDeploy);

    SpDeploy.$inject = ['$resource', 'DateUtils'];

    function SpDeploy ($resource, DateUtils) {
        var resourceUrl =  'api/sp-deploys/:id';

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
