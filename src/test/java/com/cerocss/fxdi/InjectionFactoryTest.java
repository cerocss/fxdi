package com.cerocss.fxdi;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.*;

/**
 * Tests the JavaFX Controller Callback functionality of the {@link InjectionFactory}.
 */
public class InjectionFactoryTest {

    private final DependencyContainer dependencyContainer = mock(DependencyContainer.class);

    private final InjectionFactory injectionFactory = new InjectionFactory(dependencyContainer);

    private final Object result = new Object();

    @Test
    void shouldCallDependencyContainerCreationMethod() {
        when(dependencyContainer.getOrCreateInjectableTypes(any())).thenReturn(result);

        var res = injectionFactory.call(InjectionFactoryTest.class);

        Assertions.assertEquals(result, res);
        verify(dependencyContainer, times(1)).getOrCreateInjectableTypes(InjectionFactoryTest.class);
    }
}
