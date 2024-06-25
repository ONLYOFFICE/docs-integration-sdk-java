# Change Log

##
## Added
- error status -9 and -10 to ConvertResponse.Error
- commands for working with forgotten files
- sr-Cyrl-RS empty file templates

## Changed
- pdf instead docxf

## 1.1.2
## Changed
- fixed load properties in ConfigurationUtils
- improving DefaultConfigService

## 1.1.1
## Changed
- default conversion format (from docxf to pdf instead oform)

## 1.1.0
## Added
- updated supported formats (filling pdf, remove filling for oform)
- updated model Config for Document Editor

## 1.0.0
## Changed
- list supported formats
- improved work with Settings
- UrlManager (added method replaceToDocumentServerUrl)
- ar and sr empty file templates

## 0.0.1
## Added
- common interfaces (DocumentManager, RequestManager, JwtManager, SettingsManager, UrlManager, ConfigService,
CallbackService, ConvertService, SettingsValidationService)
- default implementations (DefaultDocumentManager, DefaultRequestManager, DefaultJwtManager, DefaultSettingsManager,
DefaultUrlManager, DefaultConfigService, DefaultCallbackService, DefaultConvertService,
DefaultSettingsValidationService)
