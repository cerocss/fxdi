package com.cerocss.fxdi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static com.cerocss.fxdi.ExampleClasses.*;


/**
 * This class tests the automatic controller creation and dependency injection
 * by providing different scenarios and comparing to expected results.
 */
public class DependencyContainerInjectionTest {

    private DependencyContainer dependencyContainer;

    @BeforeEach
    void setupTests() {
        dependencyContainer = new DependencyContainer();
    }

    @Test
    void shouldCreateEmptyController() {
        Object res = dependencyContainer.getOrCreateInjectableTypes(EmptyController.class);
        Assertions.assertInstanceOf(EmptyController.class, res);
    }

    @Test
    void shouldInjectSingleModel() {
        Object res = dependencyContainer.getOrCreateInjectableTypes(SingletonController.class);
        Assertions.assertInstanceOf(SingletonController.class, res);
    }

    @Test
    void shouldInjectNestedModels() {
        Object res = dependencyContainer.getOrCreateInjectableTypes(NestedSingletonController.class);
        Assertions.assertInstanceOf(NestedSingletonController.class, res);
    }

    @Test
    void shouldInjectMultipleModels() {
        Object res = dependencyContainer.getOrCreateInjectableTypes(MultipleSingletonController.class);
        Assertions.assertInstanceOf(MultipleSingletonController.class, res);
    }

    @Test
    void shouldInjectMultipleConstructorSingleAnnotationModel() {
        Object res = dependencyContainer.getOrCreateInjectableTypes(MultipleConstructorSingleAnnotationController.class);
        Assertions.assertInstanceOf(MultipleConstructorSingleAnnotationController.class, res);
    }

    @Test
    void shouldFailAbstractController() {
        var error = Assertions.assertThrows(RuntimeException.class, () -> dependencyContainer.getOrCreateInjectableTypes(AbstractController.class));
        Assertions.assertEquals("com.cerocss.fxdi.ExampleClasses$AbstractController cannot be instantiated.", error.getMessage());
    }

    @Test
    void shouldFailPrivateConstructor() {
        var error = Assertions.assertThrows(RuntimeException.class, () -> dependencyContainer.getOrCreateInjectableTypes(NotPublicController.class));
        Assertions.assertEquals("com.cerocss.fxdi.ExampleClasses$NotPublicController has no public constructors. It cannot be instantiated.", error.getMessage());
    }

    @Test
    void shouldFailNoInjectionModel() {
        var error = Assertions.assertThrows(RuntimeException.class, () -> dependencyContainer.getOrCreateInjectableTypes(NotSingletonController.class));
        Assertions.assertEquals("com.cerocss.fxdi.ExampleClasses$NotSingletonModel is not marked as Singleton. Constructors of injected classes that are not manually registered are only allowed to contain singletons.", error.getMessage());
    }

    @Test
    void shouldFailNoCircularModel() {
        var error = Assertions.assertThrows(RuntimeException.class, () -> dependencyContainer.getOrCreateInjectableTypes(CircularController.class));
        Assertions.assertEquals("com.cerocss.fxdi.ExampleClasses$CircularModel has circular dependencies. Visited classes: [com.cerocss.fxdi.ExampleClasses$CircularController, com.cerocss.fxdi.ExampleClasses$CircularModel] already contains: com.cerocss.fxdi.ExampleClasses$CircularModel.", error.getMessage());
    }

    @Test
    void shouldFailMultipleConstructorsNoAnnotatedController() {
        var error = Assertions.assertThrows(RuntimeException.class, () -> dependencyContainer.getOrCreateInjectableTypes(MultipleConstructorNoAnnotationController.class));
        Assertions.assertEquals("com.cerocss.fxdi.ExampleClasses$MultipleConstructorNoAnnotationController has multiple public constructors. Annotate a single one with @InjectionController.", error.getMessage());
    }

    @Test
    void shouldFailMultipleConstructorsMultipleAnnotatedController() {
        var error = Assertions.assertThrows(RuntimeException.class, () -> dependencyContainer.getOrCreateInjectableTypes(MultipleConstructorMultipleAnnotationController.class));
        Assertions.assertEquals("com.cerocss.fxdi.ExampleClasses$MultipleConstructorMultipleAnnotationController has multiple public constructors annotated as InjectionController. There should be only a single public one.", error.getMessage());
    }
}

