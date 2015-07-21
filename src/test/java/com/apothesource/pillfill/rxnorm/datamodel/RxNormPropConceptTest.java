package com.apothesource.pillfill.rxnorm.datamodel;

import com.apothesource.pillfill.rxnorm.datamodel.ndf.NdfrtGroupConceptTest;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.hamcrest.Matchers;
import org.junit.Test;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import static org.junit.Assert.assertThat;

/**
 * Created by "Michael on 7/17/15.
 */

public class RxNormPropConceptTest {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testDeserializePropertiesResponse() throws IOException {
        ClassLoader cl = NdfrtGroupConceptTest.class.getClassLoader();
        File testJsonResponseFile = new File(cl.getResource("json/rxnorm-propconcept-response.json").getFile());
        FileReader jsonStreamReader = new FileReader(testJsonResponseFile);
        PropertiesGroupResponse response = gson.fromJson(jsonStreamReader, PropertiesGroupResponse.class);
        jsonStreamReader.close();

        PropConceptGroup group = response.getPropConceptGroup();
        for(PropConcept concept : group.getPropConcept()){
            assertThat("Concept category is CODES", concept.getPropCategory(), Matchers.is(PropCategoryNames.CODES));
            assertThat("Concept name is NUI", concept.getPropName(), Matchers.equalToIgnoringCase("NUI"));
            assertThat("Concept value is either N0000145914 or N0000007070", concept.getPropValue(), Matchers.in(new String[]{"N0000145914", "N0000007070"}));
        }
    }
}
