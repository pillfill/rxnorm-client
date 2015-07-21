package com.apothesource.pillfill.rxnorm.service.interaction;

import com.apothesource.pillfill.rxnorm.datamodel.interaction.*;
import com.apothesource.pillfill.rxnorm.service.ServiceIntegrationTest;
import org.hamcrest.Matchers;
import org.junit.Test;
import org.junit.experimental.categories.Category;

import java.util.Arrays;

import static org.junit.Assert.*;

/**
 * Integration Tests for NIH-based Drug Interaction Services
 * Created by Michael Ramirez (michael@pillfill.com) on 7/21/15.
 */
public class NihInteractionServiceProxyTest {
    NihInteractionServiceProxy proxy = new NihInteractionServiceProxy();

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testFindInteractionsFromList() throws Exception {
        InteractionDrugResponse response = proxy.findDrugInteractions("152923");
        InteractionTypeGroup interactionGroup = response.getInteractionTypeGroup().get(0);
        InteractionType interactionType = interactionGroup.getInteractionType().get(0);
        assertThat("RxCUI 152923 has 66 interaction pairs.",
                interactionType.getInteractionPair().size(),
                Matchers.is(66));
    }

    @Test
    @Category(ServiceIntegrationTest.class)
    public void testFindDrugInteractions() throws Exception {
        InteractionListResponse response = proxy.findInteractionsFromList(Arrays.asList("207106","152923","656659"));
        FullInteractionTypeGroup interactionGroup = response.getFullInteractionTypeGroup().get(0);

        assertThat("\"207106\",\"152923\",\"656659\" has 3 interactionTypes present",
                interactionGroup.getFullInteractionType().size(),
                Matchers.is(3));
        for(FullInteractionType interactionType : interactionGroup.getFullInteractionType()){
            assertThat("Interactions have interactionPairs defined.",
                    interactionType.getInteractionPair().size(),
                    Matchers.is(1));
        }

    }
}