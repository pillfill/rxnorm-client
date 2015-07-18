package com.apothesource.pillfill.rxnorm.service.ndfrt;

import com.apothesource.pillfill.rxnorm.datamodel.ndf.GroupConceptResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by "Michael on 7/17/15.
 */
public class NdfrtServiceProxy {
    public static final String URL_BASE = "https://rxnav.nlm.nih.gov/REST/Ndfrt";
    public static final String URL_ID_TEMPLATE = URL_BASE + "/id.json?idType=%s&idString=%s";

    private final Gson gson = new Gson();

    /**
     * http://rxnav.nlm.nih.gov/NdfrtAPIs.html#uLink=Ndfrt_REST_findConceptsByID
     *  @param idType - the type of identifier. See /typeList for valid identifier types.
     * @param idString - the value of the identifier
     */
    public GroupConceptResponse findConceptsByID(String idType, String idString) throws IOException{
        String urlString = String.format(URL_ID_TEMPLATE, idType, idString);
        URL url = new URL(urlString);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        InputStreamReader inputReader = new InputStreamReader(connection.getInputStream());
        GroupConceptResponse response = gson.fromJson(inputReader, GroupConceptResponse.class);
        inputReader.close();
        return response;
    }
}

/**

 /id	            Get the NDF-RT concept for a specified identifier
 /search	        Get the concepts for a specified name and kind
 /allconcepts	    Get concept information for specified kinds.
 /allInfo	        Get concept information
 /associationList	Get the association names
 /childConcepts	    Get the child concepts for a specified concept
 /properties	    Get the concept property value for a specified concept and property name
 /concept	        Get the concepts that contain a specified property name and value
 /EPCC	            Get the FDA Established Pharmacologic Classes (EPC) for a concept
 /typeList	        Get the identifier type names
 /kindList	        Get the kind names in the NDF-RT data set
 /version	        Get the version of the NDF-RT data set
 /parentConcepts	Get the parent concepts of a specified concept
 /propertyList	    Get the property names in the NDF-RT data set
 /associations	    Get the associations for a specified concept
 /reverse	        Get the concepts related to the specified object concept by role
 /role	            Get the concepts related to the specified concept by role
 /roleList	        Get the role names in the NDF-RT data set
 /VAMember	        Get the members of a VA class
 /VA	            Get the VA class for a specified concept

 */
