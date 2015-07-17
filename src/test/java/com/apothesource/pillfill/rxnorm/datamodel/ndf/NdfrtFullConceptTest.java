package com.apothesource.pillfill.rxnorm.datamodel.ndf;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertThat;

/**
 * Created by "Michael on 7/16/15.
 */
public class NdfrtFullConceptTest {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testDeserializeFullConceptResponse() throws IOException {
        ClassLoader cl = NdfrtFullConceptTest.class.getClassLoader();
        File testJsonResponseFile = new File(cl.getResource("json/ndfrt-fullconcept-response.json").getFile());
        FileReader jsonStreamReader = new FileReader(testJsonResponseFile);
        FullConceptResponse response = gson.fromJson(jsonStreamReader, FullConceptResponse.class);
        jsonStreamReader.close();

        assertThat("Query NUI is N0000152900", response.getResponseType().getInputNui1(), Matchers.equalToIgnoringCase("N0000152900"));

        FullConcept fullConcept = response.getFullConcept();
        assertThat("FullConcept is set", fullConcept, Matchers.notNullValue());

        assertThat("FullConcept name is ACETIC ACID 2%/HYDROCORTISONE 1% SOLN,OTIC.", fullConcept.getConceptName(), Matchers.equalToIgnoringCase("ACETIC ACID 2%/HYDROCORTISONE 1% SOLN,OTIC"));
        assertThat("FullConcept NUI is N0000152900", fullConcept.getConceptNui(), Matchers.equalToIgnoringCase("N0000152900"));

        List<GroupAssociation> groupAssociations = fullConcept.getGroupAssociations();
        assertThat("GroupAssociations are not empty", groupAssociations.size(),Matchers.greaterThanOrEqualTo(1));

        GroupAssociation groupAssociation = groupAssociations.get(0);

        for(Association association : groupAssociation.getAssociation()) {
            assertThat("Association name is Product_Component", association.getAssociationName(), Matchers.equalToIgnoringCase("Product_Component"));
            assertThat("Concepts are set in the association.", association.getConcept().size(), Matchers.equalTo(1));
        }
    }
}
