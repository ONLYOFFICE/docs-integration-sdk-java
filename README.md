# ONLYOFFICE Docs Integration Java SDK

ONLYOFFICE Docs Integration Java SDK provides common interfaces and default implementations for integrating ONLYOFFICE Document Server into your own website or application on Java.

## Prerequisites
* **Java**: version 8 and higher (download from the [Oracle official website](https://www.oracle.com/java/technologies/downloads/#java8));
* **Apache Maven** (download from the [official website](https://maven.apache.org/download.cgi)).

## SDK structure 
SDK consists of 5 main managers and 4 services.

### Managers

| Manager                       | Description                                                             | Default implementation           |
| ----------------------------- | ----------------------------------------------------------------------- | -------------------------------- |
| [DocumentManager](/src/main/java/com/onlyoffice/manager/document/DocumentManager.java)| This manager is used for working with files and document formats.| [DefaultDocumentManager](/src/main/java/com/onlyoffice/manager/document/DefaultDocumentManager.java)  (abstract)|
| [RequestManager](/src/main/java/com/onlyoffice/manager/request/RequestManager.java)| This manager is used to make requests to the ONLYOFFICE Document Server.| [DefaultRequestManager](/src/main/java/com/onlyoffice/manager/request/DefaultRequestManager.java)|
| [JwtManager](/src/main/java/com/onlyoffice/manager/security/JwtManager.java) | This manager is used for generating and verifying authorization tokens. | [DefaultJwtManager](/src/main/java/com/onlyoffice/manager/security/DefaultJwtManager.java)|
| [SettingsManager](/src/main/java/com/onlyoffice/manager/settings/SettingsManager.java)| This manager is used to manage integration application settings.| [DefaultSettingsManager](/src/main/java/com/onlyoffice/manager/settings/DefaultSettingsManager.java)  (abstract)|
| [UrlManager](/src/main/java/com/onlyoffice/manager/url/UrlManager.java) | This manager is used as a URL provider.| [DefaultUrlManager](/src/main/java/com/onlyoffice/manager/url/DefaultUrlManager.java)|

### Services

| Service                       | Description                                                                   | Default implementation           |
| ----------------------------- | ----------------------------------------------------------------------------- | -------------------------------- |
| [ConfigService](/src/main/java/com/onlyoffice/service/documenteditor/config/ConfigService.java) | This configuration generation service is used for opening the document editor.| [DefaultConfigService](/src/main/java/com/onlyoffice/service/documenteditor/config/DefaultConfigService.java)|
| [CallbackService](/src/main/java/com/onlyoffice/service/documenteditor/callback/CallbackService.java)| This service is used for processing the response of the Document Server.| [DefaultCallbackService](/src/main/java/com/onlyoffice/service/documenteditor/callback/DefaultCallbackService.java)|
| [ConvertService](/src/main/java/com/onlyoffice/service/convert/ConvertService.java)| This service is used for converting documents.| [DefaultConvertService](/src/main/java/com/onlyoffice/service/convert/DefaultConvertService.java)|
| [SettingsValidationService](/src/main/java/com/onlyoffice/service/settings/SettingsValidationService.java)| This service is used for checking the Document Server connection settings.| [DefaultSettingsValidationService](/src/main/java/com/onlyoffice/service/settings/DefaultSettingsValidationService.java)  |


## Usage

Let's look at the [demo example](/demo-example), which shows how the SDK works in a classic Spring web application:

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
    public String getDocumentKey(final  String fileId, final boolean embedded) {
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

4. Initialize the **JwtManager** and **RequestManager** Spring beans and add them to the DI container. This allows you to call the registered beans anywhere in the application:
    ```java
    @Bean
    public JwtManager jwtManager(final SettingsManager settingsManager) {
        return new DefaultJwtManager(settingsManager);
    }

    @Bean
    public RequestManager requestManager(final UrlManager urlManager, final JwtManager jwtManager, final SettingsManager settingsManager) {
        return new DefaultRequestManager(urlManager, jwtManager, settingsManager);
    }
    ```
    The full example code can be found [here](/demo-example/src/main/java/com/onlyoffice/demoexample/DemoExampleApplication.java).

5. Once ready, you can use all available services in their default implementations or by overriding and extending them:
    * To use the [callback service](/demo-example/src/main/java/com/onlyoffice/demoexample/service/CallbackServiceImpl.java), create the [callback controller](/demo-example/src/main/java/com/onlyoffice/demoexample/controllers/CallbackController.java) where the request body is wrapped in the *Callback* model and the callback service is called to handle this model.
    * To demonstrate the [config service](/demo-example/src/main/java/com/onlyoffice/demoexample/service/ConfigServiceImpl.java) work, *@GetMapping("/editor")* is used in the [main controller](/demo-example/src/main/java/com/onlyoffice/demoexample/controllers/MainController.java). When the user opens this address, the editor page is requested. At the same time, the config service is called and generates the *Config* model to open the editor:
    ```java
    @GetMapping("/editor")
    public String main(final Model model) throws JsonProcessingException {

        Config config = configService.createConfig("1", Mode.EDIT, Type.DESKTOP);

        model.addAttribute("config", config);
        model.addAttribute("documentServerApiUrl", urlManager.getDocumentServerApiUrl());

        return "editor";
    }
    ```

6. Create the [file controller](/demo-example/src/main/java/com/onlyoffice/demoexample/controllers/FileController.java) to get a file for opening in the editor. When the config service generates the config model, the URL for downloading a file is specified in this model. To open the document, the editor sends a request to this URL, and the file controller responds to this request and returns the document.
