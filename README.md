<h1 align="center">
Sample Test Automation<br>Java + Selenium + POM + TestNG + BDD-Cucumber + RestAssured + SQL
</h1>

<br>

<p align="center"><i>
A sample healthcare test automation project using Selenium for UI, API and End-to-End testing, with SQL Server integration for database validations.<br><br>
This project demonstrates automated UI and API tests for a healthcare platform with user authentication, role-based access, and management of patients and drugs.
</i></p>

<br>

## Demos

### Headed Testing on Google Chrome
![Demo 1.1 (Fast)](demos/Demo1.1_Fast_orig.gif) <br><br>

### Visual Studio Code: behind-logic example, structure and running tests from command line
![Demo 2.1 (Fast)](demos/Demo2.1_Fast_orig.gif) <br>

<br>

> [!NOTE]
> - This Maven project contains Java conversions of the following project and tests: [Playwright_JavaScript_TypeScript_Healthcare_Platform](https://github.com/RomulusMirauta/Playwright_JavaScript_TypeScript_Healthcare_Platform)
> - Conversion: Playwright + JS/TS -> Selenium + Java + POM + TestNG + BDD-Cucumber + RestAssured
> - This project contains tests created for the following Web App: [Healthcare_Platform_SampleTestObject1](https://github.com/RomulusMirauta/Healthcare_Platform_SampleTestObject1)
> - It uses Selenium for UI automation, RestAssured for API testing, TestNG as the test framework, and Cucumber for BDD-style features

<br>

## Project Structure

- Page Objects (Selenium) under `src/test/java/com/example/pages` (Login, Drugs, Patients)
- API service classes under `src/test/java/com/example/api` (AuthService, DrugsService, PatientsService)
- Cucumber feature files under `src/test/resources/features` (UI + API)
- Step definitions under `src/test/java/com/example/steps` (UI and API)
- TestNG / Cucumber TestNG runner: `com.example.runners.CucumberTestNGRunner`
- A TestNG `testng.xml` suite file is present for running groups and the Cucumber runner
- A simple `DriverFactory` for Selenium / ChromeDriver management
- `DBUtils` helper to run DB validations (optional)

## Prerequisites

- Java 17+ (project is set to release 17)
- Maven 3.9+ (or use the included Maven wrapper/scripts)
- Google Chrome installed (for UI tests)
- Application under test running at `BASE_URL` *(default `http://localhost:3001/`)*
- Optional: Microsoft SQL Server accessible for DB checks (set `DB_USER`, `DB_PASSWORD`, `DB_SERVER`, `DB_NAME`)

## Setting JDK Path Dynamically

To set the JDK path for this project dynamically (without hardcoding), use the following command in your terminal (Windows PowerShell):

```powershell
$env:JAVA_HOME = "$PWD\openJdk-25"
```

This sets JAVA_HOME to the `openJdk-25` folder inside your current project directory. You can add this command to your build or test scripts for convenience.

For Maven, you can also use the `${project.basedir}` property in your `pom.xml` to refer to the project root dynamically.

## Common commands

```bash
# Go to project's main directory
cd my-test

# Build and download dependencies (skip tests)
mvn -DskipTests install

# Run all tests (UI features will require the app backend to be running and possibly a display environment, or headless enabled)
mvn test

# Run only Cucumber (BDD) features using TestNG runner
mvn "-Dtest=com.example.runners.CucumberTestNGRunner" test

# Run a specific feature or test
mvn -Dtest=FirstTest test

# Run all tests, non-headless, fullscreen, with specific environment variables and delays (mid-testing and in-between tests; for recording DEMOs)
Set-Location ".\my-test"; $env:BASE_URL = "http://localhost:3001/index.html"; $env:FULLSCREEN = "true"; $env:DEMO_DELAY_MS = "500"; $env:TEST_DELAY_MS = "1500"; $env:PATH = ($env:PATH -split ';' | Where-Object { $_ -and $_ -ne '.\selenium\drivers\chromedriver' }) -join ';'; $env:PATH = ".\maven\apache-maven-3.9.11\bin;" + $env:PATH; mvn test
```

## Notes

- Many tests expect a running application at `BASE_URL` (default `http://localhost:3001/`). Configure with `-DBASE_URL=http://yourhost:port/` to override.
- For Selenium, set environment variable `HEADLESS=true` for headless runs.
- For demos, set `FULLSCREEN=true` to start Chrome maximized (non-headless only).
- To slow down UI actions for recording, set `DEMO_DELAY_MS` (milliseconds) to pause before each click/fill/text read.
- To add a pause between scenarios/tests, set `TEST_DELAY_MS` (milliseconds).
- If you want DB checks to work, install and configure the SQL Server driver and set `DB_USER`, `DB_PASSWORD`, `DB_SERVER`, and `DB_NAME` environment variables.

## Chrome / ChromeDriver mismatch notes

- ChromeDriver must match the installed Chrome browser major version. If you get errors like "This version of ChromeDriver only supports Chrome version 114", it means the local driver doesn't match your installed Chrome.

- Quick fixes:
   - Remove older `chromedriver.exe` from your PATH (for example, `d:\learn\java\selenium jars and drivers\drivers`), so WebDriverManager can download the correct driver.
   - Alternatively, set an explicit `CHROME_DRIVER_PATH` env var pointing to a chromedriver executable that matches your browser version:
      - PowerShell: `$env:CHROME_DRIVER_PATH = 'C:\path\to\chromedriver.exe'` then run `mvn -Dtest=com.example.runners.CucumberTestNGRunner test`
   - Or set the driver version to download with `CHROME_DRIVER_VERSION` env var (example `142.0.7444.176`):
      - PowerShell: `$env:CHROME_DRIVER_VERSION = '142.0.7444.176'` then run `mvn -Dtest=com.example.runners.CucumberTestNGRunner test`
   - In CI, pin the driver and use headless mode (set `HEADLESS=true`).

### Validating a pinned `chromedriver.exe`

On Windows, verify a local driver binary is valid by running:

- `C:\path\to\chromedriver.exe --version` (PowerShell / cmd)
- The output should start with `ChromeDriver` and include the driver version.
- If the binary reports nothing or errors, it's not a valid chromedriver binary for the current platform.

### Clearing WebDriverManager cache (force a fresh driver download)

WebDriverManager caches drivers under the user cache directory, e.g. `%USERPROFILE%\.cache\selenium\chromedriver\win32\` on Windows.

Remove the folder(s) to force WebDriverManager to download a fresh driver matching your pinned version or the installed Chrome.

PowerShell example:

```powershell
Remove-Item -Recurse -Force $env:USERPROFILE\.cache\selenium\chromedriver\*
```

### Troubleshooting tips

If you're seeing `This version of ChromeDriver only supports Chrome version 114` but your Chrome is v142:

   1. Verify you don't have a pinned path (`CHROME_DRIVER_PATH`) pointing to an old driver; if so, update it.

   2. If you set `CHROME_DRIVER_VERSION`, use the full version string (e.g., `142.0.7444.176`) then clear the WDM cache before running again.

   3. Avoid storing mismatched drivers on PATH that override WebDriverManager behavior.

## Next steps / Improvements

- Add the remainder of Playwright tests if any were missed or when new ones are added
- Add utility classes to centralize locators and wait behaviors
- Add reporting (Allure, etc.) and build pipeline integration
