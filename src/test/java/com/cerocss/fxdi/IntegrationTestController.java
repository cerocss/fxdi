package com.cerocss.fxdi;

public class IntegrationTestController {
    final ExampleClasses.SingletonModel singletonModel;
    final ExampleClasses.NestedSingletonModel nestedSingletonModel;

    @InjectionConstructor
    public IntegrationTestController(ExampleClasses.SingletonModel singletonModel, ExampleClasses.NestedSingletonModel nestedSingletonModel) {
        this.singletonModel = singletonModel;
        this.nestedSingletonModel = nestedSingletonModel;
    }

    public IntegrationTestController(ExampleClasses.SingletonModel singletonModel, ExampleClasses.NestedSingletonModel nestedSingletonModel, String ignored) {
        this.singletonModel = singletonModel;
        this.nestedSingletonModel = nestedSingletonModel;
    }
}
