# ONLYOFFICE Docs Integration Java SDK

ONLYOFFICE Docs Integration Java SDK provides common interfaces and default implementations for integrating ONLYOFFICE Document Server into your own website or application on Java.

## Prerequisites
* **Java**: version 8 and higher (download from the [Oracle official website](https://www.oracle.com/java/technologies/downloads/#java8));
* **Apache Maven** (download from the [official website](https://maven.apache.org/download.cgi)).

## Building from source

The lists of supported formats and empty file templates are included as git submodules, so clone the repository with them:

```bash
git clone --recursive <repository-url>
# or, in an already cloned repository
git submodule update --init --recursive

mvn clean package
```

## SDK structure
SDK consists of 4 main managers, 4 services and a client for the Document Server.

### Managers

| Manager                       | Description                                                             | Default implementation           |
| ----------------------------- | ----------------------------------------------------------------------- | -------------------------------- |
| [DocumentManager](/src/main/java/com/onlyoffice/manager/document/DocumentManager.java)| This manager is used for working with files and document formats.| [DefaultDocumentManager](/src/main/java/com/onlyoffice/manager/document/DefaultDocumentManager.java)  (abstract)|
| [JwtManager](/src/main/java/com/onlyoffice/manager/security/JwtManager.java) | This manager is used for generating and verifying authorization tokens. | [DefaultJwtManager](/src/main/java/com/onlyoffice/manager/security/DefaultJwtManager.java)|
| [SettingsManager](/src/main/java/com/onlyoffice/manager/settings/SettingsManager.java)| This manager is used to manage integration application settings.| [DefaultSettingsManager](/src/main/java/com/onlyoffice/manager/settings/DefaultSettingsManager.java)  (abstract)|
| [UrlManager](/src/main/java/com/onlyoffice/manager/url/UrlManager.java) | This manager is used as a URL provider.| [DefaultUrlManager](/src/main/java/com/onlyoffice/manager/url/DefaultUrlManager.java)|

### Services

| Service                       | Description                                                                   | Default implementation           |
| ----------------------------- | ----------------------------------------------------------------------------- | -------------------------------- |
| [ConfigService](/src/main/java/com/onlyoffice/service/documenteditor/config/ConfigService.java) | This configuration generation service is used for opening the document editor.| [DefaultConfigService](/src/main/java/com/onlyoffice/service/documenteditor/config/DefaultConfigService.java)|
| [CallbackService](/src/main/java/com/onlyoffice/service/documenteditor/callback/CallbackService.java)| This service is used for processing the response of the Document Server.| [DefaultCallbackService](/src/main/java/com/onlyoffice/service/documenteditor/callback/DefaultCallbackService.java)|
| [ConvertService](/src/main/java/com/onlyoffice/service/convert/ConvertService.java)| This service is used for converting documents.| [DefaultConvertServiceV2](/src/main/java/com/onlyoffice/service/convert/DefaultConvertServiceV2.java)|
| [SettingsValidationService](/src/main/java/com/onlyoffice/service/settings/SettingsValidationService.java)| This service is used for checking the Document Server connection settings.| [DefaultSettingsValidationServiceV2](/src/main/java/com/onlyoffice/service/settings/DefaultSettingsValidationServiceV2.java)  |

### Document Server client

[DocumentServerClient](/src/main/java/com/onlyoffice/client/DocumentServerClient.java) is used to send requests to the ONLYOFFICE Document Server: health check, file download, conversion, command service, document builder, and the Document Server configuration (`/meta/config`) and supported formats (`/meta/formats`). The default implementation is [ApacheHttpclientDocumentServerClient](/src/main/java/com/onlyoffice/client/ApacheHttpclientDocumentServerClient.java), based on Apache HttpClient 5.

The authorization tokens of the requests to the Document Server carry the `operation` claim ([JwtOperation](/src/main/java/com/onlyoffice/model/security/JwtOperation.java)), and `JwtManager` can refuse a token issued for another operation.

### Formats providers

`DefaultDocumentManager` gets the list of supported formats from a [FormatsProvider](/src/main/java/com/onlyoffice/provider/FormatsProvider.java):

| Provider | Description |
| -------- | ----------- |
| [DefaultFormatsProvider](/src/main/java/com/onlyoffice/provider/DefaultFormatsProvider.java) | The formats bundled with the SDK (used by default). |
| [DocumentServerFormatsProvider](/src/main/java/com/onlyoffice/provider/DocumentServerFormatsProvider.java) | The formats of the connected Document Server, cached in [DocumentServerMetaCache](/src/main/java/com/onlyoffice/cache/DocumentServerMetaCache.java). The bundled formats are returned while none are loaded. |

To use another provider, pass it to the `DefaultDocumentManager(SettingsManager, FormatsProvider)` constructor.

### Configuration and context

[DocsIntegrationSdkConfiguration](/src/main/java/com/onlyoffice/configuration/DocsIntegrationSdkConfiguration.java) describes how all SDK components are created. Only `settingsManager()` and `documentManager()` must be implemented, the other components have default implementations that can be overridden.

[DocsIntegrationSdkContext](/src/main/java/com/onlyoffice/context/DocsIntegrationSdkContext.java) creates and wires all components using the configuration and provides access to them (`getConfigService()`, `getCallbackService()`, `getUrlManager()`, etc.).

## Usage

Let's look at the [demo example](/demo-example), which shows how the SDK works in a Spring Boot web application:

1. Implement the methods of the abstract **DefaultSettingsManager** class:
    ```java
    @Override
    public String getSetting(final String name) {
        return properties.getProperty(name);
    }

    @Override
    public void setSetting(final String name, final String value) {
        properties.setProperty(name, value);
    }
    ```
    The full example code can be found [here](/demo-example/src/main/java/com/onlyoffice/demoexample/manager/SettingsManagerImpl.java).

2. Implement the methods of the abstract **DefaultDocumentManager** class:
    ```java
    @Override
    public String getDocumentKey(final String fileId, final boolean embedded) {
        return String.valueOf(fileId.hashCode());
    }

    @Override
    public String getDocumentName(final String fileId) {
        return "sample.docx";
    }
    ```
    The full example code can be found [here](/demo-example/src/main/java/com/onlyoffice/demoexample/manager/DocumentManagerImpl.java).

3. Implement the **UrlManager** methods. To open the editor in the editing mode, you need to define:
    ```java
    @Override
    public String getFileUrl(final String fileId) {
        return getServerUrl() + "/file/download";
    }

    @Override
    public String getCallbackUrl(final String fileId) {
        return getServerUrl() + "/callback";
    }
    ```
    The full example code can be found [here](/demo-example/src/main/java/com/onlyoffice/demoexample/manager/UrlMangerImpl.java).

4. Implement **DocsIntegrationSdkConfiguration** to return your implementations of the managers and services, and register **DocsIntegrationSdkContext** as a Spring bean. This allows you to get all SDK components anywhere in the application:
    ```java
    @Configuration
    public class DemoDocsIntegrationSdkConfiguration implements DocsIntegrationSdkConfiguration {

        @Override
        public SettingsManager settingsManager() {
            return new SettingsManagerImpl(this.docServerUrl, this.securityKey);
        }

        @Override
        public DocumentManager documentManager(final SettingsManager settingsManager) {
            return new DocumentManagerImpl(settingsManager);
        }

        @Override
        public UrlManager urlManager(final SettingsManager settingsManager) {
            return new UrlMangerImpl(settingsManager);
        }

        @Bean
        DocsIntegrationSdkContext docsIntegrationSdkContext() {
            return new DocsIntegrationSdkContext(this);
        }
    }
    ```
    The full example code can be found [here](/demo-example/src/main/java/com/onlyoffice/demoexample/configuration/DemoDocsIntegrationSdkConfiguration.java).

5. Once ready, you can use all available services in their default implementations or by overriding and extending them:
    * To use the [callback service](/demo-example/src/main/java/com/onlyoffice/demoexample/service/CallbackServiceImpl.java), create the [callback controller](/demo-example/src/main/java/com/onlyoffice/demoexample/controllers/CallbackController.java) where the request body is wrapped in the *Callback* model and the callback service is called to verify and handle this model.
    * To demonstrate the [config service](/demo-example/src/main/java/com/onlyoffice/demoexample/service/ConfigServiceImpl.java) work, *@GetMapping("/editor")* is used in the [main controller](/demo-example/src/main/java/com/onlyoffice/demoexample/controllers/MainController.java). When the user opens this address, the editor page is requested. At the same time, the config service is called and generates the *Config* model to open the editor:
    ```java
    @Autowired
    private DocsIntegrationSdkContext docsIntegrationSdk;

    @GetMapping("/editor")
    public String main(final Model model) throws JsonProcessingException {
        ConfigService configService = docsIntegrationSdk.getConfigService();
        UrlManager urlManager = docsIntegrationSdk.getUrlManager();

        Config config = configService.createConfig("1", Mode.EDIT, Type.DESKTOP);

        model.addAttribute("config", config);
        model.addAttribute("documentServerApiUrl", urlManager.getDocumentServerApiUrl());

        return "editor";
    }
    ```

6. Create the [file controller](/demo-example/src/main/java/com/onlyoffice/demoexample/controllers/FileController.java) to get a file for opening in the editor. When the config service generates the config model, the URL for downloading a file is specified in this model. To open the document, the editor sends a request to this URL, and the file controller responds to this request and returns the document.
