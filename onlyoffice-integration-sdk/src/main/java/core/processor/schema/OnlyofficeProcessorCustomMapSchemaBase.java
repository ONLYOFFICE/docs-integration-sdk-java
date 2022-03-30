package core.processor.schema;

public final class OnlyofficeProcessorCustomMapSchemaBase implements OnlyofficeProcessorCustomMapSchema {
    /**
     *
     * @return
     */
    public String getSecretKey() {
        return "secret";
    }

    /**
     * 
     * @return
     */
    public String getTokenKey() {
        return "token";
    }
}
