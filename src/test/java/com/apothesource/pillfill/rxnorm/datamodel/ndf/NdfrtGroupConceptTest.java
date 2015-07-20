package com.apothesource.pillfill.rxnorm.datamodel.ndf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertThat;

/**
 * Created by "Michael on 7/16/15.
 */
public class NdfrtGroupConceptTest {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testDeserializeGroupConceptResponse() throws IOException {
        ClassLoader cl = NdfrtGroupConceptTest.class.getClassLoader();
        File testJsonResponseFile = new File(cl.getResource("json/ndfrt-groupconcept-response.json").getFile());
        FileReader jsonStreamReader = new FileReader(testJsonResponseFile);
        GroupConceptResponse response = gson.fromJson(jsonStreamReader, GroupConceptResponse.class);
        jsonStreamReader.close();

        assertThat("Query is RxNormID", response.getResponseType().getInputPropertyName(), Matchers.equalTo(PropertyNames.RX_NORM_CUI));
        assertThat("Query is RxNormID 161", response.getResponseType().getInputPropertyValue(), Matchers.equalToIgnoringCase("161"));

        System.out.println(gson.toJson(response));

        GroupConcept groupConcept = response.getGroupConcepts().get(0);
        Concept firstConcept = groupConcept.getConcept().get(0);
        Concept secondConcept = groupConcept.getConcept().get(1);


        assertThat("First drug is ACETAMINOPHEN", firstConcept.getConceptName(), Matchers.equalToIgnoringCase("ACETAMINOPHEN"));
        assertThat("First drug is N0000145898", firstConcept.getConceptNui(), Matchers.equalToIgnoringCase("N0000145898"));
        assertThat("First drug kind is DRUG_KIND", firstConcept.getConceptKind(), Matchers.equalTo(KindNames.DRUG_KIND));
        assertThat("Second drug is Acetaminophen", secondConcept.getConceptName(), Matchers.equalToIgnoringCase("Acetaminophen"));
        assertThat("Second drug is N0000007359", secondConcept.getConceptNui(), Matchers.equalToIgnoringCase("N0000007359"));
        assertThat("Second drug kind is INGREDIENT_KIND", secondConcept.getConceptKind(), Matchers.equalTo(KindNames.INGREDIENT_KIND));

    }
}
