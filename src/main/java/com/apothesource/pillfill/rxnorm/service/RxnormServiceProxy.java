package com.apothesource.pillfill.rxnorm.service;

import com.apothesource.pillfill.rxnorm.datamodel.IdGroupResponse;
import com.apothesource.pillfill.rxnorm.datamodel.IdTypeNames;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by "Michael on 7/20/15.
 */
public class RxnormServiceProxy {
    public static final String URL_BASE = "https://rxnav.nlm.nih.gov/REST";
    public static final String URL_SEARCH_TEMPLATE = URL_BASE + "/rxcui.json?idtype=%s&id=%s&allsrc=%s";

    private final Gson gson = new Gson();

    /**
     * Get the concepts for a specified name and kind.
     * http://rxnav.nlm.nih.gov/RxNormAPIs.html#uLink=RxNorm_REST_findRxcuiById
     *
     * @param idType - the identifier type.
     * @param id - The search term
     * @param allSrc - An optional field indicating whether all RxCUIs are to be returned. If set to 1, all non-suppressed RxCUIs will be returned, including those which contain no terms from the RxNorm vocabulary. If set to 0 (the default), only RxCUIs which contain a non-suppressed RxNorm vocabulary term will be returned.
     * @see com.apothesource.pillfill.rxnorm.datamodel.IdTypeNames
     */
    public IdGroupResponse findRxcuiById(IdTypeNames idType, String id, boolean allSrc) throws IOException {
        String allSrcValue = allSrc ? "1" : "0";
        String urlString = String.format(URL_SEARCH_TEMPLATE, idType.name(), id, allSrcValue);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());
        IdGroupResponse response = gson.fromJson(inputReader, IdGroupResponse.class);
        inputReader.close();
        return response;
    }
}
