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
| `DefaultRequestManager` | No | HTTP requests to Document Server (deprecated since 1.5) |

Integrators **must** subclass `DefaultDocumentManager` (implementing `getDocumentKey` and `getDocumentName`) and `DefaultSettingsManager` (implementing `getSetting` and `setSetting`).

### Services (`com.onlyoffice.service`)

| Service | Purpose |
|---|---|
| `DefaultConfigService` | Generate `Config` object for opening the document editor |
| `DefaultCallbackService` | Process Document Server save/forcesave/editing callbacks |
| `DefaultConvertServiceV2` | Document format conversion (V2 is the current version) |
| `DefaultSettingsValidationServiceV2` | Validate Document Server connectivity |

### Client (`com.onlyoffice.client`)

`ApacheHttpclientDocumentServerClient` (Apache HttpComponents 5) implements `DocumentServerClient`:
- `healthcheck()`, `getFile()`, `convert()`, `command()`, `docbuilder()`

### Configuration and Context

`DocsIntegrationSdkConfiguration` is a factory interface where:
- `settingsManager()` and `documentManager()` are **abstract** — must be implemented
- All other components have `default` factory methods

`DocsIntegrationSdkContext` acts as an IoC container — call `build()` to wire all components. For Spring, components can also be registered as individual beans.

### Models

Located in `com.onlyoffice.model`. All are Lombok `@Builder` POJOs:
- `com.onlyoffice.model.documenteditor.config` — `Config`, `Document`, `EditorConfig`, `Permissions`, etc.
- `com.onlyoffice.model.documenteditor.callback` — `Callback` (statuses 1–7)
- `com.onlyoffice.model.convertservice` — `ConvertRequest`, `ConvertResponse`
- `com.onlyoffice.model.common` — `Format`, `User`, `Error`

Document formats are loaded from a static resource file in `DefaultDocumentManager` and cached in a static `formats` field.

## Demo Application

`demo-example/` is a Spring Boot 3 / Java 17 reference implementation showing how to wire the SDK into a web application. It includes concrete implementations of `SettingsManager`, `DocumentManager`, `UrlManager`, and both `CallbackService` and `ConfigService`.
