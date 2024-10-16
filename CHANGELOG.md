# Change Log

## Added
- documenteditor/config/editorconfig/customization/Logo#imageLight
- documenteditor/config/editorconfig/customization/Features#tabStyle
- documenteditor/config/editorconfig/customization/Features#tabBackground
- common/Changesa#documentSha256
- commandservice/CommandResponse#users
- default empty file templates
- fi, he, no, sl empty file templates

## Changed
- demo server address changed
- address of the command service, /command instead /coauthoring/CommandService.ashx
- deprecated documenteditor/config/EditorConfig#location
- deprecated documenteditor/config/editorconfig/Customization#toolbarNoTabs

## 1.2.0
## Added
- DocumentType - PDF
- format repository (pdf documentType for docxf and oform, fb2 additional mime, pdf as documentType in editor)
- the formsdataurl parameter to the Callback
- the model FormData
- documenteditor/callback/ForcesaveType#SUBMIT_FORM
- editorconfig/customization/Features#roles
- editorconfig/Customization#close
- error status -9 and -10 to ConvertResponse.Error
- commands for working with forgotten files
- sr-Cyrl-RS empty file templates

## Changed
- the editorconfig/customization/Goback#requestClose field is deprecated
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
