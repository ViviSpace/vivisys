'use strict';

describe('Controller Tests', function() {

    describe('ResourceDeploy Management Detail Controller', function() {
        var $scope, $rootScope;
        var MockEntity, MockPreviousState, MockResourceDeploy, MockResource, MockOrd;
        var createController;

        beforeEach(inject(function($injector) {
            $rootScope = $injector.get('$rootScope');
            $scope = $rootScope.$new();
            MockEntity = jasmine.createSpy('MockEntity');
            MockPreviousState = jasmine.createSpy('MockPreviousState');
            MockResourceDeploy = jasmine.createSpy('MockResourceDeploy');
            MockResource = jasmine.createSpy('MockResource');
            MockOrd = jasmine.createSpy('MockOrd');
            

            var locals = {
                '$scope': $scope,
                '$rootScope': $rootScope,
                'entity': MockEntity,
                'previousState': MockPreviousState,
                'ResourceDeploy': MockResourceDeploy,
                'Resource': MockResource,
                'Ord': MockOrd
            };
            createController = function() {
                $injector.get('$controller')("ResourceDeployViviDetailController", locals);
            };
        }));


        describe('Root Scope Listening', function() {
            it('Unregisters root scope listener upon scope destruction', function() {
                var eventType = 'vivisysApp:resourceDeployUpdate';

                createController();
                expect($rootScope.$$listenerCount[eventType]).toEqual(1);

                $scope.$destroy();
                expect($rootScope.$$listenerCount[eventType]).toBeUndefined();
            });
        });
    });

});
