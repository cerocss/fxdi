package com.cerocss.fxdi;

import java.util.Enumeration;
import java.util.ResourceBundle;

/**
 * A mocked translation bundle that provides mock translations.
 */
public class MockTranslationBundle extends ResourceBundle {

    /**
     * Returns a mock translation.
     *
     * @param key the key for the desired object
     * @return a mock translation containing the key
     */
    @Override
    protected Object handleGetObject(String key) {
        return "translation(" + key + ")";
    }

    /**
     * Necessary method (for extending {@link ResourceBundle}) that is not implemented and should not be used.
     *
     * @return nothing
     * @throws UnsupportedOperationException always throws
     */
    @Override
    public Enumeration<String> getKeys() {
        throw new UnsupportedOperationException();
    }
}
