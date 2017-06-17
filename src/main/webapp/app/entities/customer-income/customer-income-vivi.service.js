(function() {
    'use strict';
    angular
        .module('vivisysApp')
        .factory('CustomerIncome', CustomerIncome);

    CustomerIncome.$inject = ['$resource'];

    function CustomerIncome ($resource) {
        var resourceUrl =  'api/customer-incomes/:id';

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
