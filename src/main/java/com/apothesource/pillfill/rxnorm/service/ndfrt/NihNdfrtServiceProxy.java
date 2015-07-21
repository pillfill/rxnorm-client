package com.apothesource.pillfill.rxnorm.service.ndfrt;

import com.apothesource.pillfill.rxnorm.datamodel.ndf.*;
import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonToken;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Default implementation of {@link NdfrtService} backed by the live NIH services.
 * http://rxnav.nlm.nih.gov/NdfrtAPIs.html#
 *
 * Created by Michael Ramirez (michael@pillfill.com) on 7/17/15.
 */
public class NihNdfrtServiceProxy implements NdfrtService {
    public static final String URL_BASE = "https://rxnav.nlm.nih.gov/REST/Ndfrt";

    public static final String URL_ALL_CONCEPTS_TEMPLATE = URL_BASE + "/allconcepts.json?kind=%s";
    public static final String URL_ID_TEMPLATE = URL_BASE + "/id.json?idType=%s&idString=%s";
    public static final String URL_SEARCH_NAME_TEMPLATE = URL_BASE + "/search.son?conceptName=%s&kindName=%s";
    public static final String URL_ALL_TEMPLATE = URL_BASE + "/allInfo.json?nui=%s";
    public static final String URL_CHILDREN_TEMPLATE = URL_BASE + "/childConcepts.json?nui=%s&transitive=%s";
    public static final String URL_PARENTS_TEMPLATE = URL_BASE + "/parentConcepts.json?nui=%s&transitive=%s";
    public static final String URL_SEARCH_PROPERTY_TEMPLATE = URL_BASE + "/concept.json?propertyName=%s&propertyValue=%s";
    public static final String URL_PROPERTY_TEMPLATE = URL_BASE + "/properties.json?nui=%s&propertyName=%s";
    public static final String URL_REVERSE_ROLE_TEMPLATE = URL_BASE + "/reverse.json?nui=%s&roleName=%s&transitive=%s";
    public static final String URL_ROLE_TEMPLATE = URL_BASE + "/role.json?nui=%s&roleName=%s&transitive=%s";
    public static final String URL_ASSOCIATION_TEMPLATE = URL_BASE + "/associations.json?nui=%s&associationName=%s";
    public static final String URL_EPCC_TEMPLATE = URL_BASE + "/EPCC.json?nui=%s";
    public static final String URL_VAMEMBER_TEMPLATE = URL_BASE + "/VAMember.json?nui=%s";
    public static final String URL_VA_TEMPLATE = URL_BASE + "/VA.json?nui=%s";
    public static final String URL_VERSION = URL_BASE + "/version.json";

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
                URL_SEARCH_NAME_TEMPLATE,
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
    public GroupPropertyResponse getConceptProperties(String nui, PropertyNames propertyName) throws IOException {
        return getResponseFromNihServer(GroupPropertyResponse.class,
                URL_PROPERTY_TEMPLATE,
                new String[]{nui, propertyName.toString()});
    }

    @Override
    public GroupConceptResponse getConceptsByProperty(PropertyNames propertyName, String propertyValue) throws IOException {
        return getResponseFromNihServer(GroupConceptResponse.class,
                URL_SEARCH_PROPERTY_TEMPLATE,
                new String[]{propertyName.toString(), propertyValue});
    }

    @Override
    public GroupConceptResponse getRelatedConceptsByReverseRole(String nui, RoleNames roleName, Boolean transitive) throws IOException{
        return getResponseFromNihServer(GroupConceptResponse.class,
                URL_REVERSE_ROLE_TEMPLATE,
                new String[]{nui, roleName.toString(), transitive.toString()});
    }

    @Override
    public GroupConceptResponse getRelatedConceptsByRole(String nui, RoleNames roleName, Boolean transitive) throws IOException {
        return getResponseFromNihServer(GroupConceptResponse.class,
                URL_ROLE_TEMPLATE,
                new String[]{nui, roleName.toString(), transitive.toString()});
    }

    @Override
    public GroupAssociationResponse getRelatedConceptsByAssociation(String nui, AssociationNames assocName) throws IOException {
        return getResponseFromNihServer(GroupAssociationResponse.class,
                URL_ASSOCIATION_TEMPLATE,
                new String[]{nui, assocName.toString()});
    }

    @Override
    public GroupConceptResponse getEPCClassOfConcept(String nui) throws IOException {
        return getResponseFromNihServer(GroupConceptResponse.class,
                URL_EPCC_TEMPLATE, new String[]{nui});
    }

    @Override
    public GroupConceptResponse getVAClassMembers(String nui) throws IOException {
        return getResponseFromNihServer(GroupConceptResponse.class,
                URL_VAMEMBER_TEMPLATE, new String[]{nui});
    }

    @Override
    public ConceptListResponse getVAClassOfConcept(String nui) throws IOException {
        return getResponseFromNihServer(ConceptListResponse.class,
                URL_VA_TEMPLATE, new String[]{nui});
    }

    @Override
    public VersionResponse getNdfrtVersion() throws IOException {
        return getResponseFromNihServer(
                new URL(URL_VERSION),
                VersionResponse.class
        );
    }

    /**
     * Gnarly stream parsing of concept list.
     * TODO: Create a cleaner, reusable stream parser
     */
    @Override
    public void getAllConceptsByKind(List<KindNames> kinds, ConceptHandler handler){
        StringBuilder kindsParam = new StringBuilder();
        for(KindNames kind : kinds){
            kindsParam = kindsParam.append(kind.toString()).append("+");

        }
        String urlString = String.format(URL_ALL_CONCEPTS_TEMPLATE, kindsParam.toString());
        JsonReader reader = null;
        try {
            HttpURLConnection connection = (HttpURLConnection) new URL(urlString).openConnection();
            reader = new JsonReader(new InputStreamReader(connection.getInputStream()));
            reader.beginObject();

            while(reader.hasNext()){
                if(reader.peek() == JsonToken.NAME){
                    String name = reader.nextName();
                    if(name.equalsIgnoreCase("groupConcepts")){
                        reader.beginArray();
                        reader.beginObject();
                    }else if(name.equalsIgnoreCase("concept")){
                        reader.beginArray();
                        while(reader.peek() == JsonToken.BEGIN_OBJECT){
                            Concept concept = new Concept();
                            reader.beginObject();

                            while(reader.peek() == JsonToken.NAME) {
                                name = reader.nextName();
                                String value = reader.nextString();
                                if (name.equalsIgnoreCase("conceptName")) {
                                    concept.setConceptName(value);
                                } else if (name.equalsIgnoreCase("conceptNui")) {
                                    concept.setConceptNui(value);
                                } else if (name.equalsIgnoreCase("conceptKind")) {
                                    concept.setConceptKind(KindNames.valueOf(value));
                                }
                            }
                            handler.handleConcept(concept);
                            reader.endObject();
                        }
                    }
                }else{
                    reader.skipValue();
                }
            }

            handler.onCompleted();
        } catch (IOException e) {
            handler.onError(e);
        } finally {
            if(reader != null) try {
                reader.close();
            } catch (IOException e) {
                handler.onError(e);
            }
        }
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