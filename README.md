# BzScript Java project family

Preserved work in progress, backed up November 2, 2026.

- BzScript: scripting language lexer, parser, syntax checker, evaluator, and language operations.
- BzBuilder: generates Java language and evaluator classes from .bzdata definitions and templates.
- JEagle: template processing with actions and substitutions.
- BzData: structured data definitions and parser; see BzData/docs/BzData_Spec.md.
- JListCursor: shared list cursor utility.

## Build and tests

Install a JDK supporting Java 25, as specified by the Eclipse project settings. In Eclipse, use File > Import > Existing Projects into Workspace and select these five projects. Their .classpath files preserve source folders and bundled jar dependencies. Enable project builds. Tests use Eclipse JUnit 5 support; run the tests folders with Run As > JUnit Test.

Bundled jars are intentionally retained, including Gson and local project libraries. No clean build has been verified. BzScript and BzBuilder currently have no-argument main methods; a standard command-line launcher is not established. BzBuilder uses paths relative to its project directory and writes generated source into BzScript; do not run generation merely to restore this backup.

Additional Java drafts in BzScript/ant and BzScript/Evaluator.java are preserved alongside src. The .test-syntax/RunTests.java helper is also retained.

## Backup scope

Includes source, tests, templates, .bzdata files, documentation, Eclipse project settings and bundled jars. Excludes Eclipse workspace metadata, compiled class files, bin and .jar-build output directories, local backup archives, and credential file patterns in .gitignore. The dated local ZIP additionally preserves compiled outputs. No source files were refactored to make this snapshot build.

## Crude snapshots

Run crude-backup.bat to snapshot this workspace into C:\CRUDE using the installed Crude executable. The script checks C:\CRUDE\crude.exe then C:\dev\crude\crude.exe. Crude prompts for description and direction notes; finish each with a single dot on its own line. The wrapper does not clean or delete any files. Usage is based on https://github.com/xecronix/crude/blob/HEAD/QuickStart.md. This interactive snapshot command was not run as part of the GitHub backup.
