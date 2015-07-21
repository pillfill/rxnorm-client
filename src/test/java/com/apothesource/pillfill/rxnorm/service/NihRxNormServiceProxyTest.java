package com.apothesource.pillfill.rxnorm.service;

import com.apothesource.pillfill.rxnorm.datamodel.*;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.List;

import static org.junit.Assert.assertThat;

/**
 * Integration Test for the NIH-backed RxNormService
 *
 * Created by Michael Ramirez on 7/17/15.
 */
public class NihRxNormServiceProxyTest {
    NihRxNormServiceProxy proxy = new NihRxNormServiceProxy();

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testFindRxcuiById() throws Exception {
        IdGroupResponse queryResponse = proxy.findRxcuiById(IdTypeNames.NDC, "0781-1506-10", 0);
        assertThat("IdGroup query is 0781-1506-10", queryResponse.getIdGroup().getId(), Matchers.is("0781-1506-10"));
        assertThat("Rxnorm ID is 197381", queryResponse.getIdGroup().getRxnormId().get(0), Matchers.in(new String[]{"197381"}));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testFindRxcuiByName() throws IOException{
        IdGroupResponse queryResponse = proxy.findRxcuiByName("lipitor", 0);
        assertThat("Lipitor has RxCUI 153165", queryResponse.getIdGroup().getRxnormId().get(0), Matchers.is("153165"));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetAllProperties() throws IOException{
        PropertiesGroupResponse pcg = proxy.getAllProperties("161", PropCategoryNames.ATTRIBUTES);
        PropConcept concept = pcg.getPropConceptGroup().getPropConcept().get(0);
        assertThat("Acetaminophen has property TTY", concept.getPropName(), Matchers.is("TTY"));
        assertThat("Acetaminophen TTY is IN", concept.getPropValue(), Matchers.is("IN"));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetAllRelatedInfo() throws IOException{
        RelatedGroupResponse relatedInfo = proxy.getAllRelatedInfo("866350");
        List<ConceptGroup> relatedConcepts = relatedInfo.getAllRelatedGroup().getConceptGroup();
        assertThat("Zyrtec Itchy Eye has 16 related concepts", relatedConcepts.size(), Matchers.is(16));
        for(ConceptGroup cg : relatedConcepts){
            if(cg.getConceptProperties().isEmpty()) continue;

            ConceptProperty cp = cg.getConceptProperties().get(0);
            if(cp.getTty().equals(TTYNames.BN)){
                assertThat("Zyrtec has BN Zyrtec Itchy Eye", cp.getName(), Matchers.is("Zyrtec Itchy Eye"));
            }else if(cp.getTty().equals(TTYNames.IN)){
                assertThat("Zyrtec has IN Ketotifen", cp.getName(), Matchers.is("Ketotifen"));
            }
        }
    }
    @Test
    @Category(ServiceIntegrationTest.class)
    public void testSearchApproximateTerm() throws IOException{
        ApproximateGroupResponse approxSearch = proxy.getApproximateMatch("zocor 10 mg", 4);
        List<Candidate> candidates = approxSearch.getApproximateGroup().getCandidate();

        assertThat("We received 4 potential candidates for search \"zocor 10 mg\"", candidates.size(), Matchers.is(4));
    }
}
