Feature: API Drugs Management
  Verify admin can add and remove drugs via API

  Scenario: Add, check, and remove drug
    Given the admin credentials are "admin1" and "admin194*%&)M"
    When I add a drug with name "TestDrug_API" description "Test drug description" dosage "10mg"
    Then the drug is present in the drug list
    When I delete the drug
    Then the drug is no longer present in the drug list
