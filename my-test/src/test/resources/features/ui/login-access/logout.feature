Feature: Logout
  Verify logout behavior

  Scenario: logout redirects to login page
    Given the user has logged in as "admin1" with password "admin194*%&)M"
    When the user clicks logout
    Then the login button should be visible
