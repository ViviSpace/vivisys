'use strict';

describe('Controller Tests', function() {

    describe('Serv Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockServ, MockResource, MockProduct, MockServiceProvider, MockAgent;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockServ = jasmine.createSpy('MockServ');
            MockResource = jasmine.createSpy('MockResource');
            MockProduct = jasmine.createSpy('MockProduct');
            MockServiceProvider = jasmine.createSpy('MockServiceProvider');
            MockAgent = jasmine.createSpy('MockAgent');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Serv': MockServ,
                'Resource': MockResource,
                'Product': MockProduct,
                'ServiceProvider': MockServiceProvider,
                'Agent': MockAgent
            };
            createController = function() {
                $injector.get('$controller')("ServViviDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'vivisysApp:servUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
