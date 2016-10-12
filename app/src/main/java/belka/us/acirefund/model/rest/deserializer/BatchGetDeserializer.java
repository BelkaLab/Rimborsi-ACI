package belka.us.acirefund.model.rest.deserializer;

import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;
import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by fabriziorizzonelli on 10/10/2016.
 */

public class BatchGetDeserializer implements JsonDeserializer<BatchGetValuesResponse> {

    @Override
    public BatchGetValuesResponse deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) {
        Gson gson = new Gson();
        BatchGetValuesResponse batchGetValuesResponse = gson.fromJson(json, BatchGetValuesResponse.class);

        if (batchGetValuesResponse.getValueRanges().size() > 0) {
            final JsonObject jsonObject = json.getAsJsonObject();

            List<ValueRange> ranges = gson.fromJson(jsonObject.get("valueRanges").getAsJsonArray(), new TypeToken<ArrayList<ValueRange>>(){}.getType());
            batchGetValuesResponse.setValueRanges(ranges);
        }

        return batchGetValuesResponse;
    }
}
