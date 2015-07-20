package com.apothesource.pillfill.rxnorm.service.ndfrt;

import com.apothesource.pillfill.rxnorm.datamodel.ndf.*;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Michael on 7/17/15.
 */
public class NdfrtServiceProxy implements NdfrtService {
    public static final String URL_BASE = "https://rxnav.nlm.nih.gov/REST/Ndfrt";

    public static final String URL_ID_TEMPLATE = URL_BASE + "/id.json?idType=%s&idString=%s";
    public static final String URL_SEARCH_TEMPLATE = URL_BASE + "/search.son?conceptName=%s&kindName=%s";
    public static final String URL_ALL_TEMPLATE = URL_BASE + "/allInfo.json?nui=%s";
    public static final String URL_CHILDREN_TEMPLATE = URL_BASE + "/childConcepts.json?nui=%s&transitive=%s";
    public static final String URL_PARENTS_TEMPLATE = URL_BASE + "/parentConcepts.json?nui=%s&transitive=%s";

    private final Gson gson = new Gson();

    @Override
    public GroupConceptResponse findConceptsByID(IdTypeNames idType, String idString) throws IOException{
        return getResponseFromNihServer(GroupConceptResponse.class,
                URL_ID_TEMPLATE,
                new String[]{idType.toString(),idString});
    }

    @Override
    public FullConceptResponse getAllInfo(String nui) throws IOException {
        return getResponseFromNihServer(FullConceptResponse.class,
                URL_ALL_TEMPLATE,
                new String[]{nui});
    }

    @Override
    public GroupConceptResponse findConceptsByName(String conceptName, KindNames kindName) throws IOException {
        return getResponseFromNihServer(GroupConceptResponse.class,
                URL_SEARCH_TEMPLATE,
                new String[]{conceptName, kindName.toString()});
    }

    @Override
    public GroupConceptResponse getChildConcepts(String nui, Boolean transitive) throws IOException {
        return getResponseFromNihServer(GroupConceptResponse.class,
                URL_CHILDREN_TEMPLATE,
                new String[]{nui, transitive.toString()});
    }

    @Override
    public GroupConceptResponse getParentConcepts(String nui, Boolean transitive) throws IOException {
        return getResponseFromNihServer(GroupConceptResponse.class,
                URL_PARENTS_TEMPLATE,
                new String[]{nui, transitive.toString()});
    }

    @Override
    public GroupConceptResponse getConceptProperties(String nui, PropertyNames propertyName) throws IOException {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public GroupConceptResponse getConceptsByProperty(PropertyNames propertyName, String propertyValue) throws IOException {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public GroupConceptResponse getRelatedConceptsByReverseRole(String nui, RoleNames roleName, boolean transitive) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public GroupConceptResponse getRelatedConceptsByRole(String nui, RoleNames roleName, boolean transitive) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public GroupConceptResponse getRelatedConceptsByAssociation(String nui, AssociationNames assocName) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public GroupConceptResponse getEPCClassOfConcept(String nui) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public GroupConceptResponse getVAClassMembers(String nui) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    @Override
    public GroupConceptResponse getVAClassOfConcept(String nui) {
        throw new UnsupportedOperationException("Not Implemented");
    }

    protected <T> T getResponseFromNihServer(Class<T> classType, String template, String[] parameters) throws IOException{
        for(int i = 0; i < parameters.length; i++){
            parameters[i] = URLEncoder.encode(parameters[i], "UTF-8");
        }
        String urlString = String.format(template, parameters);
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