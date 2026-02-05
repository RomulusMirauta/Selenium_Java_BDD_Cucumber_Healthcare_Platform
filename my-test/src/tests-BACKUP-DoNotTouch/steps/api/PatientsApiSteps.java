package com.example.steps.api;

import com.api.PatientsService;
import com.example.config.Config;
import com.example.steps.common.SharedContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class PatientsApiSteps {
    private final SharedContext context;
    private final PatientsService service = new PatientsService(Config.API_BASE_URL);
    private Response addResponse;
    private Object patientId;
    private Map<String, Object> patientData;

    public PatientsApiSteps(SharedContext context) {
        this.context = context;
    }

    @When("I add a patient with firstName {string} lastName {string} dob {string} gender {string} address {string}")
    public void add_patient(String firstName, String lastName, String dob, String gender, String address) {
        patientData = Map.of(
                "firstName", firstName,
                "lastName", lastName,
                "dob", dob,
                "gender", gender,
                "address", address
        );
    addResponse = service.addPatient(patientData, context.username, context.password);
        Assert.assertTrue(addResponse.statusCode() >= 200 && addResponse.statusCode() < 300, "Add patient failed: " + addResponse.statusCode());
    }

    @Then("the patient is present in the patient list")
    public void patient_present() {
    Response getAll = service.getAllPatients(context.username, context.password);
        List<Map<String, Object>> patients = getAll.jsonPath().getList("$");
        Map<String, Object> found = patients.stream().filter(p -> (
                (p.getOrDefault("firstName", p.get("FirstName"))).equals(patientData.get("firstName")) &&
                        (p.getOrDefault("lastName", p.get("LastName"))).equals(patientData.get("lastName"))
        )).findFirst().orElse(null);
        Assert.assertNotNull(found, "Patient was not found in the patient list");
        patientId = found.getOrDefault("patientId", found.getOrDefault("id", found.get("PatientID")));
        Assert.assertNotNull(patientId, "Patient id not found");
    }

    @When("I delete the patient")
    public void delete_patient() {
        Assert.assertNotNull(patientId, "patientId is null, can't delete");
    Response delResponse = service.deletePatient(patientId, context.username, context.password);
        Assert.assertTrue(delResponse.statusCode() >= 200 && delResponse.statusCode() < 300, "Delete patient failed");
    }

    @Then("the patient is no longer present in the patient list")
    public void patient_not_present() {
    Response getAll = service.getAllPatients(context.username, context.password);
        List<Map<String, Object>> patients = getAll.jsonPath().getList("$");
        boolean exists = patients.stream().anyMatch(p -> (p.getOrDefault("patientId", p.getOrDefault("id", p.get("PatientID"))) ).equals(patientId));
        Assert.assertFalse(exists, "Patient was not deleted successfully");
    }
}
