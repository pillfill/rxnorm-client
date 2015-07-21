package com.apothesource.pillfill.rxnorm.service.interaction;

import com.apothesource.pillfill.rxnorm.datamodel.interaction.InteractionDrugResponse;
import com.apothesource.pillfill.rxnorm.datamodel.interaction.InteractionListResponse;

import java.io.IOException;
import java.util.List;

/**
 * The Drug Interaction RESTful web API is a simple web service implemented using HTTP and can be thought of as a
 * collection of resources, specified as URIs.
 * <br/>
 * <a href="http://rxnav.nlm.nih.gov/InteractionAPIREST.html">NIH Documentation.</a>
 * <br/>
 * Note: Some of the parameters & ordering in this interface are intentionally inconsistent to match the NIH documentation.
 *
 * Created by Michael Ramirez (michael@pillfill.com) on 7/21/15.
 */
public interface InteractionService {

    /**
     * Find the interactions between a list of drugs.
     *
     * @param rxcuis the list of RxNorm drugs, specified by the RxNorm identifiers. The identifiers can represent
     *               ingredients (e.g. simvastatin), brand names (e.g. Tylenol) or branded or clinical drugs (e.g.
     *               Morphine 50 mg oral tablet). A maximum of 50 identifiers is allowed.
     * @return A list of potential interactions between the specified concepts
     */
    InteractionListResponse findInteractionsFromList(List<String> rxcuis) throws IOException;

    /**
     * Get the drug interactions for a specified drug.
     *
     * @param rxcui the RxNorm identifier of the drug.
     * @return A list of potential interactions for the specified drug
     */
    InteractionDrugResponse findDrugInteractions(String rxcui) throws IOException;
}
