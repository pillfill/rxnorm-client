package com.apothesource.pillfill.rxnorm.service.ndfrt;

import com.apothesource.pillfill.rxnorm.datamodel.ndf.Concept;

import java.io.IOException;

/**
 * Created by "Michael on 7/20/15.
 */
public interface ConceptHandler {
    void handleConcept(Concept concept);
    void onError(IOException e);
    void onCompleted();
}
