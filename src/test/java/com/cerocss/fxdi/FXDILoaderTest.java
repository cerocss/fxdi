package com.cerocss.fxdi;

import javafx.fxml.FXMLLoader;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

import static org.mockito.Mockito.*;

/**
 * Tests the creation and loading of {@link FXMLLoader}s though the custom {@link FXDILoader}.
 */
public class FXDILoaderTest {

    private final FXDILoader fxdiLoader = new FXDILoader();
    private final Object result = new Object();

    @Test
    void shouldConstructLoaderWithResourceBundle() {
        try (var resourceBundleMock = mockStatic(ResourceBundle.class)) {
            resourceBundleMock.when(() -> ResourceBundle.getBundle(any())).thenReturn(new MockTranslationBundle());

            Assertions.assertDoesNotThrow(() -> new FXDILoader(Locale.ENGLISH));
        }
    }

    @Test
    void shouldDelegateLoadingToFxmlLoader() throws IOException {
        try (var mock = mockConstruction(FXMLLoader.class, (fxmlLoader, context) -> when(fxmlLoader.load()).thenReturn(result))) {
            // given
            var url = FXDILoaderTest.class.getResource("notActuallyUsed");

            // when
            var res = fxdiLoader.load(url);

            // then
            var constructedMock = mock.constructed().getFirst();
            Assertions.assertEquals(result, res);
            Assertions.assertEquals(1, mock.constructed().size());
            verify(constructedMock, times(1)).load();
            Assertions.assertEquals(url, constructedMock.getLocation());
        }
    }

    @Test
    void shouldLoadUsingFXMLLoaderInstance() throws IOException {
        try (var mock = mockConstruction(FXMLLoader.class, (fxmlLoader, context) -> when(fxmlLoader.load()).thenReturn(result))) {
            // given
            var url = FXDILoaderTest.class.getResource("notActuallyUsed");

            // when
            var fxmlLoader = fxdiLoader.getFXMLLoaderInstance(url);
            var res = fxmlLoader.load();


            // then
            Assertions.assertEquals(url, fxmlLoader.getLocation());
            var constructedMock = mock.constructed().getFirst();
            Assertions.assertEquals(result, res);
            Assertions.assertEquals(1, mock.constructed().size());
            verify(constructedMock, times(1)).load();
            Assertions.assertEquals(url, constructedMock.getLocation());
        }
    }

    @Test
    void shouldPropagateException() throws IOException {
        try (var mock = mockConstruction(FXMLLoader.class, (fxmlLoader, context) -> when(fxmlLoader.load()).thenThrow(new IOException("test")))) {
            // given
            var url = FXDILoaderTest.class.getResource("notActuallyUsed");

            // when / then
            Assertions.assertThrows(RuntimeException.class, () -> fxdiLoader.load(url), "test");

            var constructedMock = mock.constructed().getFirst();
            Assertions.assertEquals(1, mock.constructed().size());
            verify(constructedMock, times(1)).load();
            Assertions.assertEquals(url, constructedMock.getLocation());
        }
    }

    @Test
    void shouldReturnPrefilledDependencyContainer() {
        // when
        var res = fxdiLoader.getDependencyContainer();

        // then
        Assertions.assertInstanceOf(DependencyContainer.class, res.getOrCreateInjectableTypes(DependencyContainer.class));
        Assertions.assertInstanceOf(FXDILoader.class, res.getOrCreateInjectableTypes(FXDILoader.class));
    }
}
