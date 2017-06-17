'use strict';

describe('Controller Tests', function() {

    describe('Ord Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockOrd, MockResourceDeploy, MockSpDeploy, MockServ, MockAgent, MockServiceProvider, MockCustomer;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockOrd = jasmine.createSpy('MockOrd');
            MockResourceDeploy = jasmine.createSpy('MockResourceDeploy');
            MockSpDeploy = jasmine.createSpy('MockSpDeploy');
            MockServ = jasmine.createSpy('MockServ');
            MockAgent = jasmine.createSpy('MockAgent');
            MockServiceProvider = jasmine.createSpy('MockServiceProvider');
            MockCustomer = jasmine.createSpy('MockCustomer');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'Ord': MockOrd,
                'ResourceDeploy': MockResourceDeploy,
                'SpDeploy': MockSpDeploy,
                'Serv': MockServ,
                'Agent': MockAgent,
                'ServiceProvider': MockServiceProvider,
                'Customer': MockCustomer
            };
            createController = function() {
                $injector.get('$controller')("OrdViviDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'vivisysApp:ordUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
