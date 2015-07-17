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

public class RxNormIdAllRelatedGroupTest {
    private Gson gson = new GsonBuilder().setPrettyPrinting().create();

    @Test
    public void testDeserializeIdGroupResponse() throws IOException {
        ClassLoader cl = NdfrtGroupConceptTest.class.getClassLoader();
        File testJsonResponseFile = new File(cl.getResource("json/rxnorm-idgroup-response.json").getFile());
        FileReader jsonStreamReader = new FileReader(testJsonResponseFile);
        IdGroupResponse response = gson.fromJson(jsonStreamReader, IdGroupResponse.class);
        jsonStreamReader.close();

        assertThat("Query is NDC", response.getIdGroup().getIdType(), Matchers.equalTo(IdGroupQueryType.NDC));
        assertThat("Query NDC is 0781-1506-10", response.getIdGroup().getId(), Matchers.equalToIgnoringCase("0781-1506-10"));
        assertThat("Response contains 1 RxNormID ID", response.getIdGroup().getRxnormId().size(), Matchers.is(1));
        assertThat("Response contains RxNormID 197381", response.getIdGroup().getRxnormId(), Matchers.hasItem("197381"));
    }
}
