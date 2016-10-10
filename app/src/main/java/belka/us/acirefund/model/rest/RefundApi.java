package belka.us.acirefund.model.rest;

import com.google.api.services.sheets.v4.model.ValueRange;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

public interface RefundApi {

    @GET("values/TabACI!A3:E1000")
//    Observable<ValueRange> getRefunds(@Query("key") String apiKey);
    Observable<ValueRange> getRefunds();
}
