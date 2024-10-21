package com.cerocss.fxdi;

import javafx.util.Callback;

/**
 * The factory that is responsible for delegating controller creation to the dependency container.
 */
class InjectionFactory implements Callback<Class<?>, Object> {

    /**
     * The dependency container that is used to construct controller instances.
     */
    private final DependencyContainer dependencyContainer;

    /**
     * Initializes a new factory with a given dependency container.
     *
     * @param dependencyContainer the dependency container that should be used for controller creation
     */
    public InjectionFactory(DependencyContainer dependencyContainer) {
        this.dependencyContainer = dependencyContainer;
    }

    /**
     * The entrypoint for the {@link javafx.fxml.FXMLLoader} instance to construct controllers from the class in the
     * fx:controller directive in the loaded fxml file.
     *
     * @param clazz the referenced class from the fxml file
     * @return an instance of the referenced class with injected singletons
     */
    @Override
    public Object call(Class<?> clazz) {
        return dependencyContainer.getOrCreateInjectableTypes(clazz);
    }
}
