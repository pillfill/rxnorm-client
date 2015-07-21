package com.apothesource.pillfill.rxnorm.service.ndfrt;

import com.apothesource.pillfill.rxnorm.datamodel.ndf.*;
import com.apothesource.pillfill.rxnorm.service.ServiceIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Created by Michael Ramirez on 7/17/15.
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
    @Category(ServiceIntegrationTest.class)
    public void testGetAllInfo() throws IOException{
        FullConceptResponse response = proxy.getAllInfo("N0000146307");
        assertThat("Search nui is N0000146307",response.getResponseType().getInputNui1(),Matchers.is("N0000146307"));

        FullConcept fc = response.getFullConcept();
        assertThat("Concept name is SULINDAC", fc.getConceptName(), Matchers.is("SULINDAC"));
        assertThat("Concept has three children", fc.getChildConcepts().get(0).getConcept().size(), Matchers.is(3));
        assertThat("Concept has two parents", fc.getParentConcepts().get(0).getConcept().size(), Matchers.is(2));
        assertThat("Concept has 13 properties", fc.getGroupProperties().get(0).getProperty().size(), Matchers.is(13));
        assertThat("Concept has 12 roles", fc.getGroupRoles().get(0).getRole().size(), Matchers.is(12));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetChildConcepts() throws IOException{
        GroupConceptResponse response = proxy.getChildConcepts("N0000022046", true);
        GroupConcept conceptGroup = response.getGroupConcepts().get(0);
        assertThat("Concept N0000022046 has five child concepts.", conceptGroup.getConcept().size(), Matchers.is(5));
    }
    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetParentConcepts() throws IOException{
        GroupConceptResponse response = proxy.getParentConcepts("N0000153235", false);
        GroupConcept conceptGroup = response.getGroupConcepts().get(0);
        assertThat("Concept N0000153235 has two parent concepts.", conceptGroup.getConcept().size(), Matchers.is(2));
    }
    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetConceptByProperty() throws IOException{
        GroupConceptResponse response = proxy.getConceptsByProperty(PropertyNames.RX_NORM_CUI, "161");
        GroupConcept conceptGroup = response.getGroupConcepts().get(0);

        assertThat("We found two concepts when querying RXNORM_CUI 161", conceptGroup.getConcept().size(), Matchers.is(2));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetGroupProperties() throws IOException{
        GroupPropertyResponse properties = proxy.getConceptProperties("N0000153235", PropertyNames.UMLS_CUI);
        GroupProperty groupProperty = properties.getGroupProperties().get(0);
        assertThat("Concept has UMLS_CUI",groupProperty.getProperty().get(0).getPropertyName() ,Matchers.is(PropertyNames.UMLS_CUI));
        assertThat("Concept has UMLS_CUI ID: C0690746",groupProperty.getProperty().get(0).getPropertyValue() ,Matchers.is("C0690746"));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetReverseRole() throws IOException{
        GroupConceptResponse concepts = proxy.getRelatedConceptsByReverseRole("N0000000478", RoleNames.MAY_TREAT_NDFRT, false);

        GroupConcept gc = concepts.getGroupConcepts().get(0);
        assertThat("N0000000478 has 5 drug with a MAY_TREAT role", gc.getConcept().size(), Matchers.is(5));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetRole() throws IOException{
        GroupConceptResponse concepts = proxy.getRelatedConceptsByRole("N0000145914", RoleNames.HAS_PE_NDFRT, false);
        GroupConcept gc = concepts.getGroupConcepts().get(0);

        Concept concept = gc.getConcept().get(0);
        assertThat("N0000145914 has a PE of Decreased Organized Electrical Activity", concept.getConceptName(), Matchers.is("Decreased Organized Electrical Activity"));
        assertThat("N0000145914 has a PE with NUI N0000008768", concept.getConceptNui(), Matchers.is("N0000008768"));
        assertThat("N0000145914 has a PE with Kind PHYSIOLOGIC_EFFECT_KIND", concept.getConceptKind(), Matchers.is(KindNames.PHYSIOLOGIC_EFFECT_KIND));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetAssociation() throws IOException{
        GroupAssociationResponse groupAssociationResponse = proxy.getRelatedConceptsByAssociation("N0000152900", AssociationNames.PRODUCT_COMPONENT);
        GroupAssociation ga = groupAssociationResponse.getGroupAssociations().get(0);

        assertThat("groupAssociationResponse inputAssociation is PRODUCT_COMPONENT", groupAssociationResponse.getResponseType().getInputAssociationName(), Matchers.is(AssociationNames.PRODUCT_COMPONENT));
        assertThat("N0000152900 has 2 product component associations", ga.getAssociation().size(), Matchers.is(2));

        for(Association association : ga.getAssociation()){
            Concept associationConcept = association.getConcept().get(0);
            assertThat("N0000152900 product component is in the set [N0000145831,N0000146291]", associationConcept.getConceptNui(), Matchers.in(new String[]{"N0000145831","N0000146291"}));
        }
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetEPCCConcepts() throws IOException{
        GroupConceptResponse response = proxy.getEPCClassOfConcept("N0000145914");
        List<Concept> members = response.getGroupConcepts().get(0).getConcept();
        Concept epccClass = members.get(0);

        assertThat("N0000145914 is in EPCC class Opioid Agonist", epccClass.getConceptName(), Matchers.is("Opioid Agonist"));
        assertThat("N0000145914 is in EPCC class with NUI N0000175690", epccClass.getConceptNui(), Matchers.is("N0000175690"));
        assertThat("N is in EPCC class with Kind DRUG_KIND", epccClass.getConceptKind(), Matchers.is(KindNames.DRUG_KIND));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetVAMemberConcepts() throws IOException{
        GroupConceptResponse response = proxy.getVAClassMembers("N0000029069");
        List<Concept> members = response.getGroupConcepts().get(0).getConcept();
        assertThat("N0000029069 is a VA Class with 7 member concepts", members.size(), Matchers.is(7));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetVAClassConcepts() throws IOException{
        ConceptListResponse response = proxy.getVAClassOfConcept("N0000160729");
        Concept vaConcept = response.getConcept().get(0);
        assertThat("N0000160729 is a VA class member of ANTIHISTAMINES,PIPERAZINE", vaConcept.getConceptName(), Matchers.is("ANTIHISTAMINES,PIPERAZINE"));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetVersion() throws IOException{
        VersionResponse response = proxy.getNdfrtVersion();
        assertThat("Version date is not null", response.getVersion().getVersionName(), Matchers.notNullValue());
        assertThat("Version date matches YYYY.MM.DD format", response.getVersion().getVersionName(), Matchers.matchesPattern("[\\d]{4}\\.[\\d]{2}\\.[\\d]{2}"));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testGetAllConcepts() throws IOException{
        proxy.getAllConceptsByKind(Arrays.asList(KindNames.PHARMACOKINETICS_KIND,KindNames.THERAPEUTIC_CATEGORY_KIND), new ConceptHandler() {
            int conceptCount = 0;

            @Override
            public void handleConcept(Concept concept) {
                assertThat("Concept Kind is PHARMACOKINETICS_KIND or PHARMACOKINETICS_KIND", concept.getConceptKind(), Matchers.in(new KindNames[]{KindNames.PHARMACOKINETICS_KIND,KindNames.THERAPEUTIC_CATEGORY_KIND}));
                conceptCount++;
            }
            @Override
            public void onError(IOException e) {
                fail(String.format("Unexpected exception: %s", e));
            }
            @Override
            public void onCompleted() {
                assertThat("We received 89 concepts", conceptCount, Matchers.is(89));
            }
        });
    }

}