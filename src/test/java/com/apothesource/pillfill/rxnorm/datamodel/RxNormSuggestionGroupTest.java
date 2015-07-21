package com.apothesource.pillfill.rxnorm.datamodel;

import com.apothesource.pillfill.rxnorm.datamodel.ndf.NdfrtGroupConceptTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertThat;

/**
 * Created by "Michael on 7/17/15.
 */

public class RxNormSuggestionGroupTest {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testDeserializeRelatedGroupResponse() throws IOException {
        ClassLoader cl = NdfrtGroupConceptTest.class.getClassLoader();
        File testJsonResponseFile = new File(cl.getResource("json/rxnorm-related-response.json").getFile());
        FileReader jsonStreamReader = new FileReader(testJsonResponseFile);
        AllRelatedGroupResponse response = gson.fromJson(jsonStreamReader, AllRelatedGroupResponse.class);
        jsonStreamReader.close();

        List<ConceptGroup> relatedGroup = response.getAllRelatedGroup().getConceptGroup();
        assertThat("We received 16 relationships", relatedGroup.size(), CoreMatchers.is(16));

        boolean didReceiveProperties = false; //Make sure that we get at least one ConceptProperty from the set
        for(ConceptGroup cg : relatedGroup){
            assertThat("ConceptGroup is not null", cg, Matchers.notNullValue());
            assertThat("ConceptGroup is TTY is set", cg.getTty(), Matchers.notNullValue());
            for(ConceptProperty property : cg.getConceptProperties()){
                didReceiveProperties = true;
                assertThat("Property name is set", property.getName(), Matchers.notNullValue());
                assertThat("Property TTY is set", property.getTty(), Matchers.notNullValue());
                assertThat("Property langugage is set", property.getLanguage(), Matchers.notNullValue());
                assertThat("Property rxcui is set", property.getRxcui(), Matchers.notNullValue());
            }
        }
        assertThat("We saw at least one concept property set", didReceiveProperties, Matchers.is(true));
        assertThat("The rxnormId query is 866350",response.getAllRelatedGroup().getRxcui(), Matchers.equalToIgnoringCase("866350"));
    }
}
