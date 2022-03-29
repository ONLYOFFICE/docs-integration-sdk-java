package core.processor.configuration;

public class OnlyofficeProcessorCustomMapConfigurationBase implements OnlyofficeProcessorCustomMapConfiguration {
    public String getBeforeMapKey() {
        return "beforeMap";
    }

    public String getAfterMapKey() {
        return "afterMap";
    }

    public String getSecretKey() {
        return "secret";
    }

    public String getTokenKey() {
        return "token";
    }

    public String getAutoFillerKey() {
        return "autoFiller";
    }
}
