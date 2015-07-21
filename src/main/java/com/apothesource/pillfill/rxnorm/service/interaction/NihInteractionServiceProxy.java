package com.apothesource.pillfill.rxnorm.service.interaction;

import com.apothesource.pillfill.rxnorm.datamodel.interaction.InteractionDrugResponse;
import com.apothesource.pillfill.rxnorm.datamodel.interaction.InteractionListResponse;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

/**
 * Created by "Michael on 7/21/15.
 */
public class NihInteractionServiceProxy implements InteractionService {
    public static final String URL_BASE = "https://rxnav.nlm.nih.gov/REST/interaction";
    public static final String URL_INTERACTION_LIST_TEMPLATE = URL_BASE + "/list.json?rxcuis=%s";
    public static final String URL_INTERACTION_DRUG_TEMPLATE = URL_BASE + "/interaction.json?rxcui=%s";
    private final Gson gson = new Gson();

    @Override
    public InteractionListResponse findInteractionsFromList(List<String> rxcuis) throws IOException{
        StringBuilder builder = new StringBuilder();
        for(String rxcui : rxcuis){
            builder = builder.append(rxcui).append("+");
        }
        URL url = new URL(String.format(URL_INTERACTION_LIST_TEMPLATE, builder.toString()));
        return getResponseFromNihServer(url, InteractionListResponse.class);
    }

    @Override
    public InteractionDrugResponse findDrugInteractions(String rxcui) throws IOException {
        return getResponseFromNihServer(InteractionDrugResponse.class,
                URL_INTERACTION_DRUG_TEMPLATE,
                new String[]{rxcui});
    }

    protected <T> T getResponseFromNihServer(Class<T> classType, String template, String[] parameters) throws IOException {
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
