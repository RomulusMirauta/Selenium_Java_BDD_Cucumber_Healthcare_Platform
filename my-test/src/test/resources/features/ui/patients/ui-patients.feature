Feature: Patients UI

  Scenario: Add and remove patient via UI
    Given the user has logged in as "admin1" with password "admin194*%&)M"
    When the user navigates to the Patients page and adds a patient with firstName "ui-patient-<RANDOM>" lastName "Spec" dob "1990-01-01" gender "Other" address "123 Test St"
    Then the patient is visible in the UI
    When the user removes all matching patients with firstName "ui-patient" lastName "Spec"
    Then the patient is not visible in the UI
