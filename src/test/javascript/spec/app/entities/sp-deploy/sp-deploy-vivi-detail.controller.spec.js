'use strict';

describe('Controller Tests', function() {

    describe('SpDeploy Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockSpDeploy, MockServiceProvider, MockOrd;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockSpDeploy = jasmine.createSpy('MockSpDeploy');
            MockServiceProvider = jasmine.createSpy('MockServiceProvider');
            MockOrd = jasmine.createSpy('MockOrd');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'SpDeploy': MockSpDeploy,
                'ServiceProvider': MockServiceProvider,
                'Ord': MockOrd
            };
            createController = function() {
                $injector.get('$controller')("SpDeployViviDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'vivisysApp:spDeployUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
