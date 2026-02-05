Feature: Access control and role-based sections
  Verify different users can access correct sections and logout is present

  Scenario: Admin can access all sections and see username and logout
    Given the user has logged in as "admin1" with password "admin194*%&)M"
  Then the user should see sections 'Patients,Drugs,Reports,Admin,Logout' and should NOT see ''
    Then the username "admin1" is visible in the UI

  Scenario: user_drugs has limited access to drugs
    Given the user has logged in as "user_drugs" with password "user_drugs194*%&)M"
  Then the user should see sections 'Drugs' and should NOT see 'Patients,Reports,Admin'
    Then the username "user_drugs" is visible in the UI

  Scenario: user_patients has limited access to patients
    Given the user has logged in as "user_patients" with password "user_patients194*%&)M"
  Then the user should see sections 'Patients' and should NOT see 'Drugs,Reports,Admin'
    Then the username "user_patients" is visible in the UI
