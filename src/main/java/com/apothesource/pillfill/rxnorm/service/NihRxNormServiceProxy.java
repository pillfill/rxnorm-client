package com.apothesource.pillfill.rxnorm.service;

import com.apothesource.pillfill.rxnorm.datamodel.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Default Implementation of the {@link RxNormService} backed by NIH's services
 * http://rxnav.nlm.nih.gov/RxNormAPIs.html
 *
 * Created by Michael Ramirez (michael@pillfill.com) on 7/20/15.
 */
public class NihRxNormServiceProxy implements RxNormService{
    public static final String URL_BASE = "https://rxnav.nlm.nih.gov/REST";
    public static final String URL_FIND_ID_TEMPLATE = URL_BASE + "/rxcui.json?idtype=%s&id=%s&allsrc=%s";
    public static final String URL_FIND_NAME_TEMPLATE =  URL_BASE + "/rxcui.json?name=%s&search=%s";
    public static final String URL_GET_PROPERTIES_TEMPLATE =  URL_BASE + "/rxcui/%s/allProperties.json?prop=%s";
    public static final String URL_GET_RELATED_TEMPLATE = URL_BASE + "/rxcui/%s/allrelated.json";
    public static final String URL_FIND_APPROXIMATE_TEMPLATE = URL_BASE + "/approximateTerm.json?term=%s&maxEntries=%s&option=0";
    private final Gson gson = new Gson();

    @Override
    public IdGroupResponse findRxcuiById(IdTypeNames type, String id, int allSrc) throws IOException {
        return getResponseFromNihServer(IdGroupResponse.class,
                URL_FIND_ID_TEMPLATE,
                new String[]{type.toString(), id, "" + allSrc});
    }

    @Override
    public IdGroupResponse findRxcuiByName(String name, int search) throws IOException {
        return getResponseFromNihServer(IdGroupResponse.class,
                URL_FIND_NAME_TEMPLATE,
                new String[]{name, "" + search});
    }

    @Override
    public PropertiesGroupResponse getAllProperties(String id, PropCategoryNames propName) throws IOException {
        return getResponseFromNihServer(PropertiesGroupResponse.class,
                URL_GET_PROPERTIES_TEMPLATE,
                new String[]{id, propName.toString()});
    }

    @Override
    public RelatedGroupResponse getAllRelatedInfo(String id) throws IOException{
        return getResponseFromNihServer(RelatedGroupResponse.class,
                URL_GET_RELATED_TEMPLATE,
                new String[]{id});
    }

    @Override
    public ApproximateGroupResponse getApproximateMatch(String id, int maxEntries) throws IOException{
        return getResponseFromNihServer(ApproximateGroupResponse.class,
                URL_FIND_APPROXIMATE_TEMPLATE,
                new String[]{id,"" + maxEntries});
    }

    protected <T> T getResponseFromNihServer(Class<T> classType, String template, String[] parameters) throws IOException{
        for(int i = 0; i < parameters.length; i++){
            parameters[i] = URLEncoder.encode(parameters[i], "UTF-8");
        }
        String urlString = String.format(template, (Object[]) parameters);
        return getResponseFromNihServer(new URL(urlString), classType);
    }

    protected <T> T getResponseFromNihServer(URL url, Class<T> classType) throws IOException{
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());

        T response = gson.fromJson(inputReader, classType);
        inputReader.close();
        return response;
    }
}
