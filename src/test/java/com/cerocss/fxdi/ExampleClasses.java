package com.cerocss.fxdi;

/**
 * These classes are used for testing purposes by providing different injection scenarios. PMD warnings are ignored.
 */
public abstract class ExampleClasses {

    public static final String FIELD_CAN_BE_LOCAL = "FieldCanBeLocal";

    /**
     * Example controller that does not contain any dependencies.
     */
    public static class EmptyController {
        public EmptyController() {
        }
    }

    /**
     * Example {@link Singleton} model.
     */
    @Singleton
    public static class SingletonModel {
        public SingletonModel() {
        }
    }

    /**
     * Example controller containing a {@link Singleton} dependency.
     */
    public static class SingletonController {
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        final SingletonModel singletonModel;

        public SingletonController(SingletonModel singletonModel) {
            this.singletonModel = singletonModel;
        }
    }

    /**
     * Example nested {@link Singleton} model.
     */
    @Singleton
    public static class NestedSingletonModel {
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        final SingletonModel singletonModel;

        public NestedSingletonModel(SingletonModel singletonModel) {
            this.singletonModel = singletonModel;
        }
    }

    /**
     * Example controller containing a nested {@link Singleton} dependency.
     */
    public static class NestedSingletonController {
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        final NestedSingletonModel nestedSingletonModel;

        public NestedSingletonController(NestedSingletonModel nestedSingletonModel) {
            this.nestedSingletonModel = nestedSingletonModel;
        }
    }

    /**
     * Example controller containing a {@link Singleton} and a nested {@link Singleton} dependency.
     */
    public static class MultipleSingletonController {
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        final SingletonModel singletonModel;
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        final NestedSingletonModel nestedSingletonModel;

        public MultipleSingletonController(SingletonModel singletonModel, NestedSingletonModel nestedSingletonModel) {
            this.singletonModel = singletonModel;
            this.nestedSingletonModel = nestedSingletonModel;
        }
    }

    /**
     * Example controller containing an {@link InjectionConstructor}.
     */
    public static class MultipleConstructorSingleAnnotationController {
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        private SingletonModel singletonModel;

        public MultipleConstructorSingleAnnotationController() {
        }

        @InjectionConstructor
        public MultipleConstructorSingleAnnotationController(SingletonModel singletonModel) {
            this.singletonModel = singletonModel;
        }
    }

    /**
     * An abstract example controller.
     */
    public static abstract class AbstractController {
        public AbstractController() {
        }
    }

    /**
     * Example controller without public constructor.
     */
    public static class NotPublicController {
        private NotPublicController() {
        }
    }

    /**
     * Example model without {@link Singleton} annotation.
     */
    public static class NotSingletonModel {
        public NotSingletonModel() {
        }
    }

    /**
     * Example controller containing a dependency without {@link Singleton} annotation.
     */
    public static class NotSingletonController {
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        final NotSingletonModel notSingletonModel;

        public NotSingletonController(NotSingletonModel notSingletonModel) {
            this.notSingletonModel = notSingletonModel;
        }
    }

    /**
     * Example model containing circular dependencies.
     */
    @Singleton
    public static class CircularModel {
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        final CircularModel recursiveModel;

        public CircularModel(CircularModel circularModel) {
            this.recursiveModel = circularModel;
        }
    }

    /**
     * Example controller with a dependency containing circular dependencies.
     */
    public static class CircularController {
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        final CircularModel circularModel;

        public CircularController(CircularModel circularModel) {
            this.circularModel = circularModel;
        }
    }

    /**
     * Example controller with multiple constructors and no {@link InjectionConstructor} annotations.
     */
    public static class MultipleConstructorNoAnnotationController {
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        private SingletonModel singletonModel;

        public MultipleConstructorNoAnnotationController() {
        }

        public MultipleConstructorNoAnnotationController(SingletonModel singletonModel) {
            this.singletonModel = singletonModel;
        }
    }

    /**
     * Example controller with multiple constructors and multiple {@link InjectionConstructor} annotations.
     */
    public static class MultipleConstructorMultipleAnnotationController {
        @SuppressWarnings(FIELD_CAN_BE_LOCAL)
        private SingletonModel singletonModel;

        @InjectionConstructor
        public MultipleConstructorMultipleAnnotationController() {
        }

        @InjectionConstructor
        public MultipleConstructorMultipleAnnotationController(SingletonModel singletonModel) {
            this.singletonModel = singletonModel;
        }
    }
}


