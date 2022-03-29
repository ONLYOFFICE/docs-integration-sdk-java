package core.processor;

public interface OnlyofficePostProcessor<T> {

    /**
     *
     * @throws RuntimeException
     */
    default void processAfter() throws RuntimeException {}

    /**
     *
     * @param obj
     * @throws RuntimeException
     */
    default void processAfter(T obj) throws RuntimeException {}
}
