package com.apothesource.pillfill.rxnorm.service.ndfrt;

import com.apothesource.pillfill.rxnorm.datamodel.ndf.Concept;
import com.apothesource.pillfill.rxnorm.datamodel.ndf.GroupConcept;
import com.apothesource.pillfill.rxnorm.datamodel.ndf.GroupConceptResponse;
import com.apothesource.pillfill.rxnorm.service.ServiceIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import static org.junit.Assert.*;

/**
 * Created by "Michael on 7/17/15.
 */
public class NdfrtServiceProxyTest {
    NdfrtServiceProxy proxy = new NdfrtServiceProxy();

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testFindConceptsByID() throws Exception {
        GroupConceptResponse conceptResponse = proxy.findConceptsByID("RXCUI", "161");
        assertThat("Input ID is 161", conceptResponse.getResponseType().getInputIdString(), Matchers.equalTo("161"));

        GroupConcept groupConcept = conceptResponse.getGroupConcepts().get(0);
        for(Concept concept : groupConcept.getConcept()){
            assertThat("Expected concept name was returned.",
                    concept.getConceptName(),
                    Matchers.in(new String[]{"ACETAMINOPHEN","Acetaminophen"}));

            assertThat("Expected concept NUI was returned.",
                    concept.getConceptNui(),
                    Matchers.in(new String[]{"N0000007359","N0000145898"}));

            assertThat("Expected concept kind was returned.",
                    concept.getConceptKind(),
                    Matchers.in(new String[]{"DRUG_KIND","INGREDIENT_KIND"}));
        }
    }
}