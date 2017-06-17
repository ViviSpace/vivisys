'use strict';

describe('Controller Tests', function() {

    describe('Problem Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockProblem, MockProblemOrder, MockCustomer, MockOrd;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockProblem = jasmine.createSpy('MockProblem');
            MockProblemOrder = jasmine.createSpy('MockProblemOrder');
            MockCustomer = jasmine.createSpy('MockCustomer');
            MockOrd = jasmine.createSpy('MockOrd');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Problem': MockProblem,
                'ProblemOrder': MockProblemOrder,
                'Customer': MockCustomer,
                'Ord': MockOrd
            };
            createController = function() {
                $injector.get('$controller')("ProblemViviDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'vivisysApp:problemUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
