package com.apothesource.pillfill.rxnorm.service;

import com.apothesource.pillfill.rxnorm.datamodel.IdGroupResponse;
import com.apothesource.pillfill.rxnorm.datamodel.IdTypeNames;
import org.hamcrest.Matchers;
import org.junit.Test;

import static org.junit.Assert.assertThat;

/**
 * Created by "Michael on 7/17/15.
 */
public class RxNormServiceIntegrationTest {
    RxnormServiceProxy proxy = new RxnormServiceProxy();

    @Test
    public void testFindRxcuiById() throws Exception {
        IdGroupResponse queryResponse = proxy.findRxcuiById(IdTypeNames.NDC, "0781-1506-10", false);
        assertThat("IdGroup query is 0781-1506-10", queryResponse.getIdGroup().getId(), Matchers.is("0781-1506-10"));
        assertThat("Rxnorm ID is 197381", queryResponse.getIdGroup().getRxnormId().get(0), Matchers.in(new String[]{"197381"}));
    }
}
