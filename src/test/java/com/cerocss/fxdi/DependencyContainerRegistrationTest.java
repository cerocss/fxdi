package com.cerocss.fxdi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.cerocss.fxdi.ExampleClasses.NotSingletonModel;

/**
 * Tests the manual registration of instances in the {@link DependencyContainer}.
 */
public class DependencyContainerRegistrationTest {

    private DependencyContainer dependencyContainer;

    @BeforeEach
    void setupTests() {
        dependencyContainer = new DependencyContainer();
    }

    @Test
    void shouldManuallyRegisterTestClass() {
        // given
        var test = new NotSingletonModel();

        // when
        dependencyContainer.registerSingletonManually(test);
        var result = dependencyContainer.getOrCreateInjectableTypes(NotSingletonModel.class);

        // then
        Assertions.assertEquals(test, result);
    }

    @Test
    void shouldManuallyRegisterTestClassForAnotherClass() {
        // given
        var test = new NotSingletonModel();

        // when
        dependencyContainer.registerSingletonManually(DependencyContainerRegistrationTest.class, test);
        var result = dependencyContainer.getOrCreateInjectableTypes(DependencyContainerRegistrationTest.class);

        // then
        Assertions.assertEquals(test, result);
    }
}
