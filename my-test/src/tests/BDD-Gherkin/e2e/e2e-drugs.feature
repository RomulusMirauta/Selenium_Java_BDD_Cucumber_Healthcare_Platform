Feature: E2E Drugs
  Add and remove drug via UI; confirm removal in DB

  Scenario: E2E add/remove drug and DB check
    Given the user has logged in as "admin1" with password "admin194*%&)M"
    When the user navigates to the Drugs page and adds a drug with name "E2E TestDrug" description "E2E Test Drug Description" dosage "123mg"
    Then the drug is visible in the UI
    When the user removes all matching drugs with name "E2E TestDrug" description "E2E Test Drug Description" dosage "123mg"
    Then the drug is not visible in the UI
    And the drug is not present in the DB
