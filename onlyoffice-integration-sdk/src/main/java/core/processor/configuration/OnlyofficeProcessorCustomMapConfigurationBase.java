package core.processor.configuration;

public final class OnlyofficeProcessorCustomMapConfigurationBase implements OnlyofficeProcessorCustomMapConfiguration {
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
