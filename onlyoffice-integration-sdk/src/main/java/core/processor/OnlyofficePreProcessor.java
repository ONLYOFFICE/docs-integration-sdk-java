package core.processor;

public interface OnlyofficePreProcessor<T> {
    /**
     *
     * @throws RuntimeException
     */
    default void processBefore() throws RuntimeException {};

    /**
     *
     * @param obj
     * @throws RuntimeException
     */
    default void processBefore(T obj) throws RuntimeException {};
}
