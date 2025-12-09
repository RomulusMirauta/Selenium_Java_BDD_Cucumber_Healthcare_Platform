package com.example.steps.api;

import com.example.api.DrugsService;
import com.example.config.Config;
import com.example.steps.common.SharedContext;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.restassured.response.Response;
import org.testng.Assert;

import java.util.List;
import java.util.Map;

public class DrugsApiSteps {
    private final SharedContext context;
    private final DrugsService service = new DrugsService(Config.BASE_URL);
    private Map<String, Object> drugData;
    private Object drugId;
    private Response addResponse;

    public DrugsApiSteps(SharedContext context) {
        this.context = context;
    }

    @When("I add a drug with name {string} description {string} dosage {string}")
    public void add_drug(String name, String description, String dosage) {
        drugData = Map.of(
                "name", name,
                "description", description,
                "dosage", dosage
        );
    addResponse = service.addDrug(drugData, context.username, context.password);
        Assert.assertTrue(addResponse.statusCode() >= 200 && addResponse.statusCode() < 300, "Add drug failed: " + addResponse.statusCode());
    }

    @Then("the drug is present in the drug list")
    public void drug_present() {
    Response getAll = service.getAllDrugs(context.username, context.password);
        List<Map<String, Object>> drugs = getAll.jsonPath().getList("$");
        Map<String, Object> found = drugs.stream().filter(d -> (
                (d.getOrDefault("name", d.get("Name"))).equals(drugData.get("name"))
        )).findFirst().orElse(null);
        Assert.assertNotNull(found, "Drug not found");
        drugId = found.getOrDefault("drugId", found.getOrDefault("id", found.get("DrugID")));
        Assert.assertNotNull(drugId);
    }

    @When("I delete the drug")
    public void delete_drug() {
        Assert.assertNotNull(drugId);
    Response delResponse = service.deleteDrug(drugId, context.username, context.password);
        Assert.assertTrue(delResponse.statusCode() >= 200 && delResponse.statusCode() < 300, "Delete failed");
    }

    @Then("the drug is no longer present in the drug list")
    public void drug_not_present() {
    Response getAll = service.getAllDrugs(context.username, context.password);
        List<Map<String, Object>> drugs = getAll.jsonPath().getList("$");
        boolean exists = drugs.stream().anyMatch(d -> (d.getOrDefault("drugId", d.getOrDefault("id", d.get("DrugID"))) ).equals(drugId));
        Assert.assertFalse(exists, "Drug was not deleted successfully");
    }
}
