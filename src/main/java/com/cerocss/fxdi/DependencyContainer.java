package com.cerocss.fxdi;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * Container that creates and keeps references to singletons of injectable classes.
 */
public class DependencyContainer {

    /**
     * Registration map of created singletons. Entries are associated with their respective class, so that only one
     * instance of a class is registered.
     */
    private final Map<Class<?>, Object> injectableObjects = new HashMap<>();

    /**
     * Constructs a new {@link DependencyContainer}
     */
    public DependencyContainer() {
    }

    /**
     * Automatically create the respective class injecting singletons models, which are created if they did not exist before.
     * Singleton creation is propagated to all children of the respective classes. All created singleton instances are
     * registered after creation for subsequent uses to share state across created instances.
     *
     * @param type Type of object to instantiate
     * @return Instance of the requested type
     */
    public Object getOrCreateInjectableTypes(Class<?> type) {
        var order = new Stack<Class<?>>();
        order.add(type);
        addConstructorParameters(type, order, new LinkedHashSet<>());

        Object object = null;
        while (!order.isEmpty()) {
            var clazz = order.pop();
            object = Optional.ofNullable(injectableObjects.get(clazz)).orElseGet(() -> instantiateInjectedClass(getInjectionConstructor(clazz)));
            if (clazz != type) {
                injectableObjects.put(clazz, object);
            }
        }
        return object;
    }

    /**
     * Manually register a singleton instance of a class. This registration does not require the class of the instance
     * to be annotated as a {@link Singleton}.
     *
     * @param singleton object to register
     */
    public void registerSingletonManually(Object singleton) {
        registerSingletonManually(singleton.getClass(), singleton);
    }

    /**
     * Manually register a singleton instance of a class with a custom key. This registration does not require the class
     * of the instance to be annotated as a {@link Singleton}.
     *
     * @param clazz     key to be used for registering
     * @param singleton object to register
     */
    public void registerSingletonManually(Class<?> clazz, Object singleton) {
        injectableObjects.put(clazz, singleton);
    }

    /**
     * Recursively construct the dependency instantiation order of a specific class. The order is determined
     * by the order of constructor parameters. Only classes annotated with {@link Singleton} are allowed to appear as
     * constructor parameters. In addition to that, circular dependencies are checked to prohibit impossible object
     * creation.
     *
     * @param type         type to check parameters for
     * @param order        order of classes that need to be instantiated (the level order search of the dependency tree)
     * @param visitedNodes already visited classes on this branch of the dependency tree
     */
    private void addConstructorParameters(Class<?> type, Stack<Class<?>> order, Set<Class<?>> visitedNodes) {
        if (injectableObjects.containsKey(type)) {
            return;
        }

        if (visitedNodes.contains(type)) {
            throw new RuntimeException(type.getName() + " has circular dependencies. Visited classes: " + visitedNodes.stream().map(Class::getName).toList() + " already contains: " + type.getName() + ".");
        }

        if (!visitedNodes.isEmpty() && !type.isAnnotationPresent(Singleton.class)) {
            throw new RuntimeException(type.getName() + " is not marked as Singleton. Constructors of injected classes that are not manually registered are only allowed to contain singletons.");
        }

        var constructor = getInjectionConstructor(type);

        var nodes = new LinkedHashSet<>(visitedNodes);
        nodes.add(type);

        if (constructor.getParameterTypes().length > 0) {
            Collections.addAll(order, constructor.getParameterTypes());
            Arrays.stream(constructor.getParameterTypes()).forEach(clazz -> addConstructorParameters(clazz, order, nodes));
        }
    }

    /**
     * Helper method to figure out the constructor to be used for instantiation of objects. The constructor is
     * determined by the amount of public and {@link InjectionConstructor} annotated constructors. Classes without
     * public constructors cannot be instantiated by the {@link DependencyContainer}. If there are multiple public
     * constructors, the one that is annotated with {@link InjectionConstructor} is used. Classes with multiple
     * annotated public constructors or multiple not annotated constructors also cannot be instantiated by the
     * {@link DependencyContainer}.
     *
     * @param type type to find the appropriate constructor for
     * @return selected constructor of the requested class
     */
    private Constructor<?> getInjectionConstructor(Class<?> type) {
        return switch (type.getConstructors().length) {
            case 0 ->
                    throw new RuntimeException(type.getName() + " has no public constructors. It cannot be instantiated.");
            case 1 -> type.getConstructors()[0];
            default -> {
                var injectionControllers = Arrays.stream(type.getConstructors()).filter(constructor -> constructor.isAnnotationPresent(InjectionConstructor.class)).toList();
                yield switch (injectionControllers.size()) {
                    case 0 ->
                            throw new RuntimeException(type.getName() + " has multiple public constructors. Annotate a single one with @InjectionController.");
                    case 1 -> injectionControllers.getFirst();
                    default ->
                            throw new RuntimeException(type.getName() + " has multiple public constructors annotated as InjectionController. There should be only a single public one.");
                };
            }
        };
    }

    /**
     * Helper method to create the instance for a given constructor injecting already instantiated singletons.
     *
     * @param constructor constructor to use for instantiation
     * @return instance, that was created using the constructor
     */
    private Object instantiateInjectedClass(Constructor<?> constructor) {
        Object instance;
        try {
            instance = constructor.newInstance(Arrays.stream(constructor.getParameterTypes()).map(injectableObjects::get).toArray());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(constructor.getDeclaringClass().getName() + " cannot be instantiated.", e);
        }
        return instance;
    }
}
