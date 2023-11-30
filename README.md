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

Example [SettingsManagerImpl](https://github.com/ONLYOFFICE/docs-integration-sdk-java/blob/master/demo-example/src/main/java/com/onlyoffice/demoexample/manager/SettingsManagerImpl.java)

DefaultDocumentManager:

* getDocumentKey(final  String fileId, final boolean embedded)
* getDocumentName(final String fileId)

Example [DocumentManagerImpl](https://github.com/ONLYOFFICE/docs-integration-sdk-java/blob/master/demo-example/src/main/java/com/onlyoffice/demoexample/manager/DocumentManagerImpl.java)

Next you need to implement UrlManager:


To open the editor in editing mode, you need to define:

* getFileUrl(final String fileId)
* getCallbackUrl(final String fileId)

Example [UrlManagerImpl](https://github.com/ONLYOFFICE/docs-integration-sdk-java/blob/master/demo-example/src/main/java/com/onlyoffice/demoexample/manager/UrlMangerImpl.java)

You also need to add dependencies to your DI container JwtManager and RequestManager:

Example [JwtManager and RequestManager](https://github.com/ONLYOFFICE/docs-integration-sdk-java/blob/master/demo-example/src/main/java/com/onlyoffice/demoexample/DemoExampleApplication.java#L38-L47)


After this, you can use all available services in their default implementation or by overriding and extending them.

Example [ConfigServiceImpl](https://github.com/ONLYOFFICE/docs-integration-sdk-java/blob/master/demo-example/src/main/java/com/onlyoffice/demoexample/service/ConfigServiceImpl.java)

Example using [ConfigService](https://github.com/ONLYOFFICE/docs-integration-sdk-java/blob/master/demo-example/src/main/java/com/onlyoffice/demoexample/controllers/MainController.java)

Example [CallbackServiceImpl](https://github.com/ONLYOFFICE/docs-integration-sdk-java/blob/master/demo-example/src/main/java/com/onlyoffice/demoexample/service/CallbackServiceImpl.java)

Example using [CallbackService](https://github.com/ONLYOFFICE/docs-integration-sdk-java/blob/master/demo-example/src/main/java/com/onlyoffice/demoexample/controllers/CallbackController.java)
