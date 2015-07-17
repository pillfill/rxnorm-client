package com.apothesource.pillfill.rxnorm.datamodel.interaction;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.Matchers;
import org.hamcrest.core.Is;
import org.junit.Test;

import java.io.*;
import java.util.List;

import static org.junit.Assert.assertThat;

/**
 * Created by "Michael on 7/16/15.
 */
public class InteractionDrugResponseTest {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testDeserializeInteractionResponse() throws IOException {
        ClassLoader cl = InteractionDrugResponseTest.class.getClassLoader();
        File testJsonResponseFile = new File(cl.getResource("json/interaction-drug-response.json").getFile());
        FileReader jsonStreamReader = new FileReader(testJsonResponseFile);
        InteractionDrugResponse response = gson.fromJson(jsonStreamReader, InteractionDrugResponse.class);
        jsonStreamReader.close();

        assertThat("InteractionDrugResponse is populated", response, Matchers.notNullValue());

        List<InteractionTypeGroup> interactionGroup = response.getInteractionTypeGroup();
        assertThat("interactionGroup list is populated", interactionGroup.size(), Matchers.greaterThanOrEqualTo(1));

        InteractionType interaction = interactionGroup.get(0).getInteractionType().get(0);

        System.out.println(gson.toJson(interaction));
        assertThat("Drug is flunisolide", interaction.getMinConceptItem().getName(), Is.is("flunisolide"));

    }
}
