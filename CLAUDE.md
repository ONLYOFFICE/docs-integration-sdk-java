# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Build and Test Commands

```bash
# Build
mvn clean compile
mvn clean package -DskipTests

# Run all tests
mvn test

# Run a single test class
mvn test -Dtest=DocumentManagerTest

# Run Checkstyle linting
mvn checkstyle:check
```

Checkstyle runs automatically during the build and fails on violations.

## Code Style Requirements

The project enforces Sun coding conventions via Checkstyle with these key constraints:

- **Max line length**: 120 characters
- **No tabs** — use spaces only
- **All Java files must begin with the Apache 2.0 license header** (see `onlyoffice.header`)
- **Javadoc required** on all public methods, types, and variables
- **No wildcard imports**
- **All method parameters must be `final`**
- **Every package must have a `package-info.java`**
- **Files must end with a newline**
- Inline suppression: `// CHECKSTYLE:OFF` ... `// CHECKSTYLE:ON`

## Architecture Overview

This is a **single-module library** (`com.onlyoffice:docs-integration-sdk`) that integrates ONLYOFFICE Document Server into Java applications.

### Layer Structure

```
Configuration (DocsIntegrationSdkConfiguration interface)
    └── Context (DocsIntegrationSdkContext — wires all components)
         ├── Managers (low-level operations)
         └── Services (high-level business logic)
              └── Client (HTTP to Document Server)
```

### Managers (`com.onlyoffice.manager`)

| Manager | Abstract? | Purpose |
|---|---|---|
| `DefaultDocumentManager` | Yes | File format detection, document type, conversion lists |
| `DefaultSettingsManager` | Yes | Read/write application settings |
| `DefaultJwtManager` | No | JWT token creation and verification |
| `DefaultUrlManager` | No | URL construction |

Integrators **must** subclass `DefaultDocumentManager` (implementing `getDocumentKey` and `getDocumentName`) and `DefaultSettingsManager` (implementing `getSetting` and `setSetting`).

`RequestManager`/`DefaultRequestManager`, `CommandService`, `DefaultConvertService` and `DefaultSettingsValidationService` were removed in 1.10.0 — HTTP calls to Document Server go through `DocumentServerClient`.

### Services (`com.onlyoffice.service`)

| Service | Purpose |
|---|---|
| `DefaultConfigService` | Generate `Config` object for opening the document editor |
| `DefaultCallbackService` | Process Document Server save/forcesave/editing callbacks |
| `DefaultConvertServiceV2` | Document format conversion (V2 is the current version) |
| `DefaultSettingsValidationServiceV2` | Validate Document Server connectivity |

### Client (`com.onlyoffice.client`)

`ApacheHttpclientDocumentServerClient` (Apache HttpComponents 5, extends `AbstractDocumentServerClient`) implements `DocumentServerClient`:
- `healthcheck()`, `getFile()`, `convert()`, `command()`, `docbuilder()`, `applySettings()`
- `getConfig()` (`/meta/config`) and `getFormats()` (`/meta/formats`) — `default` methods in the interface that throw `UnsupportedOperationException`, so custom clients stay source-compatible

Tokens of the requests to Document Server carry the JWT `operation` claim (`JwtOperation`: `converter`, `command`, `docbuilder`, `download`, `callback`). `JwtManager#verify(token, operation)` rejects a token issued for another operation with `IncorrectOperationException`; a token without the claim is accepted. `DefaultCallbackService#verifyCallback` expects `callback`.

### Formats (`com.onlyoffice.provider`, `com.onlyoffice.cache`)

`DefaultDocumentManager` gets its formats from a `FormatsProvider` (constructor `DefaultDocumentManager(SettingsManager, FormatsProvider)`):
- `DefaultFormatsProvider` (default) — formats bundled with the SDK (`assets/document-formats/onlyoffice-docs-formats.json`), loaded once into a static field
- `DocumentServerFormatsProvider` — formats from the Document Server (`/meta/formats`), kept in a `DocumentServerMetaCache`; reloaded when the Document Server address changes, falls back to another provider (bundled formats by default) while nothing is loaded

`DocumentServerMetaCache<T>` is a thread-safe in-memory cache of one Document Server answer, with TTL, retry delay after a failure, load timeout and invalidation; `get()` propagates errors of the loader.

### Configuration and Context

`DocsIntegrationSdkConfiguration` is a factory interface where:
- `settingsManager()` and `documentManager()` are **abstract** — must be implemented
- All other components have `default` factory methods

`DocsIntegrationSdkContext` acts as an IoC container — its constructor calls `build()` to wire all components, which are then available through getters (`getConfigService()`, `getUrlManager()`, ...). For Spring, components can also be registered as individual beans.

### Models

Located in `com.onlyoffice.model`. All are Lombok `@Builder` POJOs:
- `com.onlyoffice.model.documenteditor` — `Config`, `Callback` (statuses 1–7), `History`, `HistoryData`, `FormData`
- `com.onlyoffice.model.documenteditor.config` — `Document`, `EditorConfig` and their subpackages (`document.Permissions`, `editorconfig.Customization`, ...)
- `com.onlyoffice.model.convertservice` — `ConvertRequest`, `ConvertResponse`
- `com.onlyoffice.model.commandservice`, `com.onlyoffice.model.docbuilderservice` — command and docbuilder requests/responses
- `com.onlyoffice.model.common` — `Format`, `User`, `Error`

## Resources (git submodules)

`src/main/resources/assets/document-formats` (supported formats) and `src/main/resources/assets/document-templates` (empty file templates) are git submodules. After cloning run `git submodule update --init --recursive`, otherwise formats and templates are missing.

## Changelog

Every user-visible change goes into `CHANGELOG.md` under the upcoming version (`Added` / `Changed` / `Removed`).

## Demo Application

`demo-example/` is a Spring Boot 3 / Java 17 reference implementation showing how to wire the SDK into a web application. It implements `DocsIntegrationSdkConfiguration` (`DemoDocsIntegrationSdkConfiguration`), exposes `DocsIntegrationSdkContext` as a Spring bean and includes concrete implementations of `SettingsManager`, `DocumentManager`, `UrlManager`, and both `CallbackService` and `ConfigService`.
