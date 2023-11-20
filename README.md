# ONLYOFFICE Docs Integration SDK

SDK for integrating ONLYOFFICE Document Server into your own website or application on Java


## SDK structure 
SDK consists of 5 main managers and 4 services.

**Managers:**
* DocumentManager - for working with files and document formats.
* RequestManager - to make requests to the ONLYOFFICE document server.
* JwtManager - for generating and verifying authorization tokens.
* SettingsManager - to manage integration application settings.
* UrlManager - URL provider.

**Default implementations:**
* DefaultDocumentManager (abstract)
* DefaultRequestManager
* DefaultJwtManager
* DefaultSettingsManager (abstract)
* DefaultUrlManager


**Services:**
* ConfigService - configuration generation service for opening the document editor.
* CallbackService - service for processing the response of the document server.
* ConvertService - service for converting documents.
* SettingsValidationService - service for checking document server connection settings.
  
**Default implementation:**
* DefaultConfigService
* DefaultCallbackService
* DefaultConvertService
* DefaultSettingsValidationService

## Usage

The first step is to implement the methods of the abstract classes DefaultSettingsManager and DefaultDocumentManager.

DefaultSettingsManager:

* getSetting(final String name)
* setSetting(final String name, final String value)

Example
```
public class SettingsManagerImpl extends DefaultSettingsManager {
    private static final String SETTINGS_PREFIX = "onlyoffice.";

    private final PluginSettings pluginSettings;

    public SettingsManagerImpl(final PluginSettingsFactory pluginSettingsFactory) {
        this.pluginSettings = pluginSettingsFactory.createGlobalSettings();
    }

    @Override
    public String getSetting(final String name) {
        return (String) pluginSettings.get(SETTINGS_PREFIX + name);
    }

    @Override
    public void setSetting(final String name, final String value) {
         pluginSettings.put(SETTINGS_PREFIX + name, value);
    }
}
```

DefaultDocumentManager:

* getSetting(final String name)
* setSetting(final String name, final String value)

Example
```
public class DocumentManagerImpl extends DefaultDocumentManager {
    private final SettingsManager settingsManager;

    public DocumentManagerImpl(final SettingsManager settingsManager) {
        super(settingsManager);
    }

    @Override
    public String getDocumentKey(final String fileId, final boolean embedded) {
        String key = fileId;

        return embedded ? key + "_embedded" : key;
    }

    @Override
    public String getDocumentName(final String fileId) {
       FileEntity file = FileManager.getFile(fileId);
    
        return file.getName();
    }
```