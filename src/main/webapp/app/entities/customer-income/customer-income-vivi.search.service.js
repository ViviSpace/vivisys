(function() {
    'use strict';

    angular
        .module('vivisysApp')
        .factory('CustomerIncomeSearch', CustomerIncomeSearch);

    CustomerIncomeSearch.$inject = ['$resource'];

    function CustomerIncomeSearch($resource) {
        var resourceUrl =  'api/_search/customer-incomes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
