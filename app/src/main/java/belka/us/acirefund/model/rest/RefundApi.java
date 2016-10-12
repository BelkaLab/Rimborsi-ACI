package belka.us.acirefund.model.rest;

import com.google.api.services.sheets.v4.model.BatchGetValuesResponse;
import com.google.api.services.sheets.v4.model.ValueRange;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by fabriziorizzonelli on 28/09/2016.
 */

public interface RefundApi {

    @GET
    Observable<BatchGetValuesResponse> getRefundsByBrand(@Url String url, @Query("ranges") List<String> ranges, @Query("key") String apiKey);

    @GET("values/{range}")
    Observable<ValueRange> getRefundsByBrandAndByFuelType(@Path("range") String range, @Query("key") String apiKey);
}
