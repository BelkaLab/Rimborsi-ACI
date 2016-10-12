package belka.us.acirefund.model.rest;

import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import belka.us.acirefund.model.rest.deserializer.BatchGetDeserializer;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by fabriziorizzonelli on 10/10/2016.
 */

public class GoogleSpreadsheetGsonConverter {

    public static GsonConverterFactory buildGsonConverter() {
        GsonBuilder gsonBuilder = new GsonBuilder();

        // Adding custom deserializers
        gsonBuilder.registerTypeAdapter(BatchGetValuesResponse.class, new BatchGetDeserializer());
        Gson myGson = gsonBuilder.create();

        return GsonConverterFactory.create(myGson);
    }
}
