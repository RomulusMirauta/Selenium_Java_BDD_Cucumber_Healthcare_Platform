Feature: E2E Patients
  Add and remove patient via UI; confirm removal in DB

  Scenario: E2E add/remove patient and DB check
    Given the user has logged in as "admin1" with password "admin194*%&)M"
    When the user navigates to the Patients page and adds a patient with firstName "E2E" lastName "TestPatient" dob "2000-01-01" gender "Other" address "E2E Test Address"
    Then the patient is visible in the UI
    When the user removes all matching patients with firstName "E2E" lastName "TestPatient"
    Then the patient is not visible in the UI
    And the patient is not present in the DB
