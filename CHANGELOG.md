# Change Log

## Added
- documenteditor/config/editorconfig/Customization#mobile
- documenteditor/config/editorconfig/Customization#pointerMode
- documenteditor/config/editorconfig/Customization#slidePlayerBackground
- documenteditor/config/editorconfig/Customization#wordHeadingsColor
- support hwp, hwpx, pages, numbers, key formats
- sq-AL empty file templates

## Changed
- documenteditor/config/editorconfig/Customization#submitForm type boolean or SubmitForm
- deprecated documenteditor/config/editorconfig/Customization#mobileForceView

## 1.3.0
## Added
- documenteditor/config/editorconfig/customization/Logo#imageLight
- documenteditor/config/editorconfig/customization/Features#tabStyle
- documenteditor/config/editorconfig/customization/Features#tabBackground
- commandservice/CommandResponse#users
- default empty file templates
- fi, he, no, sl empty file templates
- shardkey parameter
- manager/document/DocumentManager#isForm(InputStream inputStream)
- the list of image formats available for insertion into the editor has been expanded (tiff)
- support java 8

## Changed
- demo server address changed
- address of the command service, /command instead /coauthoring/CommandService.ashx
- address of the convert service, /converter instead /ConvertService.ashx
- deprecated documenteditor/config/EditorConfig#location
- deprecated documenteditor/config/editorconfig/Customization#toolbarNoTabs
- apache httpclient 5
- default token lifetime is 5 minutes
- delete model common/Changes

## 1.2.1
## Changed
- ignore unknown properties

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
