Feature: Login
  Test login flows via web UI

  Background:
    Given the application is running

  Scenario: Login with valid credentials
    When the user navigates to the login page
    And the user logs in with username "admin1" and password "admin194*%&)M"
    Then the username "admin1" is visible in the UI

  Scenario: Login with invalid credentials
    When the user navigates to the login page
    And the user logs in with username "admin1" and password "wrong_password"
    Then an unauthorized error is shown
