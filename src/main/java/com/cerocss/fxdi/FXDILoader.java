package com.cerocss.fxdi;

import javafx.fxml.FXMLLoader;

import java.io.IOException;
import java.net.URL;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * A custom FXMLLoader with dependency injection functionalities. Should be used as a replacement to FXMLLoader. It
 * uses JavaFX custom controller callbacks to delegate controller construction to the {@link InjectionFactory}. It also
 * provides the used {@link DependencyContainer} registering itself and the container as singletons.
 */
public class FXDILoader {

    /**
     * The injection factory that is used for controller construction.
     */
    private final InjectionFactory injectionFactory;

    /**
     * The dependency container instance that is used for this specific {@link FXDILoader}.
     */
    private final DependencyContainer dependencyContainer;

    private ResourceBundle resourceBundle;

    /**
     * Constructs a new {@link FXDILoader}.
     */
    public FXDILoader() {
        dependencyContainer = new DependencyContainer();
        injectionFactory = new InjectionFactory(dependencyContainer);
        dependencyContainer.registerSingletonManually(dependencyContainer);
        dependencyContainer.registerSingletonManually(this);
    }

    /**
     * Constructs a new {@link FXDILoader} using a preloaded resource bundle.
     *
     * @param locale the locale to use for translation
     */
    public FXDILoader(Locale locale) {
        dependencyContainer = new DependencyContainer();
        injectionFactory = new InjectionFactory(dependencyContainer);
        dependencyContainer.registerSingletonManually(dependencyContainer);
        dependencyContainer.registerSingletonManually(this);
        loadLanguage(locale);
    }

    /**
     * {@link FXMLLoader} equivalent to load fxml files using the dependency injection. Internally this uses the
     * functionality of the {@link FXMLLoader}, by constructing a new {@link FXMLLoader} every time this method is
     * called, injecting the same injection factory for every instance to share singletons across loading multiple fxml files.
     *
     * @param url the url of the fxml file to load
     * @param <T> type of fxml parent to return
     * @return the loaded fxml parent
     */
    public <T> T load(URL url) {
        try {
            return new FXMLLoader(url, resourceBundle, null, injectionFactory).load();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Create {@link FXMLLoader} instance that is used to load files.
     *
     * @param url the url of the fxml file to load
     * @return the preconfigured {@link FXMLLoader}
     */
    public FXMLLoader getFXMLLoaderInstance(URL url) {
        return new FXMLLoader(url, resourceBundle, null, injectionFactory);
    }

    /**
     * Load a new language to be used for loading new fxml files.
     *
     * @param locale the locale to use for translations
     */
    public final void loadLanguage(Locale locale) {
        resourceBundle = ResourceBundle.getBundle("messages", locale);
    }

    /**
     * Getter for the used dependency container, can be used to be able to register singletons without being in an
     * injection context if the callee has access to the {@link FXDILoader} instance.
     *
     * @return the used dependency container for this {@link FXDILoader} instance
     */
    public DependencyContainer getDependencyContainer() {
        return dependencyContainer;
    }
}
