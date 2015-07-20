package com.apothesource.pillfill.rxnorm.service.ndfrt;

import com.apothesource.pillfill.rxnorm.datamodel.ndf.*;
import com.apothesource.pillfill.rxnorm.service.ServiceIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;

import static org.junit.Assert.*;

/**
 * Created by "Michael on 7/17/15.
 */
public class NdfrtServiceProxyTest {
    NdfrtServiceProxy proxy = new NdfrtServiceProxy();

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testFindConceptsByID() throws Exception {
        GroupConceptResponse conceptResponse = proxy.findConceptsByID(IdTypeNames.RX_CUI, "161");
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
                    Matchers.in(new KindNames[]{KindNames.DRUG_KIND,KindNames.INGREDIENT_KIND}));
        }
    }

    @Test
    public void testGetAllInfo() throws IOException{
        FullConceptResponse response = proxy.getAllInfo("N0000146307");
        assertThat("Search nui is N0000146307",response.getResponseType().getInputNui1(),Matchers.is("N0000146307"));

        FullConcept fc = response.getFullConcept();
        assertThat("Concept name is SULINDAC", fc.getConceptName(), Matchers.is("SULINDAC"));
        assertThat("Concept has three children", fc.getChildConcepts().get(0).getConcept().size(), Matchers.is(3));
        assertThat("Concept has two parents", fc.getParentConcepts().get(0).getConcept().size(), Matchers.is(2));
    }

    @Test
    public void testGetChildConcepts() throws IOException{
        GroupConceptResponse response = proxy.getChildConcepts("N0000022046", true);
        GroupConcept conceptGroup = response.getGroupConcepts().get(0);
        assertThat("Concept N0000022046 has five child concepts.", conceptGroup.getConcept().size(), Matchers.is(5));
    }
    @Test
    public void testGetParentConcepts() throws IOException{
        GroupConceptResponse response = proxy.getParentConcepts("N0000153235", false);
        GroupConcept conceptGroup = response.getGroupConcepts().get(0);
        assertThat("Concept N0000153235 has two parent concepts.", conceptGroup.getConcept().size(), Matchers.is(2));
    }
}