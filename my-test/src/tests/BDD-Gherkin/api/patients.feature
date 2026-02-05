Feature: API Patients Management
  Verify admin can add and remove patients via API

  Scenario: Add, check, and remove patient
    Given the admin credentials are "admin1" and "admin194*%&)M"
    When I add a patient with firstName "Test" lastName "API" dob "2025-10-14" gender "test" address "test"
    Then the patient is present in the patient list
    When I delete the patient
    Then the patient is no longer present in the patient list
