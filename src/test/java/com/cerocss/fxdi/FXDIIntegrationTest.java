package com.cerocss.fxdi;

import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.testfx.framework.junit5.ApplicationExtension;
import org.testfx.framework.junit5.Start;

import java.io.IOException;
import java.util.Locale;

@ExtendWith(ApplicationExtension.class)
public class FXDIIntegrationTest {

    private FXDILoader fxdiLoader;
    private FXMLLoader fxmlLoader;

    @Start
    private void start(Stage rootStage) throws IOException {
        fxdiLoader = new FXDILoader(Locale.ENGLISH);
        fxmlLoader = fxdiLoader.getFXMLLoaderInstance(getClass().getResource("integration-test.fxml"));
        VBox vBox = fxmlLoader.load();
        rootStage.setScene(new Scene(vBox));
        rootStage.show();
    }

    @Test
    void shouldCreateController() {
        IntegrationTestController controller = fxmlLoader.getController();

        Assertions.assertNotNull(controller);
        Assertions.assertNotNull(controller.singletonModel);
        Assertions.assertNotNull(controller.nestedSingletonModel);
        Assertions.assertNotNull(controller.nestedSingletonModel.singletonModel);

        Assertions.assertEquals(controller.singletonModel, controller.nestedSingletonModel.singletonModel);

        Assertions.assertEquals(fxdiLoader.getDependencyContainer().getOrCreateInjectableTypes(ExampleClasses.SingletonModel.class), controller.singletonModel);
        Assertions.assertEquals(fxdiLoader.getDependencyContainer().getOrCreateInjectableTypes(ExampleClasses.SingletonModel.class), controller.nestedSingletonModel.singletonModel);
        Assertions.assertEquals(fxdiLoader.getDependencyContainer().getOrCreateInjectableTypes(ExampleClasses.NestedSingletonModel.class), controller.nestedSingletonModel);
    }
}
