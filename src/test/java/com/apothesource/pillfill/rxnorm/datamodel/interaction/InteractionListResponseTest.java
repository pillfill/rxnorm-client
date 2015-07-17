package com.apothesource.pillfill.rxnorm.datamodel.interaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertThat;

/**
 * Created by "Michael on 7/16/15.
 */
public class InteractionListResponseTest {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testDeserializeInteractionListResponse() throws IOException {
        ClassLoader cl = InteractionListResponseTest.class.getClassLoader();
        File testJsonResponseFile = new File(cl.getResource("json/interaction-list-response.json").getFile());
        FileReader jsonStreamReader = new FileReader(testJsonResponseFile);
        InteractionListResponse response = gson.fromJson(jsonStreamReader, InteractionListResponse.class);
        jsonStreamReader.close();

        assertThat("InteractionDrugResponse is populated", response, Matchers.notNullValue());

        FullInteractionTypeGroup interactionGroup = response.getFullInteractionTypeGroup().get(0);
        assertThat("FullInteractionTypeGroup list is populated", interactionGroup.getFullInteractionType().size(), Matchers.greaterThanOrEqualTo(1));

        FullInteractionType interaction = interactionGroup.getFullInteractionType().get(0);

        assertThat("First drug is Simvastatin 40 MG Oral Tablet [Zocor]", interaction.getMinConcept().get(0).getName(), Is.is("Simvastatin 40 MG Oral Tablet [Zocor]"));
        assertThat("Second drug is bosentan 125 MG Oral Tablet", interaction.getMinConcept().get(1).getName(), Is.is("bosentan 125 MG Oral Tablet"));

    }
}
