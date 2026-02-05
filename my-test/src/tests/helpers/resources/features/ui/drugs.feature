Feature: Drugs UI
  Add and remove drug via UI

  Scenario: Add and remove drug via UI
    Given the user has logged in as "admin1" with password "admin194*%&)M"
    When the user navigates to the Drugs page and adds a drug with name "ui-test-<RANDOM>" description "Automated test drug" dosage "10mg"
    Then the drug is visible in the UI
    When the user removes the drug
    Then the drug is no longer visible
